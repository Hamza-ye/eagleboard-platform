package com.mass3d.parser.expression;

import static com.mass3d.expression.MissingValueStrategy.NEVER_SKIP;
import static com.mass3d.parser.expression.ParserUtils.DOUBLE_VALUE_IF_NULL;
import static com.mass3d.parser.expression.ParserUtils.ITEM_REGENERATE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.ParserRuleContext;
import org.apache.commons.lang3.Validate;
import org.hisp.dhis.antlr.AntlrExpressionVisitor;
import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import com.mass3d.common.DimensionService;
import com.mass3d.common.DimensionalItemId;
import com.mass3d.common.MapMap;
import com.mass3d.common.ValueType;
import com.mass3d.constant.Constant;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementService;
import com.mass3d.expression.MissingValueStrategy;
import com.mass3d.i18n.I18n;
import com.mass3d.jdbc.StatementBuilder;
import com.mass3d.organisationunit.OrganisationUnitGroupService;
import com.mass3d.period.Period;
import com.mass3d.program.ProgramIndicator;
import com.mass3d.program.ProgramIndicatorService;
import com.mass3d.program.ProgramStage;
import com.mass3d.program.ProgramStageService;
import com.mass3d.relationship.RelationshipTypeService;
import com.mass3d.trackedentity.TrackedEntityAttributeService;

/**
 * Common traversal of the ANTLR4 expression parse tree using the visitor
 * pattern.
 *
 */
