package com.mass3d.dataelement;

import static com.mass3d.dataset.DataSet.NO_EXPIRY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import com.mass3d.audit.AuditAttribute;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DimensionItemType;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.common.ValueType;
import com.mass3d.common.ValueTypedDimensionalItemObject;
import com.mass3d.dataset.DataSet;
import com.mass3d.dataset.DataSetElement;
import com.mass3d.option.OptionSet;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodType;
import com.mass3d.period.YearlyPeriodType;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.annotation.Property;
import com.mass3d.schema.annotation.PropertyRange;
import com.mass3d.translation.TranslationProperty;
import org.joda.time.DateTime;

/**
 * A DataElement is a definition (meta-information about) of the entities that
 * are captured in the system. An example from public health care is a
 * DataElement representing the number BCG doses; A DataElement with "BCG dose"
 * as name, with type DataElement.TYPE_INT.
 * <p>
 * DataElement acts as a DimensionSet in the dynamic dimensional model, and as a
 * DimensionOption in the static DataElement dimension.
 *
 */
@JacksonXmlRootElement( localName = "dataElement", namespace = DxfNamespaces.DXF_2_0 )
public class DataElement extends BaseDimensionalItemObject
    implements MetadataObject, ValueTypedDimensionalItemObject
{
    public static final String[] I18N_PROPERTIES = { TranslationProperty.NAME.getName(), TranslationProperty.SHORT_NAME.getName(),
        TranslationProperty.DESCRIPTION.getName(), TranslationProperty.FORM_NAME.getName() };

    /**
     * Data element value type (int, boolean, etc)
     */
    private ValueType valueType;

    /**
     * The name to appear in forms.
     */
    private String formName;

    /**
     * The i18n variant of the display name. Should not be persisted.
     */
    protected transient String displayFormName;

    /**
     * The domain of this DataElement; e.g. DataElementDomainType.AGGREGATE or
     * DataElementDomainType.TRACKER.
     */
    private DataElementDomain domainType;

    /**
     * URL for lookup of additional information on the web.
     */
    private String url;

    /**
     * The data element groups which this
     */
    private Set<DataElementGroup> groups = new HashSet<>();

    /**
     * The data sets which this data element is a member of.
     */
    @JsonIgnore
    private Set<DataSetElement> dataSetElements = new HashSet<>();

    /**
     * The lower organisation unit levels for aggregation.
     */
    private List<Integer> aggregationLevels = new ArrayList<>();

    /**
     * Indicates whether to store zero data values.
     */
    private boolean zeroIsSignificant;

    /**
     * The option set for data values linked to this data element, can be null.
     */
    @AuditAttribute
    private OptionSet optionSet;

    /**
     * The option set for comments linked to this data element, can be null.
     */
    private OptionSet commentOptionSet;

    /**
     * Field mask represent how the value should be formatted during input. This string will
     * be validated as a TextPatternSegment of type TEXT.
     */
    private String fieldMask;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public DataElement()
    {
    }

    public DataElement( String name )
    {
        this();
        this.name = name;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addDataElementGroup( DataElementGroup group )
    {
        groups.add( group );
        group.getMembers().add( this );
    }

    public void removeDataElementGroup( DataElementGroup group )
    {
        groups.remove( group );
        group.getMembers().remove( this );
    }

    public void updateDataElementGroups( Set<DataElementGroup> updates )
    {
        for ( DataElementGroup group : new HashSet<>( groups ) )
        {
            if ( !updates.contains( group ) )
            {
                removeDataElementGroup( group );
            }
        }

        updates.forEach( this::addDataElementGroup );
    }

    public boolean removeDataSetElement( DataSetElement element )
    {
        dataSetElements.remove( element );
        return element.getDataSet().getDataSetElements().remove( element );
    }

    /**
     * Indicates whether the value type of this data element is numeric.
     */
    public boolean isNumericType()
    {
        return getValueType().isNumeric();
    }

    /**
     * Indicates whether the value type of this data element is a file (externally stored resource)
     */
    public boolean isFileType()
    {
        return getValueType().isFile();
    }

    /**
     * Returns the data set of this data element. If this data element has
     * multiple data sets, the data set with the highest collection frequency is
     * returned.
     */
    public DataSet getDataSet()
    {
        List<DataSet> list = new ArrayList<>( getDataSets() );
//        list.sort( DataSetFrequencyComparator.INSTANCE );
        return !list.isEmpty() ? list.get( 0 ) : null;
    }

    /**
     * Returns the data set of this data element. If this data element has
     * multiple data sets, the data set with approval enabled, then the highest
     * collection frequency, is returned.
     */
    public DataSet getApprovalDataSet()
    {
        List<DataSet> list = new ArrayList<>( getDataSets() );
//        list.sort( DataSetApprovalFrequencyComparator.INSTANCE );
        return !list.isEmpty() ? list.get( 0 ) : null;
    }

    /**
     * Note that this method returns an immutable set and can not be used to
     * modify the model. Returns an immutable set of data sets associated with
     * this data element.
     */
    public Set<DataSet> getDataSets()
    {
        return ImmutableSet.copyOf( dataSetElements.stream().map( DataSetElement::getDataSet ).filter(
            Objects::nonNull ).collect( Collectors.toSet() ) );
    }

    /**
     * Returns the PeriodType of the DataElement, based on the PeriodType of the
     * DataSet which the DataElement is associated with. If this data element has
     * multiple data sets, the data set with the highest collection frequency is
     * returned.
     */
    public PeriodType getPeriodType()
    {
        DataSet dataSet = getDataSet();

        return dataSet != null ? dataSet.getPeriodType() : null;
    }

    /**
     * Returns the PeriodTypes of the DataElement, based on the PeriodType of the
     * DataSets which the DataElement is associated with.
     */
    public Set<PeriodType> getPeriodTypes()
    {
        return getDataSets().stream().map( DataSet::getPeriodType ).collect( Collectors.toSet() );
    }

    /**
     * Returns the frequency order for the PeriodType of this DataElement. If no
     * PeriodType exists, 0 is returned.
     */
    public int getFrequencyOrder()
    {
        PeriodType periodType = getPeriodType();

        return periodType != null ? periodType.getFrequencyOrder() : YearlyPeriodType.FREQUENCY_ORDER;
    }

    /**
     * Tests whether a PeriodType can be defined for the DataElement, which
     * requires that the DataElement is registered for DataSets with the same
     * PeriodType.
     */
    public boolean periodTypeIsValid()
    {
        PeriodType periodType = null;

        for ( DataSet dataSet : getDataSets() )
        {
            if ( periodType != null && !periodType.equals( dataSet.getPeriodType() ) )
            {
                return false;
            }

            periodType = dataSet.getPeriodType();
        }

        return true;
    }

    /**
     * Tests whether more than one aggregation level exists for the DataElement.
     */
    public boolean hasAggregationLevels()
    {
        return aggregationLevels != null && aggregationLevels.size() > 0;
    }

    /**
     * Returns the form name, or the name if it does not exist.
     */
    public String getFormNameFallback()
    {
        return formName != null && !formName.isEmpty() ? getFormName() : getDisplayName();
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDisplayFormName()
    {
        displayFormName = null; // getTranslation( TranslationProperty.FORM_NAME, displayFormName );
        return displayFormName != null ? displayFormName : getFormNameFallback();
    }

    public void setDisplayFormName( String displayFormName )
    {
        this.displayFormName = displayFormName;
    }

    /**
     * Returns the maximum number of expiry days from the data sets of this data
     * element. Returns {@link DataSet#NO_EXPIRY} if any data set has no expiry.
     */
    public int getExpiryDays()
    {
        int expiryDays = Integer.MIN_VALUE;

        for ( DataSet dataSet : getDataSets() )
        {
            if ( dataSet.getExpiryDays() == NO_EXPIRY )
            {
                return NO_EXPIRY;
            }

            if ( dataSet.getExpiryDays() > expiryDays )
            {
                expiryDays = dataSet.getExpiryDays();
            }
        }

        return expiryDays == Integer.MIN_VALUE ? NO_EXPIRY : expiryDays;
    }

    /**
     * Indicates whether the given period is considered expired for the end date
     * of the given date based on the expiry days of the data sets associated
     * with this data element.
     *
     * @param period the period.
     * @param now    the date used as basis.
     * @return true or false.
     */
    public boolean isExpired( Period period, Date now )
    {
        int expiryDays = getExpiryDays();

        return expiryDays != DataSet.NO_EXPIRY && new DateTime( period.getEndDate() ).plusDays( expiryDays ).isBefore( new DateTime( now ) );
    }

    /**
     * Indicates whether this data element has a description.
     *
     * @return true if this data element has a description.
     */
    public boolean hasDescription()
    {
        return description != null && !description.trim().isEmpty();
    }

    /**
     * Indicates whether this data element has a URL.
     *
     * @return true if this data element has a URL.
     */
    public boolean hasUrl()
    {
        return url != null && !url.trim().isEmpty();
    }

    /**
     * Indicates whether this data element has an option set.
     *
     * @return true if this data element has an option set.
     */
    @Override
    public boolean hasOptionSet()
    {
        return optionSet != null;
    }

    // -------------------------------------------------------------------------
    // DimensionalItemObject
    // -------------------------------------------------------------------------

    //TODO can also be dimension

    @Override
    public DimensionItemType getDimensionItemType()
    {
        return DimensionItemType.DATA_ELEMENT;
    }

    // -------------------------------------------------------------------------
    // Helper getters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isOptionSetValue()
    {
        return optionSet != null;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @Override
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ValueType getValueType()
    {
        //TODO return optionSet != null ? optionSet.getValueType() : valueType;
        return valueType;
    }

    public void setValueType( ValueType valueType )
    {
        this.valueType = valueType;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @PropertyRange( min = 2 )
    public String getFormName()
    {
        return formName;
    }

    public void setFormName( String formName )
    {
        this.formName = formName;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public DataElementDomain getDomainType()
    {
        return domainType;
    }

    public void setDomainType( DataElementDomain domainType )
    {
        this.domainType = domainType;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @Property( PropertyType.URL )
    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    @JsonProperty( "dataElementGroups" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "dataElementGroups", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "dataElementGroup", namespace = DxfNamespaces.DXF_2_0 )
    public Set<DataElementGroup> getGroups()
    {
        return groups;
    }

    public void setGroups( Set<DataElementGroup> groups )
    {
        this.groups = groups;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "dataSetElements", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "dataSetElements", namespace = DxfNamespaces.DXF_2_0 )
    public Set<DataSetElement> getDataSetElements()
    {
        return dataSetElements;
    }

    public void setDataSetElements( Set<DataSetElement> dataSetElements )
    {
        this.dataSetElements = dataSetElements;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public List<Integer> getAggregationLevels()
    {
        return aggregationLevels;
    }

    public void setAggregationLevels( List<Integer> aggregationLevels )
    {
        this.aggregationLevels = aggregationLevels;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isZeroIsSignificant()
    {
        return zeroIsSignificant;
    }

    public void setZeroIsSignificant( boolean zeroIsSignificant )
    {
        this.zeroIsSignificant = zeroIsSignificant;
    }

    @Override
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public OptionSet getOptionSet()
    {
        return optionSet;
    }

    public void setOptionSet( OptionSet optionSet )
    {
        this.optionSet = optionSet;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public OptionSet getCommentOptionSet()
    {
        return commentOptionSet;
    }

    public void setCommentOptionSet( OptionSet commentOptionSet )
    {
        this.commentOptionSet = commentOptionSet;
    }

    /**
     * Checks if the combination of period and date is allowed for any of the dataSets associated with the dataElement
     *
     * @param period period to check
     * @param date   date to check
     * @return true if no dataSets exists, or at least one dataSet has a valid DataInputPeriod for the period and date.
     */
    public boolean isDataInputAllowedForPeriodAndDate( Period period, Date date )
    {
        return getDataSets().isEmpty() || getDataSets().stream()
            .anyMatch( dataSet -> dataSet.isDataInputPeriodAndDateAllowed( period, date ) );
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getFieldMask()
    {
        return fieldMask;
    }

    public void setFieldMask( String fieldMask )
    {
        this.fieldMask = fieldMask;
    }
}
