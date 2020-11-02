package com.mass3d.dxf2.csv;


import static com.mass3d.util.DateUtils.getMediumDate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.mass3d.analytics.AggregationType;
import com.mass3d.category.Category;
import com.mass3d.category.CategoryCombo;
import com.mass3d.category.CategoryOption;
import com.mass3d.category.CategoryOptionGroup;
import com.mass3d.category.CategoryService;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.CodeGenerator;
import com.mass3d.common.DataDimensionType;
import com.mass3d.common.ListMap;
import com.mass3d.common.ValueType;
import com.mass3d.commons.collection.CachingMap;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementDomain;
import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.dataelement.DataElementGroupService;
import com.mass3d.dxf2.metadata.Metadata;
import com.mass3d.expression.Expression;
import com.mass3d.expression.MissingValueStrategy;
import com.mass3d.expression.Operator;
import com.mass3d.indicator.Indicator;
import com.mass3d.indicator.IndicatorGroup;
import com.mass3d.indicator.IndicatorGroupService;
import com.mass3d.option.Option;
import com.mass3d.option.OptionGroup;
import com.mass3d.option.OptionGroupSet;
import com.mass3d.option.OptionService;
import com.mass3d.option.OptionSet;
import com.mass3d.organisationunit.FeatureType;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.organisationunit.OrganisationUnitGroup;
import com.mass3d.organisationunit.OrganisationUnitGroupService;
import com.mass3d.period.MonthlyPeriodType;
import com.mass3d.period.PeriodType;
import com.mass3d.system.util.CsvUtils;
import com.mass3d.validation.Importance;
import com.mass3d.validation.ValidationRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csvreader.CsvReader;


