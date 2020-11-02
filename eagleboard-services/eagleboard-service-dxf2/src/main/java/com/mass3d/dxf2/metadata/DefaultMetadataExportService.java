package com.mass3d.dxf2.metadata;

import com.google.common.base.Enums;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mass3d.attribute.Attribute;
import com.mass3d.attribute.AttributeService;
import com.mass3d.category.Category;
import com.mass3d.category.CategoryCombo;
import com.mass3d.category.CategoryOption;
import com.mass3d.category.CategoryOptionCombo;
import com.mass3d.dataelement.DataElementOperand;
import com.mass3d.dataentryform.DataEntryForm;
import com.mass3d.dataset.Section;
import com.mass3d.programrule.ProgramRuleService;
import com.mass3d.programrule.ProgramRuleVariableService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.InterpretableObject;
import com.mass3d.common.SetMap;
import com.mass3d.commons.timer.SystemTimer;
import com.mass3d.commons.timer.Timer;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.dataset.DataSet;
import com.mass3d.dataset.DataSetElement;
import com.mass3d.document.Document;
import com.mass3d.dxf2.common.OrderParams;
import com.mass3d.fieldfilter.Defaults;
import com.mass3d.fieldfilter.FieldFilterParams;
import com.mass3d.fieldfilter.FieldFilterService;
import com.mass3d.indicator.Indicator;
import com.mass3d.indicator.IndicatorType;
import com.mass3d.interpretation.Interpretation;
import com.mass3d.node.NodeUtils;
import com.mass3d.node.config.InclusionStrategy;
import com.mass3d.node.types.CollectionNode;
import com.mass3d.node.types.ComplexNode;
import com.mass3d.node.types.RootNode;
import com.mass3d.node.types.SimpleNode;
import com.mass3d.option.Option;
import com.mass3d.option.OptionSet;
import com.mass3d.program.Program;
import com.mass3d.query.Query;
import com.mass3d.query.QueryService;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaService;
import com.mass3d.security.Authorities;
import com.mass3d.system.SystemInfo;
import com.mass3d.system.SystemService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service( "com.mass3d.dxf2.metadata.MetadataExportService" )
public class DefaultMetadataExportService implements MetadataExportService
{
    @Autowired
    private SchemaService schemaService;

    @Autowired
    private QueryService queryService;

    @Autowired
    private FieldFilterService fieldFilterService;

    @Autowired
    private CurrentUserService currentUserService;

//    @Autowired
//    private ProgramRuleService programRuleService;
//
//    @Autowired
//    private ProgramRuleVariableService programRuleVariableService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private AttributeService attributeService;

    @Override
    @SuppressWarnings( "unchecked" )
    public Map<Class<? extends IdentifiableObject>, List<? extends IdentifiableObject>> getMetadata( MetadataExportParams params )
    {
        Timer timer = new SystemTimer().start();
        Map<Class<? extends IdentifiableObject>, List<? extends IdentifiableObject>> metadata = new HashMap<>();

        if ( params.getUser() == null )
        {
            params.setUser( currentUserService.getCurrentUser() );
        }

        if ( params.getClasses().isEmpty() )
        {
            schemaService.getMetadataSchemas().stream().filter( Schema::isIdentifiableObject ).filter( s -> !s.isSecondaryMetadata() )
                .forEach( schema -> params.getClasses().add( (Class<? extends IdentifiableObject>) schema.getKlass() ) );
        }

        log.info( "(" + params.getUsername() + ") Export:Start" );

        for ( Class<? extends IdentifiableObject> klass : params.getClasses() )
        {
            Query query;

            if ( params.getQuery( klass ) != null )
            {
                query = params.getQuery( klass );
            }
            else
            {
                OrderParams orderParams = new OrderParams( Sets.newHashSet( params.getDefaultOrder() ) );
                query = queryService.getQueryFromUrl( klass, params.getDefaultFilter(), orderParams.getOrders( schemaService.getDynamicSchema( klass ) ) );
            }

            if ( query.getUser() == null )
            {
                query.setUser( params.getUser() );
            }

            query.setDefaultOrder();
            query.setDefaults( params.getDefaults() );

            List<? extends IdentifiableObject> objects = queryService.query( query );

            if ( !objects.isEmpty() )
            {
                log.info( "(" + params.getUsername() + ") Exported " + objects.size() + " objects of type " + klass.getSimpleName() );
                metadata.put( klass, objects );
            }
        }

        log.info( "(" + params.getUsername() + ") Export:Done took " + timer.toString() );

        return metadata;
    }