public class CommonExpressionVisitor
    extends AntlrExpressionVisitor
{
    private DimensionService dimensionService;

    private OrganisationUnitGroupService organisationUnitGroupService;

    private ProgramIndicatorService programIndicatorService;

    private ProgramStageService programStageService;

    private DataElementService dataElementService;

    private TrackedEntityAttributeService attributeService;

    private RelationshipTypeService relationshipTypeService;

    private StatementBuilder statementBuilder;

    private I18n i18n;

    /**
     * Map of ExprItem instances to call for each expression item
     */
    private Map<Integer, ExpressionItem> itemMap;

    /**
     * Method to call within the ExprItem instance
     */
    private ExpressionItemMethod itemMethod;

    /**
     * By default, replace nulls with 0 or ''.
     */
    private boolean replaceNulls = true;

    /**
     * By default, the offset is not set.
     */
    private int periodOffset = 0;

    /**
     * Used to collect the string replacements to build a description.
     */
    private Map<String, String> itemDescriptions = new HashMap<>();

    /**
     * Constants to use in evaluating an expression.
     */
    private Map<String, Constant> constantMap = new HashMap<>();

    /**
     * Used to collect the dimensional item ids in the expression.
     */
    private Set<DimensionalItemId> itemIds = new HashSet<>();

    /**
     * Used to collect the sampled dimensional item ids in the expression.
     */
    private Set<DimensionalItemId> sampleItemIds = new HashSet<>();

    /**
     * Used to collect the organisation unit group ids in the expression.
     */
    private Set<String> orgUnitGroupIds = new HashSet<>();

    /**
     * Organisation unit group counts to use in evaluating an expression.
     */
    Map<String, Integer> orgUnitCountMap = new HashMap<>();

    /**
     * Count of days in period to use in evaluating an expression.
     */
    private Double days = null;

    /**
     * Values to use for dimensional items in evaluating an expression.
     */
    private Map<String, Double> itemValueMap;

    /**
     * Dimensional item values by period for aggregating in evaluating an
     * expression.
     */
    private MapMap<Period, String, Double> periodItemValueMap;

    /**
     * Periods to sample over for predictor sample functions.
     */
    private List<Period> samplePeriods;

    /**
     * Count of dimension items found.
     */
    private int itemsFound = 0;

    /**
     * Count of dimension item values found.
     */
    private int itemValuesFound = 0;

    /**
     * Strategy for handling missing values.
     */
    private MissingValueStrategy missingValueStrategy = NEVER_SKIP;

    /**
     * Current program indicator.
     */
    private ProgramIndicator programIndicator;

    /**
     * Reporting start date.
     */
    private Date reportingStartDate;

    /**
     * Reporting end date.
     */
    private Date reportingEndDate;

    /**
     * Idenfitiers of DataElements and Attribuetes in expression.
     */
    private Set<String> dataElementAndAttributeIdentifiers;

    /**
     * Default value for data type double.
     */
    public static final double DEFAULT_DOUBLE_VALUE = 1d;

    /**
     * Default value for data type date.
     */
    public static final String DEFAULT_DATE_VALUE = "2017-07-08";

    /**
     * Default value for data type boolean.
     */
    public static final boolean DEFAULT_BOOLEAN_VALUE = false;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    protected CommonExpressionVisitor()
    {
    }

    /**
     * Creates a new Builder for CommonExpressionVisitor.
     *
     * @return a Builder for CommonExpressionVisitor.
     */
    public static Builder newBuilder()
    {
        return new CommonExpressionVisitor.Builder();
    }

    // -------------------------------------------------------------------------
    // Visitor methods
    // -------------------------------------------------------------------------

    @Override
    public Object visitExpr( ExprContext ctx )
    {
        if ( ctx.it != null )
        {
            ExpressionItem item = itemMap.get( ctx.it.getType() );

            if ( item == null )
            {
                throw new ParserExceptionWithoutContext(
                    "Item " + ctx.it.getText() + " not supported for this type of expression" );
            }

            return itemMethod.apply( item, ctx, this );
        }

        if ( itemMethod == ITEM_REGENERATE )
        {
            return regenerateAllChildren( ctx );
        }

        if ( ctx.expr().size() > 0 ) // If there's an expr, visit the expr
        {
            return visit( ctx.expr( 0 ) );
        }

        return visit( ctx.getChild( 0 ) ); // All others: visit first child.
    }

    // -------------------------------------------------------------------------
    // Logic for expression items
    // -------------------------------------------------------------------------

    /**
     * Visits a context while allowing null values (not replacing them with 0 or
     * ''), even if we would otherwise be replacing them.
     *
     * @param ctx any context
     * @return the value while allowing nulls
     */
    public Object visitAllowingNulls( ParserRuleContext ctx )
    {
        boolean savedReplaceNulls = replaceNulls;

        replaceNulls = false;

        Object result = visit( ctx );

        replaceNulls = savedReplaceNulls;

        return result;
    }

    /**
     * Visits a context using a period offset.
     *
     * @param ctx any context
     * @return the value with the applied offset
     */
    public Object visitWithOffset( ParserRuleContext ctx, int offset )
    {
        int savedPeriodOffset = periodOffset;

        periodOffset = offset;

        Object result = visit( ctx );

        periodOffset = savedPeriodOffset;

        return result;
    }

    /**
     * Handles nulls and missing values.
     * <p/>
     * If we should replace nulls with the default value, then do so, and remember
     * how many items found, and how many of them had values, for subsequent
     * MissingValueStrategy analysis.
     * <p/>
     * If we should not replace nulls with the default value, then don't, as this is
     * likely for some function that is testing for nulls, and a missing value
     * should not count towards the MissingValueStrategy.
     *
     * @param value the (possibly null) value
     * @return the value we should return.
     */
    public Object handleNulls( Object value )
    {
        if ( replaceNulls )
        {
            itemsFound++;

            if ( value == null )
            {
                return DOUBLE_VALUE_IF_NULL;
            }
            else
            {
                itemValuesFound++;
            }
        }

        return value;
    }

    /**
     * Validates a program stage id / data element id pair
     *
     * @param text expression text containing both program stage id and data element
     *        id
     * @param programStageId the program stage id
     * @param dataElementId the data element id
     * @return the ValueType of the data element
     */
    public ValueType validateStageDataElement( String text, String programStageId, String dataElementId )
    {
        ProgramStage programStage = programStageService.getProgramStage( programStageId );
        DataElement dataElement = dataElementService.getDataElement( dataElementId );

        if ( programStage == null )
        {
            throw new ParserExceptionWithoutContext(
                "Program stage " + programStageId + " not found" );
        }

        if ( dataElement == null )
        {
            throw new ParserExceptionWithoutContext( "Data element " + dataElementId + " not found" );
        }

        String description = programStage.getDisplayName() + ProgramIndicator.SEPARATOR_ID
            + dataElement.getDisplayName();

        itemDescriptions.put( text, description );

        return dataElement.getValueType();
    }

    /**
     * Regenerates an expression by visiting all the children of the expression node
     * (including any terminal nodes).
     *
     * @param ctx the expression context
     * @return the regenerated expression (as a String)
     */
    public Object regenerateAllChildren( ExprContext ctx )
    {
        return ctx.children.stream().map( this::castStringVisit )
            .collect( Collectors.joining() );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public DimensionService getDimensionService()
    {
        return dimensionService;
    }

    public OrganisationUnitGroupService getOrganisationUnitGroupService()
    {
        return organisationUnitGroupService;
    }

    public ProgramIndicatorService getProgramIndicatorService()
    {
        return programIndicatorService;
    }

    public ProgramStageService getProgramStageService()
    {
        return programStageService;
    }

    public DataElementService getDataElementService()
    {
        return dataElementService;
    }

    public TrackedEntityAttributeService getAttributeService()
    {
        return attributeService;
    }

    public RelationshipTypeService getRelationshipTypeService()
    {
        return relationshipTypeService;
    }

    public StatementBuilder getStatementBuilder()
    {
        return statementBuilder;
    }

    public I18n getI18n()
    {
        return i18n;
    }

    public ProgramIndicator getProgramIndicator()
    {
        return programIndicator;
    }

    public void setProgramIndicator(
        ProgramIndicator programIndicator )
    {
        this.programIndicator = programIndicator;
    }

    public Date getReportingStartDate()
    {
        return reportingStartDate;
    }

    public void setReportingStartDate( Date reportingStartDate )
    {
        this.reportingStartDate = reportingStartDate;
    }

    public Date getReportingEndDate()
    {
        return reportingEndDate;
    }

    public void setReportingEndDate( Date reportingEndDate )
    {
        this.reportingEndDate = reportingEndDate;
    }

    public Set<String> getDataElementAndAttributeIdentifiers()
    {
        return dataElementAndAttributeIdentifiers;
    }

    public void setDataElementAndAttributeIdentifiers(
        Set<String> dataElementAndAttributeIdentifiers )
    {
        this.dataElementAndAttributeIdentifiers = dataElementAndAttributeIdentifiers;
    }

    public Map<String, String> getItemDescriptions()
    {
        return itemDescriptions;
    }

    public Map<String, Constant> getConstantMap()
    {
        return constantMap;
    }

    public boolean getReplaceNulls()
    {
        return replaceNulls;
    }

    public void setReplaceNulls( boolean replaceNulls )
    {
        this.replaceNulls = replaceNulls;
    }

    public int getPeriodOffset()
    {
        return periodOffset;
    }

    public void setPeriodOffset( int periodOffset )
    {
        this.periodOffset = periodOffset;
    }

    public Set<DimensionalItemId> getItemIds()
    {
        return itemIds;
    }

    public void setItemIds( Set<DimensionalItemId> itemIds )
    {
        this.itemIds = itemIds;
    }

    public Set<DimensionalItemId> getSampleItemIds()
    {
        return sampleItemIds;
    }

    public void setSampleItemIds( Set<DimensionalItemId> sampleItemIds )
    {
        this.sampleItemIds = sampleItemIds;
    }

    public Set<String> getOrgUnitGroupIds()
    {
        return orgUnitGroupIds;
    }

    public Map<String, Integer> getOrgUnitCountMap()
    {
        return orgUnitCountMap;
    }

    public void setOrgUnitCountMap( Map<String, Integer> orgUnitCountMap )
    {
        this.orgUnitCountMap = orgUnitCountMap;
    }

    public Map<String, Double> getItemValueMap()
    {
        return itemValueMap;
    }

    public void setItemValueMap( Map<String, Double> itemValueMap )
    {
        this.itemValueMap = itemValueMap;
    }

    public MapMap<Period, String, Double> getPeriodItemValueMap()
    {
        return periodItemValueMap;
    }

    public void setPeriodItemValueMap( MapMap<Period, String, Double> periodItemValueMap )
    {
        this.periodItemValueMap = periodItemValueMap;
    }

    public List<Period> getSamplePeriods()
    {
        return samplePeriods;
    }

    public Double getDays()
    {
        return days;
    }

    public void setDays( Double days )
    {
        this.days = days;
    }

    public int getItemsFound()
    {
        return itemsFound;
    }

    public void setItemsFound( int itemsFound )
    {
        this.itemsFound = itemsFound;
    }

    public int getItemValuesFound()
    {
        return itemValuesFound;
    }

    public void setItemValuesFound( int itemValuesFound )
    {
        this.itemValuesFound = itemValuesFound;
    }

    public MissingValueStrategy getMissingValueStrategy()
    {
        return missingValueStrategy;
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    /**
     * Builder for {@link CommonExpressionVisitor} instances.
     */
    public static class Builder
    {
        private CommonExpressionVisitor visitor;

        protected Builder()
        {
            this.visitor = new CommonExpressionVisitor();
        }

        public Builder withItemMap( Map<Integer, ExpressionItem> itemMap )
        {
            this.visitor.itemMap = itemMap;
            return this;
        }

        public Builder withItemMethod( ExpressionItemMethod itemMethod )
        {
            this.visitor.itemMethod = itemMethod;
            return this;
        }

        public Builder withDimensionService( DimensionService dimensionService )
        {
            this.visitor.dimensionService = dimensionService;
            return this;
        }

        public Builder withOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
        {
            this.visitor.organisationUnitGroupService = organisationUnitGroupService;
            return this;
        }

        public Builder withProgramIndicatorService( ProgramIndicatorService programIndicatorService )
        {
            this.visitor.programIndicatorService = programIndicatorService;
            return this;
        }

        public Builder withProgramStageService( ProgramStageService programStageService )
        {
            this.visitor.programStageService = programStageService;
            return this;
        }

        public Builder withDataElementService( DataElementService dataElementService )
        {
            this.visitor.dataElementService = dataElementService;
            return this;
        }

        public Builder withAttributeService( TrackedEntityAttributeService attributeService )
        {
            this.visitor.attributeService = attributeService;
            return this;
        }

        public Builder withRelationshipTypeService( RelationshipTypeService relationshipTypeService )
        {
            this.visitor.relationshipTypeService = relationshipTypeService;
            return this;
        }

        public Builder withStatementBuilder( StatementBuilder statementBuilder )
        {
            this.visitor.statementBuilder = statementBuilder;
            return this;
        }

        public Builder withI18n( I18n i18n )
        {
            this.visitor.i18n = i18n;
            return this;
        }

        public Builder withConstantMap( Map<String, Constant> constantMap )
        {
            this.visitor.constantMap = constantMap;
            return this;
        }

        public Builder withSamplePeriods( List<Period> samplePeriods )
        {
            this.visitor.samplePeriods = samplePeriods;
            return this;
        }

        public Builder withMissingValueStrategy( MissingValueStrategy missingValueStrategy )
        {
            this.visitor.missingValueStrategy = missingValueStrategy;
            return this;
        }

        public CommonExpressionVisitor buildForExpressions()
        {
            Validate.notNull( this.visitor.dimensionService, "Missing required property 'dimensionService'" );
            Validate.notNull( this.visitor.organisationUnitGroupService,
                "Missing required property 'organisationUnitGroupService'" );
            Validate.notNull( this.visitor.missingValueStrategy, "Missing required property 'missingValueStrategy'" );

            return validateCommonProperties();
        }

        public CommonExpressionVisitor buildForProgramIndicatorExpressions()
        {
            Validate.notNull( this.visitor.programIndicatorService,
                "Missing required property 'programIndicatorService'" );
            Validate.notNull( this.visitor.programStageService, "Missing required property 'programStageService'" );
            Validate.notNull( this.visitor.dataElementService, "Missing required property 'dataElementService'" );
            Validate.notNull( this.visitor.attributeService, "Missing required property 'attributeService'" );
            Validate.notNull( this.visitor.relationshipTypeService,
                "Missing required property 'relationshipTypeService'" );
            Validate.notNull( this.visitor.statementBuilder, "Missing required property 'statementBuilder'" );
            Validate.notNull( this.visitor.i18n, "Missing required property 'i18n'" );

            return validateCommonProperties();
        }

        private CommonExpressionVisitor validateCommonProperties()
        {
            Validate.notNull( this.visitor.constantMap, "Missing required property 'constantMap'" );
            Validate.notNull( this.visitor.itemMap, "Missing required property 'itemMap'" );
            Validate.notNull( this.visitor.itemMethod, "Missing required property 'itemMethod'" );
            Validate
                .notNull( this.visitor.samplePeriods, "Missing required property 'samplePeriods'" );

            return visitor;
        }
    }
}