@Service ( "com.mass3d.dxf2.csv.CsvImportService" )
public class DefaultCsvImportService
    implements CsvImportService
{
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrganisationUnitGroupService organisationUnitGroupService;

    @Autowired
    private DataElementGroupService dataElementGroupService;

    @Autowired
    private IndicatorGroupService indicatorGroupService;

    @Autowired
    private OptionService optionService;

    private static final String JSON_GEOM_TEMPL = "{\"type\":\"%s\", \"coordinates\":%s}";

    // -------------------------------------------------------------------------
    // CsvImportService implementation
    // -------------------------------------------------------------------------

    //TODO Add unit tests

    @Override
    public Metadata fromCsv( InputStream input, CsvImportOptions options )
        throws IOException
    {
        CsvReader reader = CsvUtils.getReader( input );
        reader.setSafetySwitch( false ); // Disabled due to large geometry values for org units

        if ( options.isFirstRowIsHeader() )
        {
            reader.readRecord(); // Ignore first row
        }

        Metadata metadata = new Metadata();

        switch ( options.getImportClass() )
        {
            case DATA_ELEMENT:
                metadata.setDataElements( dataElementsFromCsv( reader ) );
                break;
            case DATA_ELEMENT_GROUP:
                metadata.setDataElementGroups( dataElementGroupsFromCsv( reader ) );
                break;
            case DATA_ELEMENT_GROUP_MEMBERSHIP:
                metadata.setDataElementGroups( dataElementGroupMembersFromCsv( reader ) );
                break;
            case INDICATOR_GROUP_MEMBERSHIP:
                metadata.setIndicatorGroups( indicatorGroupMembersFromCsv( reader ) );
                break;
            case CATEGORY_OPTION:
                metadata.setCategoryOptions( categoryOptionsFromCsv( reader ) );
                break;
            case CATEGORY:
                metadata.setCategories( categoriesFromCsv( reader ) );
                break;
            case CATEGORY_COMBO:
                metadata.setCategoryCombos( categoryCombosFromCsv( reader ) );
                break;
            case CATEGORY_OPTION_GROUP:
                metadata.setCategoryOptionGroups( categoryOptionGroupsFromCsv( reader ) );
                break;
            case ORGANISATION_UNIT:
                metadata.setOrganisationUnits( orgUnitsFromCsv( reader ) );
                break;
            case ORGANISATION_UNIT_GROUP:
                metadata.setOrganisationUnitGroups( orgUnitGroupsFromCsv( reader ) );
                break;
            case ORGANISATION_UNIT_GROUP_MEMBERSHIP:
                metadata.setOrganisationUnitGroups( orgUnitGroupMembersFromCsv( reader ) );
                break;
            case TODOTASK:
//                metadata.setTodoTasks( todoTasksFromCsv( reader ) );
                break;
            case ACTIVITY:
//                metadata.setActivities( activitiesFromCsv( reader ) );
                break;
            case PROJECT:
//                metadata.setProjects( projectsFromCsv( reader ) );
                break;
            case VALIDATION_RULE:
                metadata.setValidationRules( validationRulesFromCsv( reader ) );
                break;
            case OPTION_SET:
                setOptionSetsFromCsv( reader, metadata );
                break;
            case OPTION_GROUP:
                setOptionGroupsFromCsv( reader, metadata );
                break;
            case OPTION_GROUP_SET:
                metadata.setOptionGroupSets( setOptionGroupSetFromCsv( reader ) );
                break;
            case OPTION_GROUP_SET_MEMBERSHIP:
                metadata.setOptionGroupSets( optionGroupSetMembersFromCsv( reader ) );
                break;
            default:
                break;
        }

        return metadata;
    }

    // -------------------------------------------------------------------------
    // Private methods
    // -------------------------------------------------------------------------

    private List<DataElement> dataElementsFromCsv( CsvReader reader )
        throws IOException
    {
        CategoryCombo categoryCombo = categoryService.getDefaultCategoryCombo();

        List<DataElement> list = new ArrayList<>();

        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                DataElement object = new DataElement();
                setIdentifiableObject( object, values );
                object.setShortName( getSafe( values, 3, object.getName(), 50 ) );
                object.setDescription( getSafe( values, 4, null, null ) );
                object.setFormName( getSafe( values, 5, null, 230 ) );

                String domainType = getSafe( values, 6, DataElementDomain.AGGREGATE.getValue(), 16 );
                object.setDomainType( DataElementDomain.fromValue( domainType ) );
                object.setValueType( ValueType.valueOf( getSafe( values, 7, ValueType.INTEGER.toString(), 50 ) ) );

                object.setAggregationType( AggregationType.valueOf( getSafe( values, 8, AggregationType.SUM.toString(), 50 ) ) );
                String categoryComboUid = getSafe( values, 9, null, 11 );
                object.setUrl( getSafe( values, 10, null, 255 ) );
                object.setZeroIsSignificant( Boolean.valueOf( getSafe( values, 11, "false", null ) ) );
                String optionSetUid = getSafe( values, 12, null, 11 );
                String commentOptionSetUid = getSafe( values, 13, null, 11 );
                object.setAutoFields();

                CategoryCombo cc = new CategoryCombo();
                cc.setUid( categoryComboUid );
                cc.setAutoFields();

                if ( categoryComboUid == null )
                {
                    cc.setUid( categoryCombo.getUid() );
                }

                object.setCategoryCombo( cc );

                if ( optionSetUid != null )
                {
                    OptionSet optionSet = new OptionSet();
                    optionSet.setUid( optionSetUid );
                    optionSet.setAutoFields();
                    object.setOptionSet( optionSet );
                }

                if ( commentOptionSetUid != null )
                {
                    OptionSet optionSet = new OptionSet();
                    optionSet.setUid( commentOptionSetUid );
                    optionSet.setAutoFields();
                    object.setCommentOptionSet( optionSet );
                }

                list.add( object );
            }
        }

        return list;
    }

    private List<DataElementGroup> dataElementGroupsFromCsv( CsvReader reader )
        throws IOException
    {
        List<DataElementGroup> list = new ArrayList<>();

        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                DataElementGroup object = new DataElementGroup();
                setIdentifiableObject( object, values );
                object.setShortName( getSafe( values, 3, object.getName(), 50 ) );
                object.setAutoFields();
                list.add( object );
            }
        }

        return list;
    }

    private List<DataElementGroup> dataElementGroupMembersFromCsv( CsvReader reader )
        throws IOException
    {
        CachingMap<String, DataElementGroup> uidMap = new CachingMap<>();

        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                String groupUid = values[0];
                String memberUid = values[1];

                DataElementGroup persistedGroup = dataElementGroupService.getDataElementGroupByUid( groupUid );

                if ( persistedGroup != null )
                {
                    DataElementGroup group = uidMap.get( groupUid, () -> {
                        DataElementGroup nonPersistedGroup = new DataElementGroup();
                        nonPersistedGroup.setUid( persistedGroup.getUid() );
                        nonPersistedGroup.setName( persistedGroup.getName() );
                        return nonPersistedGroup;
                    } );

                    DataElement member = new DataElement();
                    member.setUid( memberUid );
                    group.addDataElement( member );
                }
            }
        }

        return new ArrayList<>( uidMap.values() );
    }

    private List<IndicatorGroup> indicatorGroupMembersFromCsv( CsvReader reader )
        throws IOException
    {
        CachingMap<String, IndicatorGroup> uidMap = new CachingMap<>();

        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                String groupUid = values[0];
                String memberUid = values[1];

                IndicatorGroup persistedGroup = indicatorGroupService.getIndicatorGroupByUid( groupUid );

                if ( persistedGroup != null )
                {

                    IndicatorGroup group = uidMap.get( groupUid, () -> {
                        IndicatorGroup nonPersistedGroup = new IndicatorGroup();
                        nonPersistedGroup.setUid( persistedGroup.getUid() );
                        nonPersistedGroup.setName( persistedGroup.getName() );
                        return nonPersistedGroup;
                    } );

                    Indicator member = new Indicator();
                    member.setUid( memberUid );
                    group.addIndicator( member );
                }
            }
        }
        return new ArrayList<>( uidMap.values() );
    }

    private List<CategoryOption> categoryOptionsFromCsv( CsvReader reader )
        throws IOException
    {
        List<CategoryOption> list = new ArrayList<>();

        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                CategoryOption object = new CategoryOption();
                setIdentifiableObject( object, values );
                object.setShortName( getSafe( values, 3, object.getName(), 50 ) );
                list.add( object );
            }
        }

        return list;
    }

    private List<Category> categoriesFromCsv( CsvReader reader )
        throws IOException
    {
        List<Category> list = new ArrayList<>();

        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                Category object = new Category();
                setIdentifiableObject( object, values );
                object.setDescription( getSafe( values, 3, null, 255 ) );
                object.setDataDimensionType( DataDimensionType.valueOf( getSafe( values, 4, DataDimensionType.DISAGGREGATION.toString(), 40 ) ) );
                object.setDataDimension( Boolean
                    .valueOf( getSafe( values, 5, Boolean.FALSE.toString(), 40 ) ) );
                list.add( object );
            }
        }

        return list;
    }

    private List<CategoryCombo> categoryCombosFromCsv( CsvReader reader )
        throws IOException
    {
        List<CategoryCombo> list = new ArrayList<>();

        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                CategoryCombo object = new CategoryCombo();
                setIdentifiableObject( object, values );
                object.setDataDimensionType( DataDimensionType
                    .valueOf( getSafe( values, 3, DataDimensionType.DISAGGREGATION.toString(), 40 ) ) );
                object.setSkipTotal( Boolean
                    .valueOf( getSafe( values, 4, Boolean.FALSE.toString(), 40 ) ) );
                list.add( object );
            }
        }

        return list;
    }

    private List<CategoryOptionGroup> categoryOptionGroupsFromCsv( CsvReader reader )
        throws IOException
    {
        List<CategoryOptionGroup> list = new ArrayList<>();

        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                CategoryOptionGroup object = new CategoryOptionGroup();
                setIdentifiableObject( object, values );
                object.setShortName( getSafe( values, 3, object.getName(), 50 ) );
                list.add( object );
            }
        }

        return list;
    }

    private List<ValidationRule> validationRulesFromCsv( CsvReader reader )
        throws IOException
    {
        List<ValidationRule> list = new ArrayList<>();

        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                Expression leftSide = new Expression();
                Expression rightSide = new Expression();

                ValidationRule object = new ValidationRule();
                setIdentifiableObject( object, values );
                object.setDescription( getSafe( values, 3, null, 255 ) );
                object.setInstruction( getSafe( values, 4, null, 255 ) );
                object.setImportance( Importance
                    .valueOf( getSafe( values, 5, Importance.MEDIUM.toString(), 255 ) ) );
                // Index 6 was rule type which has been removed from the data model
                object.setOperator( Operator
                    .safeValueOf( getSafe( values, 7, Operator.equal_to.toString(), 255 ) ) );
                object.setPeriodType( PeriodType
                    .getByNameIgnoreCase( getSafe( values, 8, MonthlyPeriodType.NAME, 255 ) ) );

                leftSide.setExpression( getSafe( values, 9, null, 255 ) );
                leftSide.setDescription( getSafe( values, 10, null, 255 ) );
                leftSide.setMissingValueStrategy( MissingValueStrategy
                    .safeValueOf( getSafe( values, 11, MissingValueStrategy.NEVER_SKIP.toString(), 50 ) ) );

                rightSide.setExpression( getSafe( values, 12, null, 255 ) );
                rightSide.setDescription( getSafe( values, 13, null, 255 ) );
                rightSide.setMissingValueStrategy( MissingValueStrategy
                    .safeValueOf( getSafe( values, 14, MissingValueStrategy.NEVER_SKIP.toString(), 50 ) ) );

                object.setLeftSide( leftSide );
                object.setRightSide( rightSide );
                object.setAutoFields();

                list.add( object );
            }
        }

        return list;
    }

    private void setGeometry( OrganisationUnit ou, FeatureType featureType, String coordinates )
        throws IOException
    {
        if ( !( featureType == FeatureType.NONE)  && StringUtils.isNotBlank( coordinates ) )
        {
            ou.setGeometryAsJson( String.format( JSON_GEOM_TEMPL, featureType.value(), coordinates ) );
        }
    }

    private List<OrganisationUnit> orgUnitsFromCsv( CsvReader reader )
        throws IOException
    {
        List<OrganisationUnit> list = new ArrayList<>();

        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                OrganisationUnit object = new OrganisationUnit();
                setIdentifiableObject( object, values );
                String parentUid = getSafe( values, 3, null, 230 ); // Could be UID, code, name
                object.setShortName( getSafe( values, 4, object.getName(), 50 ) );
                object.setDescription( getSafe( values, 5, null, null ) );
                object.setOpeningDate( getMediumDate( getSafe( values, 6, "1970-01-01", null ) ) );
                object.setClosedDate( getMediumDate( getSafe( values, 7, null, null ) ) );
                object.setComment( getSafe( values, 8, null, null ) );
                setGeometry( object, FeatureType.valueOf( getSafe( values, 9, "NONE", 50 ) ),
                    getSafe( values, 10, null, null ) );
                object.setUrl( getSafe( values, 11, null, 255 ) );
                object.setContactPerson( getSafe( values, 12, null, 255 ) );
                object.setAddress( getSafe( values, 13, null, 255 ) );
                object.setEmail( getSafe( values, 14, null, 150 ) );
                object.setPhoneNumber( getSafe( values, 15, null, 150 ) );
                object.setAutoFields();

                if ( parentUid != null )
                {
                    OrganisationUnit parent = new OrganisationUnit();
                    parent.setUid( parentUid );
                    object.setParent( parent );
                }

                list.add( object );
            }
        }

        return list;
    }

    private List<OrganisationUnitGroup> orgUnitGroupsFromCsv( CsvReader reader )
        throws IOException
    {
        List<OrganisationUnitGroup> list = new ArrayList<>();

        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                OrganisationUnitGroup object = new OrganisationUnitGroup();
                setIdentifiableObject( object, values );
                object.setAutoFields();
                object.setShortName( getSafe( values, 3, object.getName(), 50 ) );
                list.add( object );
            }
        }

        return list;
    }

    private List<OrganisationUnitGroup> orgUnitGroupMembersFromCsv( CsvReader reader )
        throws IOException
    {
        CachingMap<String, OrganisationUnitGroup> uidMap = new CachingMap<>();

        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                String groupUid = values[0];
                String memberUid = values[1];

                OrganisationUnitGroup persistedGroup = organisationUnitGroupService.getOrganisationUnitGroup( groupUid );

                if ( persistedGroup != null )
                {

                    OrganisationUnitGroup group = uidMap.get( groupUid, () -> {
                        OrganisationUnitGroup nonPersistedGroup = new OrganisationUnitGroup();

                        nonPersistedGroup.setUid( persistedGroup.getUid() );
                        nonPersistedGroup.setName( persistedGroup.getName() );

                        return nonPersistedGroup;
                    } );

                    OrganisationUnit member = new OrganisationUnit();
                    member.setUid( memberUid );
                    group.addOrganisationUnit( member );
                }
            }
        }

        return new ArrayList<>( uidMap.values() );
    }

    /**
     * Option set format:
     * <p>
     * <ul>
     * <li>option set name</li>
     * <li>option set uid</li>
     * <li>option set code</li>
     * <li>option name</li>
     * <li>option uid</li>
     * <li>option code</li>
     * </ul>
     */
    private void setOptionSetsFromCsv( CsvReader reader, Metadata metadata )
        throws IOException
    {
        ListMap<String, Option> nameOptionMap = new ListMap<>();
        Map<String, OptionSet> nameOptionSetMap = new HashMap<>();

        // Read option sets and options and put in maps

        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                OptionSet optionSet = new OptionSet();
                setIdentifiableObject( optionSet, values );
                optionSet.setAutoFields();
                optionSet.setValueType( ValueType.TEXT );

                Option option = new Option();
                option.setName( getSafe( values, 3, null, 230 ) );
                option.setUid( getSafe( values, 4, CodeGenerator.generateUid(), 11 ) );
                option.setCode( getSafe( values, 5, null, 50 ) );
                option.setAutoFields();

                if ( optionSet.getName() == null || option.getCode() == null )
                {
                    continue;
                }

                nameOptionSetMap.put( optionSet.getName(), optionSet );

                nameOptionMap.putValue( optionSet.getName(), option );

                metadata.getOptions().add( option );
            }
        }

        // Read option sets from map and set in meta data

        for ( String optionSetName : nameOptionSetMap.keySet() )
        {
            OptionSet optionSet = nameOptionSetMap.get( optionSetName );

            List<Option> options = new ArrayList<>( nameOptionMap.get( optionSetName ) );

            optionSet.setOptions( options );

            metadata.getOptionSets().add( optionSet );
        }
    }

    /**
     * Option group format:
     * <p>
     * <ul>
     * <li>option group name</li>
     * <li>option group uid</li>
     * <li>option group code</li>
     * <li>option group short name</li>
     * <li>option set uid</li>
     * <li>option uid</li>
     * </ul>
     * @param reader
     * @param metadata
     * @throws IOException
     */
    private void setOptionGroupsFromCsv( CsvReader reader, Metadata metadata )
        throws IOException
    {
        ListMap<String, Option> nameOptionMap = new ListMap<>();
        Map<String, OptionGroup> nameOptionGroupMap = new HashMap<>();
        CachingMap<String, OptionSet> mapOptionSet = new CachingMap<>();

        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                OptionGroup optionGroup = new OptionGroup();
                setIdentifiableObject( optionGroup, values );
                optionGroup.setShortName( getSafe( values, 3, null, 50 ) );
                optionGroup.setAutoFields();

                if ( optionGroup.getName() == null || optionGroup.getShortName() == null )
                {
                    continue;
                }

                OptionSet optionSet = new OptionSet();
                optionSet.setUid( getSafe( values, 4, null, 11  ) );

                if ( optionSet.getUid() == null )
                {
                    continue;
                }

                OptionSet persistedOptionSet =  optionSet.getUid() != null ?
                    mapOptionSet.get( optionSet.getUid(), () -> {
                        OptionSet persistedOS = optionService.getOptionSet( optionSet.getUid() );
                        persistedOS.getOptions();
                        return persistedOS;
                    } ) :
                    mapOptionSet.get( optionSet.getCode(), () -> {
                        OptionSet persistedOS = optionService.getOptionSetByCode( optionSet.getUid() );
                        persistedOS.getOptions();
                        return persistedOS;
                    }
                 );

                if ( persistedOptionSet == null )
                {
                    continue;
                }

                optionGroup.setOptionSet( optionSet );

                Option option = new Option();
                option.setUid( getSafe( values, 5, null, 11 ) );
                option.setCode( getSafe( values, 6, null, 50 ) );

                if ( option.getCode() == null && option.getUid() == null )
                {
                    continue;
                }

                Optional<Option> isOptionExisted = persistedOptionSet.getOptions().stream().filter( persistedOption -> {
                    if ( option.getUid() != null )
                    {
                        return persistedOption.getUid().equals( option.getUid() );
                    }
                    else
                    {
                        return persistedOption.getCode().equals( option.getCode() );
                    }
                } ).findFirst();

                if ( !isOptionExisted.isPresent() )
                {
                    continue;
                }

                nameOptionGroupMap.put( optionGroup.getName(), optionGroup );

                nameOptionMap.putValue( optionGroup.getName(), isOptionExisted.get() );
            }
        }

        // Read option groups from map and set in meta data

        for ( String optionGroupName : nameOptionGroupMap.keySet() )
        {
            OptionGroup optionGroup = nameOptionGroupMap.get( optionGroupName );

            Set<Option> options = new HashSet<>( nameOptionMap.get( optionGroupName ) );

            optionGroup.setMembers( options );

            metadata.getOptionGroups().add( optionGroup );
        }
    }

    /**
     * Option group set format:
     * <p>
     * <ul>
     * <li>option group set name</li>
     * <li>option group set uid</li>
     * <li>option group set code</li>
     * <li>option group set description</li>
     * <li>data dimension</li>
     * <li>option set uid</li>
     * <li>option set code</li>
     * </ul>
     * @param reader
     * @return
     * @throws IOException
     */
    private List<OptionGroupSet> setOptionGroupSetFromCsv( CsvReader reader )
        throws IOException
    {
        List<OptionGroupSet> optionGroupSets = new ArrayList<>();
        CachingMap<String, OptionSet> mapOptionSet = new CachingMap<>();

        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                OptionGroupSet optionGroupSet = new OptionGroupSet();
                setIdentifiableObject( optionGroupSet, values );
                optionGroupSet.setDescription( getSafe( values, 4, null, null ) );
                optionGroupSet.setDataDimension( Boolean
                    .valueOf( getSafe( values, 3,  Boolean.FALSE.toString(), 40 ) ) ); // boolean

                OptionSet optionSet = new OptionSet();
                optionSet.setUid( getSafe( values, 5, null, 11  ) );
                optionSet.setCode( getSafe( values, 6, null, 50  ) );

                if ( optionSet.getUid() == null && optionSet.getCode() == null )
                {
                    continue;
                }

                OptionSet persistedOptionSet =  optionSet.getUid() != null ?
                    mapOptionSet.get( optionSet.getUid(), () -> optionService.getOptionSet( optionSet.getUid() ) ) :
                    mapOptionSet.get( optionSet.getCode(), () -> optionService.getOptionSetByCode( optionSet.getCode() )
                    );

                if ( persistedOptionSet == null )
                {
                    continue;
                }

                optionGroupSet.setOptionSet( optionSet );

                optionGroupSets.add( optionGroupSet );
            }
        }
        return optionGroupSets;
    }

    /**
     * Option Group Set Members format
     * <ul>
     *     <li>option group set uid</li>
     *     <li>option group uid</li>
     * </ul>
     * @param reader
     * @return
     * @throws IOException
     */
    public List<OptionGroupSet> optionGroupSetMembersFromCsv( CsvReader reader )
        throws IOException
    {
        CachingMap<String, OptionGroupSet> uidMap = new CachingMap<>();
        CachingMap<String, OptionGroupSet> persistedGroupSetMap = new CachingMap<>();


        while ( reader.readRecord() )
        {
            String[] values = reader.getValues();

            if ( values != null && values.length > 0 )
            {
                String groupSetUid = getSafe( values, 0, null, 11 );
                String groupUid = getSafe( values, 1, null, 11 );
                if ( groupSetUid == null || groupUid == null )
                {
                    continue;
                }

                OptionGroupSet persistedGroupSet = persistedGroupSetMap.get( groupSetUid, () -> optionService.getOptionGroupSet( groupSetUid ) );

                if ( persistedGroupSet != null )
                {
                    OptionGroupSet optionSetGroup = uidMap.get( groupSetUid, () -> {
                        OptionGroupSet nonPersistedGroup = new OptionGroupSet();
                        nonPersistedGroup.setUid( persistedGroupSet.getUid() );
                        nonPersistedGroup.setName( persistedGroupSet.getName() );
                        return nonPersistedGroup;
                    } );

                    OptionGroup member = new OptionGroup();
                    member.setUid( groupUid );
                    optionSetGroup.addOptionGroup( member );
                }
            }
        }

        return new ArrayList<>( uidMap.values() );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Sets the name, uid and code properties on the given object.
     *
     * @param object the object to set identifiable properties.
     * @param values the array of property values.
     */
    private static void setIdentifiableObject( BaseIdentifiableObject object, String[] values )
    {
        object.setName( getSafe( values, 0, null, 230 ) );
        object.setUid( getSafe( values, 1, CodeGenerator.generateUid(), 11 ) );
        object.setCode( getSafe( values, 2, null, 50 ) );
    }

    /**
     * Returns a string from the given array avoiding exceptions.
     *
     * @param values       the string array.
     * @param index        the array index of the string to get, zero-based.
     * @param defaultValue the default value in case index is out of bounds.
     * @param maxChars     the max number of characters to return for the string.
     */
    private static String getSafe( String[] values, int index, String defaultValue, Integer maxChars )
    {
        String string;

        if ( values == null || index < 0 || index >= values.length )
        {
            string = defaultValue;
        }
        else
        {
            string = values[index];
        }

        string = StringUtils.defaultIfBlank( string, defaultValue );

        if ( string != null )
        {
            return maxChars != null ? StringUtils.substring( string, 0, maxChars ) : string;
        }

        return null;
    }
}