    @Override
    public RootNode getMetadataAsNode( MetadataExportParams params )
    {
        RootNode rootNode = NodeUtils.createMetadata();
        rootNode.getConfig().setInclusionStrategy( params.getInclusionStrategy() );

        SystemInfo systemInfo = systemService.getSystemInfo();

        ComplexNode system = rootNode.addChild( new ComplexNode( "system" ) );
        system.addChild( new SimpleNode( "id", systemInfo.getSystemId() ) );
        system.addChild( new SimpleNode( "rev", systemInfo.getRevision() ) );
        system.addChild( new SimpleNode( "version", systemInfo.getVersion() ) );
        system.addChild( new SimpleNode( "date", systemInfo.getServerDate() ) );

        Map<Class<? extends IdentifiableObject>, List<? extends IdentifiableObject>> metadata = getMetadata( params );

        for ( Class<? extends IdentifiableObject> klass : metadata.keySet() )
        {
            FieldFilterParams fieldFilterParams = new FieldFilterParams( metadata.get( klass ), params.getFields( klass ), params.getDefaults(), params.getSkipSharing() );
            fieldFilterParams.setUser( params.getUser() );

            CollectionNode collectionNode = fieldFilterService.toCollectionNode( klass, fieldFilterParams );

            if ( !collectionNode.getChildren().isEmpty() )
            {
                rootNode.addChild( collectionNode );
            }
        }

        return rootNode;
    }

    @Override
    public void validate( MetadataExportParams params )
    {
        if ( params.getUser() == null )
        {
            params.setUser( currentUserService.getCurrentUser() );
        }

        User user = params.getUser();

        if ( params.getClasses().isEmpty()
            && !(user == null || user.isSuper() || user.isAuthorized( Authorities.F_METADATA_EXPORT )) )
        {
            throw new MetadataExportException( "Unfiltered access to metadata export requires super user or 'F_METADATA_EXPORT' authority." );
        }

        if ( params.getClasses().contains( User.class )
            && !(user == null || user.isSuper() || user.isAuthorized( Authorities.F_USER_VIEW ) || user.isAuthorized( Authorities.F_METADATA_EXPORT )) )
        {
            throw new MetadataExportException( "Exporting user metadata requires the 'F_USER_VIEW' authority." );
        }
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public MetadataExportParams getParamsFromMap( Map<String, List<String>> parameters )
    {
        MetadataExportParams params = new MetadataExportParams();
        Map<Class<? extends IdentifiableObject>, Map<String, List<String>>> map = new HashMap<>();

        params.setDefaults( getEnumWithDefault( Defaults.class, parameters, "defaults", Defaults.INCLUDE ) );
        params.setInclusionStrategy( getEnumWithDefault( InclusionStrategy.Include.class, parameters, "inclusionStrategy",
            InclusionStrategy.Include.NON_NULL ) );

        if ( parameters.containsKey( "fields" ) )
        {
            params.setDefaultFields( parameters.get( "fields" ) );
            parameters.remove( "fields" );
        }

        if ( parameters.containsKey( "filter" ) )
        {
            params.setDefaultFilter( parameters.get( "filter" ) );
            parameters.remove( "filter" );
        }

        if ( parameters.containsKey( "order" ) )
        {
            params.setDefaultOrder( parameters.get( "order" ) );
            parameters.remove( "order" );
        }

        if ( parameters.containsKey( "skipSharing" ) )
        {
            params.setSkipSharing( Boolean.parseBoolean( parameters.get( "skipSharing" ).get( 0 ) ) );
            parameters.remove( "skipSharing" );
        }

        for ( String parameterKey : parameters.keySet() )
        {
            String[] parameter = parameterKey.split( ":" );
            Schema schema = schemaService.getSchemaByPluralName( parameter[0] );

            if ( schema == null || !schema.isIdentifiableObject() )
            {
                continue;
            }

            Class<? extends IdentifiableObject> klass = (Class<? extends IdentifiableObject>) schema.getKlass();

            // class is enabled if value = true, or fields/filter/order is present
            if ( isSelectedClass( parameters.get( parameterKey ) ) || (parameter.length > 1 && ("fields".equalsIgnoreCase( parameter[1] )
                || "filter".equalsIgnoreCase( parameter[1] ) || "order".equalsIgnoreCase( parameter[1] ))) )
            {
                if ( !map.containsKey( klass ) ) map.put( klass, new HashMap<>() );
            }
            else
            {
                continue;
            }

            if ( parameter.length > 1 )
            {
                if ( "fields".equalsIgnoreCase( parameter[1] ) )
                {
                    if ( !map.get( klass ).containsKey( "fields" ) ) map.get( klass ).put( "fields", new ArrayList<>() );
                    map.get( klass ).get( "fields" ).addAll( parameters.get( parameterKey ) );
                }

                if ( "filter".equalsIgnoreCase( parameter[1] ) )
                {
                    if ( !map.get( klass ).containsKey( "filter" ) ) map.get( klass ).put( "filter", new ArrayList<>() );
                    map.get( klass ).get( "filter" ).addAll( parameters.get( parameterKey ) );
                }

                if ( "order".equalsIgnoreCase( parameter[1] ) )
                {
                    if ( !map.get( klass ).containsKey( "order" ) ) map.get( klass ).put( "order", new ArrayList<>() );
                    map.get( klass ).get( "order" ).addAll( parameters.get( parameterKey ) );
                }
            }
        }

        map.keySet().forEach( params::addClass );

        for ( Class<? extends IdentifiableObject> klass : map.keySet() )
        {
            Map<String, List<String>> classMap = map.get( klass );
            Schema schema = schemaService.getDynamicSchema( klass );

            if ( classMap.containsKey( "fields" ) ) params.addFields( klass, classMap.get( "fields" ) );

            if ( classMap.containsKey( "filter" ) && classMap.containsKey( "order" ) )
            {
                OrderParams orderParams = new OrderParams( Sets.newHashSet( classMap.get( "order" ) ) );
                Query query = queryService.getQueryFromUrl( klass, classMap.get( "filter" ), orderParams.getOrders( schema ) );
                query.setDefaultOrder();
                params.addQuery( query );
            }
            else if ( classMap.containsKey( "filter" ) )
            {
                Query query = queryService.getQueryFromUrl( klass, classMap.get( "filter" ), new ArrayList<>() );
                query.setDefaultOrder();
                params.addQuery( query );
            }
            else if ( classMap.containsKey( "order" ) )
            {
                OrderParams orderParams = new OrderParams();
                orderParams.setOrder( Sets.newHashSet( classMap.get( "order" ) ) );

                Query query = queryService.getQueryFromUrl( klass, new ArrayList<>(), orderParams.getOrders( schema ) );
                query.setDefaultOrder();
                params.addQuery( query );
            }
        }

        return params;
    }

    @Override
    public SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> getMetadataWithDependencies( IdentifiableObject object )
    {
        SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata = new SetMap<>();

        if ( OptionSet.class.isInstance( object ) ) return handleOptionSet( metadata, (OptionSet) object );
        if ( DataSet.class.isInstance( object ) ) return handleDataSet( metadata, (DataSet) object );
//        if ( Program.class.isInstance( object ) ) return handleProgram( metadata, (Program) object );
        if ( CategoryCombo.class.isInstance( object ) ) return handleCategoryCombo( metadata, (CategoryCombo) object );
//        if ( Dashboard.class.isInstance( object ) ) return handleDashboard( metadata, (Dashboard) object );
        if ( DataElementGroup.class.isInstance( object ) ) return handleDataElementGroup( metadata, (DataElementGroup) object );
        return metadata;
    }

    @Override
    public RootNode getMetadataWithDependenciesAsNode( IdentifiableObject object, @Nonnull MetadataExportParams params )
    {
        RootNode rootNode = NodeUtils.createMetadata();
        rootNode.addChild( new SimpleNode( "date", new Date(), true ) );

        SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata = getMetadataWithDependencies( object );

        for ( Class<? extends IdentifiableObject> klass : metadata.keySet() )
        {
            FieldFilterParams fieldFilterParams = new FieldFilterParams( Lists.newArrayList( metadata.get( klass ) ),
                Lists.newArrayList( ":owner" ) );
            fieldFilterParams.setSkipSharing( params.getSkipSharing() );
            rootNode.addChild( fieldFilterService.toCollectionNode( klass, fieldFilterParams ) );
        }

        return rootNode;
    }

    //-----------------------------------------------------------------------------------
    // Utility Methods
    //-----------------------------------------------------------------------------------

    private boolean isSelectedClass( @Nonnull List<String> values )
    {
        if ( values.stream().anyMatch( "false"::equalsIgnoreCase ) )
        {
            return false;
        }

        return values.stream().anyMatch( "true"::equalsIgnoreCase );
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleDataSet( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, DataSet dataSet )
    {
        metadata.putValue( DataSet.class, dataSet );
        handleAttributes( metadata, dataSet );

        dataSet.getDataSetElements().forEach( dataSetElement -> handleDataSetElement( metadata, dataSetElement ) );
        dataSet.getSections().forEach( section -> handleSection( metadata, section ) );
        dataSet.getIndicators().forEach( indicator -> handleIndicator( metadata, indicator ) );

        handleDataEntryForm( metadata, dataSet.getDataEntryForm() );
//        handleLegendSet( metadata, dataSet.getLegendSets() );
        handleCategoryCombo( metadata, dataSet.getCategoryCombo() );
//
        dataSet.getCompulsoryDataElementOperands().forEach( dataElementOperand -> handleDataElementOperand( metadata, dataElementOperand ) );
        dataSet.getDataElementOptionCombos().forEach( dataElementOptionCombo -> handleCategoryOptionCombo( metadata, dataElementOptionCombo ) );

        return metadata;
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleDataElementOperand( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, DataElementOperand dataElementOperand )
    {
        if ( dataElementOperand == null ) return metadata;
        handleCategoryOptionCombo( metadata, dataElementOperand.getCategoryOptionCombo() );
//        handleLegendSet( metadata, dataElementOperand.getLegendSets() );
        handleDataElement( metadata, dataElementOperand.getDataElement() );

        return metadata;
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleCategoryOptionCombo( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, CategoryOptionCombo categoryOptionCombo )
    {
        if ( categoryOptionCombo == null ) return metadata;
        metadata.putValue( CategoryOptionCombo.class, categoryOptionCombo );
        handleAttributes( metadata, categoryOptionCombo );

        categoryOptionCombo.getCategoryOptions().forEach( categoryOption -> handleCategoryOption( metadata, categoryOption ) );

        return metadata;
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleCategoryCombo( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, CategoryCombo categoryCombo )
    {
        if ( categoryCombo == null ) return metadata;
        metadata.putValue( CategoryCombo.class, categoryCombo );
        handleAttributes( metadata, categoryCombo );

        categoryCombo.getCategories().forEach( category -> handleCategory( metadata, category ) );
        categoryCombo.getOptionCombos().forEach( optionCombo -> handleCategoryOptionCombo( metadata, optionCombo ) );

        return metadata;
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleCategory( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, Category category )
    {
        if ( category == null ) return metadata;
        metadata.putValue( Category.class, category );
        handleAttributes( metadata, category );

        category.getCategoryOptions().forEach( categoryOption -> handleCategoryOption( metadata, categoryOption ) );

        return metadata;
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleCategoryOption( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, CategoryOption categoryOption )
    {
        if ( categoryOption == null ) return metadata;
        metadata.putValue( CategoryOption.class, categoryOption );
        handleAttributes( metadata, categoryOption );

        return metadata;
    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleLegendSet( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, List<LegendSet> legendSets )
//    {
//        if ( legendSets == null ) return metadata;
//
//        for ( LegendSet legendSet : legendSets )
//        {
//            metadata.putValue( LegendSet.class, legendSet );
//            handleAttributes( metadata, legendSet );
//            legendSet.getLegends().forEach( legend -> handleLegend( metadata, legend ) );
//        }
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleLegend( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, Legend legend )
//    {
//        if ( legend == null ) return metadata;
//        metadata.putValue( Legend.class, legend );
//        handleAttributes( metadata, legend );
//
//        return metadata;
//    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleDataEntryForm( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, DataEntryForm dataEntryForm )
    {
        if ( dataEntryForm == null ) return metadata;
        metadata.putValue( DataEntryForm.class, dataEntryForm );
        handleAttributes( metadata, dataEntryForm );

        return metadata;
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleDataSetElement( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, DataSetElement dataSetElement )
    {
        if ( dataSetElement == null ) return metadata;

        handleDataElement( metadata, dataSetElement.getDataElement() );
        handleCategoryCombo( metadata, dataSetElement.getCategoryCombo() );

        return metadata;
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleDataElement( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, DataElement dataElement )
    {
        if ( dataElement == null ) return metadata;
        metadata.putValue( DataElement.class, dataElement );
        handleAttributes( metadata, dataElement );

        handleCategoryCombo( metadata, dataElement.getCategoryCombo() );
        handleOptionSet( metadata, dataElement.getOptionSet() );
        handleOptionSet( metadata, dataElement.getCommentOptionSet() );

        return metadata;
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleOptionSet( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, OptionSet optionSet )
    {
        if ( optionSet == null ) return metadata;
        metadata.putValue( OptionSet.class, optionSet );
        handleAttributes( metadata, optionSet );

        optionSet.getOptions().forEach( o -> handleOption( metadata, o ) );

        return metadata;
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleOption( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, Option option )
    {
        if ( option == null ) return metadata;
        metadata.putValue( Option.class, option );
        handleAttributes( metadata, option );

        return metadata;
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleSection( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, Section section )
    {
        if ( section == null ) return metadata;
        metadata.putValue( Section.class, section );
        handleAttributes( metadata, section );

        section.getGreyedFields().forEach( dataElementOperand -> handleDataElementOperand( metadata, dataElementOperand ) );
        section.getIndicators().forEach( indicator -> handleIndicator( metadata, indicator ) );
        section.getDataElements().forEach( dataElement -> handleDataElement( metadata, dataElement ) );

        return metadata;
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleIndicator( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, Indicator indicator )
    {
        if ( indicator == null ) return metadata;
        metadata.putValue( Indicator.class, indicator );
        handleAttributes( metadata, indicator );

        handleIndicatorType( metadata, indicator.getIndicatorType() );

        return metadata;
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleIndicatorType( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, IndicatorType indicatorType )
    {
        if ( indicatorType == null ) return metadata;
        metadata.putValue( IndicatorType.class, indicatorType );
        handleAttributes( metadata, indicatorType );

        return metadata;
    }

//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleProgram( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, Program program )
//    {
//        if ( program == null ) return metadata;
//        metadata.putValue( Program.class, program );
//        handleAttributes( metadata, program );
//
//        handleCategoryCombo( metadata, program.getCategoryCombo() );
//        handleDataEntryForm( metadata, program.getDataEntryForm() );
//        handleTrackedEntityType( metadata, program.getTrackedEntityType() );
//
//        program.getNotificationTemplates().forEach( template -> handleNotificationTemplate( metadata, template ) );
//        program.getProgramStages().forEach( programStage -> handleProgramStage( metadata, programStage ) );
//        program.getProgramAttributes().forEach( programTrackedEntityAttribute -> handleProgramTrackedEntityAttribute( metadata, programTrackedEntityAttribute ) );
//        program.getProgramIndicators().forEach( programIndicator -> handleProgramIndicator( metadata, programIndicator ) );
//
//        List<ProgramRule> programRules = programRuleService.getProgramRule( program );
//        List<ProgramRuleVariable> programRuleVariables = programRuleVariableService.getProgramRuleVariable( program );
//
//        programRules.forEach( programRule -> handleProgramRule( metadata, programRule ) );
//        programRuleVariables.forEach( programRuleVariable -> handleProgramRuleVariable( metadata, programRuleVariable ) );
//
//        return metadata;
//    }

//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleNotificationTemplate( SetMap<Class<? extends
//        IdentifiableObject>, IdentifiableObject> metadata, ProgramNotificationTemplate template )
//    {
//        if ( template == null )
//        {
//            return metadata;
//        }
//
//        metadata.putValue( ProgramNotificationTemplate.class, template );
//
//        handleTrackedEntityAttribute( metadata, template.getRecipientProgramAttribute() );
//
//        handleDataElement( metadata, template.getRecipientDataElement() );
//
//        return metadata;
//    }

//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleProgramRuleVariable( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, ProgramRuleVariable programRuleVariable )
//    {
//        if ( programRuleVariable == null ) return metadata;
//        metadata.putValue( ProgramRuleVariable.class, programRuleVariable );
//        handleAttributes( metadata, programRuleVariable );
//
//        handleTrackedEntityAttribute( metadata, programRuleVariable.getAttribute() );
//        handleDataElement( metadata, programRuleVariable.getDataElement() );
//        handleProgramStage( metadata, programRuleVariable.getProgramStage() );
//
//        return metadata;
//    }

//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleTrackedEntityAttribute( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, TrackedEntityAttribute trackedEntityAttribute )
//    {
//        if ( trackedEntityAttribute == null ) return metadata;
//        metadata.putValue( TrackedEntityAttribute.class, trackedEntityAttribute );
//        handleAttributes( metadata, trackedEntityAttribute );
//
//        handleOptionSet( metadata, trackedEntityAttribute.getOptionSet() );
//
//        return metadata;
//    }

//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleProgramRule( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, ProgramRule programRule )
//    {
//        if ( programRule == null ) return metadata;
//        metadata.putValue( ProgramRule.class, programRule );
//        handleAttributes( metadata, programRule );
//
//        programRule.getProgramRuleActions().forEach( programRuleAction -> handleProgramRuleAction( metadata, programRuleAction ) );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleProgramRuleAction( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, ProgramRuleAction programRuleAction )
//    {
//        if ( programRuleAction == null ) return metadata;
//        metadata.putValue( ProgramRuleAction.class, programRuleAction );
//        handleAttributes( metadata, programRuleAction );
//
//        handleDataElement( metadata, programRuleAction.getDataElement() );
//        handleTrackedEntityAttribute( metadata, programRuleAction.getAttribute() );
//        handleProgramIndicator( metadata, programRuleAction.getProgramIndicator() );
//        handleProgramStageSection( metadata, programRuleAction.getProgramStageSection() );
//        handleProgramStage( metadata, programRuleAction.getProgramStage() );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleProgramTrackedEntityAttribute( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, ProgramTrackedEntityAttribute programTrackedEntityAttribute )
//    {
//        if ( programTrackedEntityAttribute == null ) return metadata;
//        metadata.putValue( ProgramTrackedEntityAttribute.class, programTrackedEntityAttribute );
//        handleAttributes( metadata, programTrackedEntityAttribute );
//
//        handleTrackedEntityAttribute( metadata, programTrackedEntityAttribute.getAttribute() );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleProgramStage( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, ProgramStage programStage )
//    {
//        if ( programStage == null ) return metadata;
//        metadata.putValue( ProgramStage.class, programStage );
//        handleAttributes( metadata, programStage );
//
//        programStage.getNotificationTemplates().forEach( template -> handleNotificationTemplate( metadata, template ) );
//        programStage.getProgramStageDataElements().forEach( programStageDataElement -> handleProgramStageDataElement( metadata, programStageDataElement ) );
//        programStage.getProgramStageSections().forEach( programStageSection -> handleProgramStageSection( metadata, programStageSection ) );
//
//        handleDataEntryForm( metadata, programStage.getDataEntryForm() );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleProgramStageSection( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, ProgramStageSection programStageSection )
//    {
//        if ( programStageSection == null ) return metadata;
//        metadata.putValue( ProgramStageSection.class, programStageSection );
//        handleAttributes( metadata, programStageSection );
//
//        programStageSection.getProgramIndicators().forEach( programIndicator -> handleProgramIndicator( metadata, programIndicator ) );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleProgramIndicator( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, ProgramIndicator programIndicator )
//    {
//        if ( programIndicator == null ) return metadata;
//        metadata.putValue( ProgramIndicator.class, programIndicator );
//        handleAttributes( metadata, programIndicator );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleProgramStageDataElement( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, ProgramStageDataElement programStageDataElement )
//    {
//        if ( programStageDataElement == null ) return metadata;
//        metadata.putValue( ProgramStageDataElement.class, programStageDataElement );
//
//        handleAttributes( metadata, programStageDataElement );
//        handleDataElement( metadata, programStageDataElement.getDataElement() );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleTrackedEntityType( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, TrackedEntityType trackedEntityType )
//    {
//        if ( trackedEntityType == null ) return metadata;
//        metadata.putValue( TrackedEntityType.class, trackedEntityType );
//        handleAttributes( metadata, trackedEntityType );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleChart( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, Chart chart )
//    {
//        if ( chart == null ) return metadata;
//        metadata.putValue( Chart.class, chart );
//        handleAttributes( metadata, chart );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleEventChart( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, EventChart eventChart )
//    {
//        if ( eventChart == null ) return metadata;
//        metadata.putValue( EventChart.class, eventChart );
//        handleAttributes( metadata, eventChart );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleEventReport( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, EventReport eventReport )
//    {
//        if ( eventReport == null ) return metadata;
//        metadata.putValue( EventReport.class, eventReport );
//        handleAttributes( metadata, eventReport );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleMapView( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, MapView mapView )
//    {
//        if ( mapView == null ) return metadata;
//        metadata.putValue( MapView.class, mapView );
//        handleAttributes( metadata, mapView );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleMap( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, com.mass3d.mapping.Map map )
//    {
//        if ( map == null ) return metadata;
//        metadata.putValue( com.mass3d.mapping.Map.class, map );
//        handleAttributes( metadata, map );
//
//        map.getMapViews().forEach( mapView -> handleMapView( metadata, mapView ) );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleReportTable( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, ReportTable reportTable )
//    {
//        if ( reportTable == null ) return metadata;
//        metadata.putValue( ReportTable.class, reportTable );
//        handleAttributes( metadata, reportTable );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleVisualization( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, Visualization visualization )
//    {
//        if ( visualization == null ) return metadata;
//        metadata.putValue( Visualization.class, visualization );
//        handleAttributes( metadata, visualization );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleReport( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, Report report )
//    {
//        if ( report == null ) return metadata;
//        metadata.putValue( Report.class, report );
//        handleAttributes( metadata, report );
//
//        return metadata;
//    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleInterpretation( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, Interpretation interpretation )
    {
        if ( interpretation == null ) return metadata;
        metadata.putValue( Interpretation.class, interpretation );
        handleAttributes( metadata, interpretation );

        return metadata;
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleEmbbedItem( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, InterpretableObject embbededItem )
    {
        if ( embbededItem == null ) return metadata;

        if ( embbededItem.getInterpretations() != null )
        {
            embbededItem.getInterpretations().forEach( interpretation -> handleInterpretation( metadata, interpretation ) );
        }

        return metadata;
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleDocument( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, Document document )
    {
        if ( document == null ) return metadata;
        metadata.putValue( Document.class, document );
        handleAttributes( metadata, document );

        return metadata;
    }

//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleDashboardItem( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, DashboardItem dashboardItem )
//    {
//        if ( dashboardItem == null ) return metadata;
//        handleAttributes( metadata, dashboardItem );
//
//        handleVisualization( metadata, dashboardItem.getVisualization() );
//        handleChart( metadata, dashboardItem.getChart() );
//        handleEventChart( metadata, dashboardItem.getEventChart() );
//        handleEventReport( metadata, dashboardItem.getEventReport() );
//        handleMap( metadata, dashboardItem.getMap() );
//        handleReportTable( metadata, dashboardItem.getReportTable() );
//        handleEmbbedItem( metadata, dashboardItem.getEmbeddedItem() );
//
//        dashboardItem.getReports().forEach( report -> handleReport( metadata, report ) );
//        dashboardItem.getResources().forEach( document -> handleDocument( metadata, document ) );
//
//        return metadata;
//    }
//
//    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleDashboard( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, Dashboard dashboard )
//    {
//        metadata.putValue( Dashboard.class, dashboard );
//        handleAttributes( metadata, dashboard );
//        dashboard.getItems().forEach( dashboardItem -> handleDashboardItem( metadata, dashboardItem ) );
//
//        return metadata;
//    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleDataElementGroup( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, DataElementGroup dataElementGroup )
    {
        metadata.putValue( DataElementGroup.class, dataElementGroup );
        handleAttributes( metadata, dataElementGroup );

        dataElementGroup.getMembers().forEach( dataElement -> handleDataElement( metadata, dataElement ) );
//        handleLegendSet( metadata, dataElementGroup.getLegendSets() );

        return metadata;
    }

    private SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> handleAttributes( SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata, IdentifiableObject identifiableObject )
    {
        if ( identifiableObject == null ) return metadata;
        identifiableObject.getAttributeValues().forEach( av -> metadata.putValue( Attribute.class, attributeService.getAttribute( av.getAttribute().getUid() ) ) );

        return metadata;
    }

    private <T extends Enum<T>> T getEnumWithDefault( Class<T> enumKlass, Map<String, List<String>> parameters, String key, T defaultValue )
    {
        if ( parameters == null || parameters.get( key ) == null || parameters.get( key ).isEmpty() )
        {
            return defaultValue;
        }

        String value = String.valueOf( parameters.get( key ).get( 0 ) );

        return Enums.getIfPresent( enumKlass, value ).or( defaultValue );
    }
}
