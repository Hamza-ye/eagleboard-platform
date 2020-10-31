package com.mass3d;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import com.mass3d.activity.Activity;
import com.mass3d.analytics.AggregationType;
import com.mass3d.category.Category;
import com.mass3d.category.CategoryCombo;
import com.mass3d.category.CategoryOption;
import com.mass3d.category.CategoryOptionCombo;
import com.mass3d.category.CategoryOptionGroup;
import com.mass3d.category.CategoryOptionGroupSet;
import com.mass3d.category.CategoryService;
import com.mass3d.common.DataDimensionType;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.ValueType;
import com.mass3d.constant.Constant;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementDomain;
import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.dataelement.DataElementGroupSet;
import com.mass3d.dataset.DataSet;
import com.mass3d.datavalue.DataValue;
import com.mass3d.external.location.LocationManager;
import com.mass3d.fileresource.ExternalFileResource;
import com.mass3d.fileresource.FileResource;
import com.mass3d.fileresource.FileResourceDomain;
import com.mass3d.indicator.Indicator;
import com.mass3d.indicator.IndicatorGroup;
import com.mass3d.indicator.IndicatorGroupSet;
import com.mass3d.indicator.IndicatorType;
import com.mass3d.option.Option;
import com.mass3d.option.OptionSet;
import com.mass3d.period.MonthlyPeriodType;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodType;
import com.mass3d.render.RenderService;
import com.mass3d.todotask.TodoTask;
import com.mass3d.user.User;
import com.mass3d.user.UserAccess;
import com.mass3d.user.UserAuthorityGroup;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserGroup;
import com.mass3d.user.UserService;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;
import org.springframework.util.MimeTypeUtils;
import org.xml.sax.InputSource;

@ActiveProfiles( profiles = { "test" } )
public abstract class DhisConvenienceTest
{
    protected static final Log log = LogFactory.getLog( DhisConvenienceTest.class );

    protected static final String BASE_UID = "abcdefghij";
    protected static final String BASE_IN_UID = "inabcdefgh";
    protected static final String BASE_DE_UID = "deabcdefgh";
    protected static final String BASE_DS_UID = "dsabcdefgh";
    protected static final String BASE_OU_UID = "ouabcdefgh";
    protected static final String BASE_COC_UID = "cuabcdefgh";
    protected static final String BASE_USER_UID = "userabcdef";
    protected static final String BASE_USER_GROUP_UID = "ugabcdefgh";

    private static final String EXT_TEST_DIR = System.getProperty( "user.home" ) + File.separator + "dhis2_test_dir";

    private static Date date;

    protected static final double DELTA = 0.01;

    // -------------------------------------------------------------------------
    // Service references
    // -------------------------------------------------------------------------

//    @Autowired
    protected UserService userService;

//    @Autowired
    protected RenderService renderService;

    @Autowired( required = false )
    protected CategoryService internalCategoryService;

    protected static CategoryService categoryService;

    static
    {
        DateTime dateTime = new DateTime( 1970, 1, 1, 0, 0 );
        date = dateTime.toDate();
    }

    // -------------------------------------------------------------------------
    // Convenience methods
    // -------------------------------------------------------------------------

    /**
     * Creates a date.
     *
     * @param year  the year.
     * @param month the month.
     * @param day   the day of month.
     * @return a date.
     */
    public static Date getDate( int year, int month, int day )
    {
        DateTime dateTime = new DateTime( year, month, day, 0, 0 );
        return dateTime.toDate();
    }

    /**
     * Creates a date.
     *
     * @param s a string representation of a date
     * @return a date.
     */
    public static Date getDate( String s )
    {
        DateTime dateTime = new DateTime( s );
        return dateTime.toDate();
    }

    /**
     * Creates a date.
     *
     * @param day the day of the year.
     * @return a date.
     */
    public Date getDay( int day )
    {
        DateTime dataTime = DateTime.now();
        dataTime = dataTime.withTimeAtStartOfDay();
        dataTime = dataTime.withDayOfYear( day );

        return dataTime.toDate();
    }

    /**
     * Compares two collections for equality. This method does not check for the
     * implementation type of the collection in contrast to the native equals
     * method. This is useful for black-box testing where one will not know the
     * implementation type of the returned collection for a method.
     *
     * @param actual    the actual collection to check.
     * @param reference the reference objects to check against.
     * @return true if the collections are equal, false otherwise.
     */
    public static boolean equals( Collection<?> actual, Object... reference )
    {
        final Collection<Object> collection = new HashSet<>();

        Collections.addAll( collection, reference );

        if ( actual == collection )
        {
            return true;
        }

        if ( actual == null )
        {
            return false;
        }

        if ( actual.size() != collection.size() )
        {
            log.warn( "Actual collection has different size compared to reference collection: " + actual.size() + " / "
                + collection.size() );
            return false;
        }

        for ( Object object : actual )
        {
            if ( !collection.contains( object ) )
            {
                log.warn( "Object in actual collection not part of reference collection: " + object );
                return false;
            }
        }

        for ( Object object : collection )
        {
            if ( !actual.contains( object ) )
            {
                log.warn( "Object in reference collection not part of actual collection: " + object );
                return false;
            }
        }

        return true;
    }

    public static String message( Object expected )
    {
        return "Expected was: " + ((expected != null) ? "[" + expected.toString() + "]" : "[null]");
    }

    public static String message( Object expected, Object actual )
    {
        return message( expected ) + " Actual was: " + ((actual != null) ? "[" + actual.toString() + "]" : "[null]");
    }

    // -------------------------------------------------------------------------
    // Dependency injection methods
    // -------------------------------------------------------------------------

    /**
     * Sets a dependency on the target service. This method can be used to set
     * mock implementations of dependencies on services for testing purposes.
     * The advantage of using this method over setting the services directly is
     * that the test can still be executed against the interface type of the
     * service; making the test unaware of the implementation and thus
     * re-usable. A weakness is that the field name of the dependency must be
     * assumed.
     *
     * @param targetService the target service.
     * @param fieldName     the name of the dependency field in the target service.
     * @param dependency    the dependency.
     */
    protected void setDependency( Object targetService, String fieldName, Object dependency )
    {
        Class<?> clazz = dependency.getClass().getInterfaces()[0];

        setDependency( targetService, fieldName, dependency, clazz );
    }

    /**
     * Sets a dependency on the target service. This method can be used to set
     * mock implementations of dependencies on services for testing purposes.
     * The advantage of using this method over setting the services directly is
     * that the test can still be executed against the interface type of the
     * service; making the test unaware of the implementation and thus
     * re-usable. A weakness is that the field name of the dependency must be
     * assumed.
     *
     * @param targetService the target service.
     * @param fieldName     the name of the dependency field in the target service.
     * @param dependency    the dependency.
     * @param clazz         the interface type of the dependency.
     */
    protected void setDependency( Object targetService, String fieldName, Object dependency, Class<?> clazz )
    {
        try
        {
            targetService = getRealObject( targetService );

            String setMethodName = "set" + fieldName.substring( 0, 1 ).toUpperCase()
                + fieldName.substring( 1, fieldName.length() );

            Class<?>[] argumentClass = new Class<?>[]{ clazz };

            Method method = targetService.getClass().getMethod( setMethodName, argumentClass );

            method.invoke( targetService, dependency );
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to set dependency '" + fieldName + "' on service: " + getStackTrace( ex ), ex );
        }
    }

    /**
     * If the given class is advised by Spring AOP it will return the target
     * class, i.e. the advised class. If not the given class is returned
     * unchanged.
     *
     * @param object the object.
     */
    @SuppressWarnings( "unchecked" )
    private <T> T getRealObject( T object )
        throws Exception
    {
        if ( AopUtils.isAopProxy( object ) )
        {
            return (T) ((Advised) object).getTargetSource().getTarget();
        }

        return object;
    }

    // -------------------------------------------------------------------------
    // Create object methods
    // -------------------------------------------------------------------------

    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    public static DataElement createDataElement( char uniqueCharacter )
    {
        return createDataElement( uniqueCharacter, null );
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param categoryCombo   The category combo.
     */
    public static DataElement createDataElement( char uniqueCharacter, CategoryCombo categoryCombo )
    {
        DataElement dataElement = new DataElement();
        dataElement.setAutoFields();

        dataElement.setUid( BASE_DE_UID + uniqueCharacter );
        dataElement.setName( "DataElement" + uniqueCharacter );
        dataElement.setShortName( "DataElementShort" + uniqueCharacter );
        dataElement.setCode( "DataElementCode" + uniqueCharacter );
        dataElement.setDescription( "DataElementDescription" + uniqueCharacter );
        dataElement.setValueType( ValueType.INTEGER );
        dataElement.setDomainType( DataElementDomain.AGGREGATE );
        dataElement.setAggregationType( AggregationType.SUM );
        dataElement.setZeroIsSignificant( false );

        if ( categoryCombo != null )
        {
            dataElement.setCategoryCombo( categoryCombo );
        }
        else if ( categoryService != null )
        {
            dataElement.setCategoryCombo( categoryService.getDefaultCategoryCombo() );
        }

        return dataElement;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param valueType       The value type.
     * @param aggregationType The aggregation type.
     */
    public static DataElement createDataElement( char uniqueCharacter, ValueType valueType,
        AggregationType aggregationType )
    {
        DataElement dataElement = createDataElement( uniqueCharacter );
        dataElement.setValueType( valueType );
        dataElement.setAggregationType( aggregationType );

        return dataElement;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param valueType       The value type.
     * @param aggregationType The aggregation type.
     * @param domainType      The domain type.
     */
    public static DataElement createDataElement( char uniqueCharacter, ValueType valueType,
        AggregationType aggregationType, DataElementDomain domainType )
    {
        DataElement dataElement = createDataElement( uniqueCharacter );
        dataElement.setValueType( valueType );
        dataElement.setAggregationType( aggregationType );
        dataElement.setDomainType( domainType );

        return dataElement;
    }

    /**
     * @param categoryComboUniqueIdentifier A unique character to identify the
     *                                      category option combo.
     * @param categories                    the categories category options.
     * @return CategoryOptionCombo
     */
    public static CategoryCombo createCategoryCombo( char categoryComboUniqueIdentifier, Category... categories )
    {
        CategoryCombo categoryCombo = new CategoryCombo( "CategoryCombo" + categoryComboUniqueIdentifier,
            DataDimensionType.DISAGGREGATION );
        categoryCombo.setAutoFields();

        for ( Category category : categories )
        {
            categoryCombo.getCategories().add( category );
        }

        return categoryCombo;
    }

    /**
     * @param categoryComboUniqueIdentifier   A unique character to identify the
     *                                        category combo.
     * @param categoryOptionUniqueIdentifiers Unique characters to identify the
     *                                        category options.
     * @return CategoryOptionCombo
     */
    public static CategoryOptionCombo createCategoryOptionCombo( char categoryComboUniqueIdentifier,
        char... categoryOptionUniqueIdentifiers )
    {
        CategoryOptionCombo categoryOptionCombo = new CategoryOptionCombo();
        categoryOptionCombo.setAutoFields();

        categoryOptionCombo.setCategoryCombo( new CategoryCombo( "CategoryCombo"
            + categoryComboUniqueIdentifier, DataDimensionType.DISAGGREGATION ) );

        for ( char identifier : categoryOptionUniqueIdentifiers )
        {
            categoryOptionCombo.getCategoryOptions()
                .add( new CategoryOption( "CategoryOption" + identifier ) );
        }

        return categoryOptionCombo;
    }

    /**
     * @param categoryCombo   the category combo.
     * @param categoryOptions the category options.
     * @return CategoryOptionCombo
     */
    public static CategoryOptionCombo createCategoryOptionCombo( CategoryCombo categoryCombo,
        CategoryOption... categoryOptions )
    {
        CategoryOptionCombo categoryOptionCombo = new CategoryOptionCombo();
        categoryOptionCombo.setAutoFields();

        categoryOptionCombo.setCategoryCombo( categoryCombo );

        for ( CategoryOption categoryOption : categoryOptions )
        {
            categoryOptionCombo.getCategoryOptions().add( categoryOption );
            categoryOption.getCategoryOptionCombos().add( categoryOptionCombo );
        }

        return categoryOptionCombo;
    }

    public static CategoryOptionCombo createCategoryOptionCombo( char uniqueCharacter )
    {
        CategoryOptionCombo coc = new CategoryOptionCombo();
        coc.setAutoFields();

        coc.setUid( BASE_COC_UID + uniqueCharacter );
        coc.setName( "CategoryOptionCombo" + uniqueCharacter );
        coc.setName( "CategoryOptionComboCode" + uniqueCharacter );

        return coc;
    }

    /**
     * @param categoryUniqueIdentifier A unique character to identify the category.
     * @param categoryOptions          the category options.
     * @return Category
     */
    public static Category createCategory( char categoryUniqueIdentifier,
        CategoryOption... categoryOptions )
    {
        Category category = new Category( "Category" + categoryUniqueIdentifier, DataDimensionType.DISAGGREGATION );
        category.setAutoFields();

        for ( CategoryOption categoryOption : categoryOptions )
        {
            category.addCategoryOption( categoryOption );
        }

        return category;
    }

    public static CategoryOption createCategoryOption( char uniqueIdentifier )
    {
        CategoryOption categoryOption = new CategoryOption( "CategoryOption" + uniqueIdentifier );
        categoryOption.setAutoFields();

        return categoryOption;
    }

    /**
     * @param uniqueIdentifier A unique character to identify the category option
     *                         group.
     * @param categoryOptions  the category options.
     * @return CategoryOptionGroup
     */
    public static CategoryOptionGroup createCategoryOptionGroup( char uniqueIdentifier,
        CategoryOption... categoryOptions )
    {
        CategoryOptionGroup categoryOptionGroup = new CategoryOptionGroup( "CategoryOptionGroup" + uniqueIdentifier );
        categoryOptionGroup.setShortName( "ShortName" + uniqueIdentifier );
        categoryOptionGroup.setAutoFields();

        categoryOptionGroup.setMembers( new HashSet<>() );

        for ( CategoryOption categoryOption : categoryOptions )
        {
            categoryOptionGroup.addCategoryOption( categoryOption );
        }

        return categoryOptionGroup;
    }

    /**
     * @param categoryGroupSetUniqueIdentifier A unique character to identify the
     *                                         category option group set.
     * @param categoryOptionGroups             the category option groups.
     * @return CategoryOptionGroupSet
     */
    public static CategoryOptionGroupSet createCategoryOptionGroupSet( char categoryGroupSetUniqueIdentifier,
        CategoryOptionGroup... categoryOptionGroups )
    {
        CategoryOptionGroupSet categoryOptionGroupSet = new CategoryOptionGroupSet(
            "CategoryOptionGroupSet" + categoryGroupSetUniqueIdentifier );
        categoryOptionGroupSet.setAutoFields();

        for ( CategoryOptionGroup categoryOptionGroup : categoryOptionGroups )
        {
            categoryOptionGroupSet.addCategoryOptionGroup( categoryOptionGroup );
        }

        return categoryOptionGroupSet;
    }


    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    public static IndicatorType createIndicatorType( char uniqueCharacter )
    {
        IndicatorType type = new IndicatorType();
        type.setAutoFields();

        type.setName( "IndicatorType" + uniqueCharacter );
        type.setFactor( 100 );

        return type;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    public static DataElementGroup createDataElementGroup( char uniqueCharacter )
    {
        DataElementGroup group = new DataElementGroup();
        group.setAutoFields();

        group.setUid( BASE_UID + uniqueCharacter );
        group.setName( "DataElementGroup" + uniqueCharacter );
        group.setShortName( "DataElementGroup" + uniqueCharacter );
        group.setCode( "DataElementCode" + uniqueCharacter );

        return group;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    public static DataElementGroupSet createDataElementGroupSet( char uniqueCharacter )
    {
        DataElementGroupSet groupSet = new DataElementGroupSet();
        groupSet.setAutoFields();

        groupSet.setUid( BASE_UID + uniqueCharacter );
        groupSet.setName( "DataElementGroupSet" + uniqueCharacter );

        return groupSet;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param type            The type.
     */
    public static Indicator createIndicator( char uniqueCharacter, IndicatorType type )
    {
        Indicator indicator = new Indicator();
        indicator.setAutoFields();

        indicator.setUid( BASE_IN_UID + uniqueCharacter );
        indicator.setName( "Indicator" + uniqueCharacter );
        indicator.setShortName( "IndicatorShort" + uniqueCharacter );
        indicator.setCode( "IndicatorCode" + uniqueCharacter );
        indicator.setDescription( "IndicatorDescription" + uniqueCharacter );
        indicator.setAnnualized( false );
        indicator.setIndicatorType( type );
        indicator.setNumerator( "Numerator" );
        indicator.setNumeratorDescription( "NumeratorDescription" );
        indicator.setDenominator( "Denominator" );
        indicator.setDenominatorDescription( "DenominatorDescription" );

        return indicator;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    public static IndicatorGroup createIndicatorGroup( char uniqueCharacter )
    {
        IndicatorGroup group = new IndicatorGroup();
        group.setAutoFields();

        group.setUid( BASE_UID + uniqueCharacter );
        group.setName( "IndicatorGroup" + uniqueCharacter );

        return group;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    public static IndicatorGroupSet createIndicatorGroupSet( char uniqueCharacter )
    {
        IndicatorGroupSet groupSet = new IndicatorGroupSet();
        groupSet.setAutoFields();

        groupSet.setUid( BASE_UID + uniqueCharacter );
        groupSet.setName( "IndicatorGroupSet" + uniqueCharacter );

        return groupSet;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    public static DataSet createDataSet( char uniqueCharacter )
    {
        DataSet dataSet = createDataSet( uniqueCharacter, null );
        dataSet.setPeriodType( new MonthlyPeriodType() );

        return dataSet;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param periodType      The period type.
     */
    public static DataSet createDataSet( char uniqueCharacter, PeriodType periodType )
    {
        DataSet dataSet = new DataSet();
        dataSet.setAutoFields();

        dataSet.setUid( BASE_DS_UID + uniqueCharacter );
        dataSet.setName( "DataSet" + uniqueCharacter );
        dataSet.setShortName( "DataSetShort" + uniqueCharacter );
        dataSet.setCode( "DataSetCode" + uniqueCharacter );
        dataSet.setPeriodType( periodType );

        return dataSet;
    }


    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    public static TodoTask createTodotask( char uniqueCharacter )
    {
        TodoTask unit = new TodoTask();
        unit.setAutoFields();

        unit.setUid( BASE_OU_UID + uniqueCharacter );
        unit.setName( "TodoTask" + uniqueCharacter );
        unit.setShortName( "TodoTask" + uniqueCharacter );
        unit.setCode( "TodoTask" + uniqueCharacter );

        return unit;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    public static Activity createActivity( char uniqueCharacter )
    {
        Activity activity = new Activity();
        activity.setAutoFields();

        activity.setUid( BASE_OU_UID + uniqueCharacter );
        activity.setName( "Activity" + uniqueCharacter );
        activity.setShortName( "Activity" + uniqueCharacter );
        activity.setCode( "Activity" + uniqueCharacter );

        return activity;
    }

    /**
     * Uses the given category option combo also as attribute option combo.
     *
     * @param dataElement         The data element.
     * @param period              The period.
     * @param source              The source.
     * @param value               The value.
     */
    public static DataValue createDataValue( DataElement dataElement, Period period, TodoTask source,
        String value )
    {
        DataValue dataValue = new DataValue();

        dataValue.setDataElement( dataElement );
        dataValue.setPeriod( period );
        dataValue.setSource( source );
//        dataValue.setCategoryOptionCombo( categoryOptionCombo );
//        dataValue.setAttributeOptionCombo( categoryOptionCombo );
        dataValue.setValue( value );
        dataValue.setComment( "Comment" );
        dataValue.setStoredBy( "StoredBy" );

        return dataValue;
    }

    /**
     * @param type      The PeriodType.
     * @param startDate The start date.
     */
    public static Period createPeriod( PeriodType type, Date startDate )
    {
        Period period = new Period();
        period.setAutoFields();

        period.setPeriodType( type );
        period.setStartDate( startDate );

        return period;
    }

    /**
     * @param type      The PeriodType.
     * @param startDate The start date.
     * @param endDate   The end date.
     */
    public static Period createPeriod( PeriodType type, Date startDate, Date endDate )
    {
        Period period = new Period();
        period.setAutoFields();

        period.setPeriodType( type );
        period.setStartDate( startDate );
        period.setEndDate( endDate );

        return period;
    }

    /**
     * @param isoPeriod the ISO period string.
     */
    public static Period createPeriod( String isoPeriod )
    {
        return PeriodType.getPeriodFromIsoString( isoPeriod );
    }

    /**
     * @param startDate The start date.
     * @param endDate   The end date.
     */
    public static Period createPeriod( Date startDate, Date endDate )
    {
        Period period = new Period();
        period.setAutoFields();

        period.setPeriodType( new MonthlyPeriodType() );
        period.setStartDate( startDate );
        period.setEndDate( endDate );

        return period;
    }

    public static User createUser( char uniqueCharacter )
    {
        return createUser( uniqueCharacter, Lists.newArrayList() );
    }

    public static User createUser( char uniqueCharacter, List<String> auths )
    {
        UserCredentials credentials = new UserCredentials();
        User user = new User();
        user.setUid( BASE_USER_UID + uniqueCharacter );

        credentials.setUserInfo( user );
        user.setUserCredentials( credentials );

        credentials.setUsername( "username" + uniqueCharacter );
        credentials.setPassword( "password" + uniqueCharacter );

        if ( auths != null && !auths.isEmpty() )
        {
            UserAuthorityGroup role = new UserAuthorityGroup();
            auths.stream().forEach( auth -> role.getAuthorities().add( auth ) );
            credentials.getUserAuthorityGroups().add( role );
        }

        user.setFirstName( "FirstName" + uniqueCharacter );
        user.setSurname( "Surname" + uniqueCharacter );
        user.setEmail( "Email" + uniqueCharacter );
        user.setPhoneNumber( "PhoneNumber" + uniqueCharacter );
        user.setCode( "UserCode" + uniqueCharacter );
        user.setAutoFields();

        return user;
    }

    public static UserCredentials createUserCredentials( char uniqueCharacter, User user )
    {
        UserCredentials credentials = new UserCredentials();
        credentials.setName( "UserCredentials" + uniqueCharacter );
        credentials.setUsername( "Username" + uniqueCharacter );
        credentials.setPassword( "Password" + uniqueCharacter );
        credentials.setUserInfo( user );
        user.setUserCredentials( credentials );

        return credentials;
    }

    public static UserGroup createUserGroup( char uniqueCharacter, Set<User> users )
    {
        UserGroup userGroup = new UserGroup();
        userGroup.setAutoFields();

        userGroup.setUid( BASE_USER_GROUP_UID + uniqueCharacter );
        userGroup.setCode( "UserGroupCode" + uniqueCharacter );
        userGroup.setName( "UserGroup" + uniqueCharacter );
        userGroup.setMembers( users );

        return userGroup;
    }

    public static UserAuthorityGroup createUserAuthorityGroup( char uniqueCharacter )
    {
        return createUserAuthorityGroup( uniqueCharacter, new String[] {} );
    }

    public static UserAuthorityGroup createUserAuthorityGroup( char uniqueCharacter, String... auths )
    {
        UserAuthorityGroup role = new UserAuthorityGroup();
        role.setAutoFields();

        role.setUid( BASE_UID + uniqueCharacter );
        role.setName( "UserAuthorityGroup" + uniqueCharacter );
        
        for ( String auth : auths )
        {
            role.getAuthorities().add( auth );
        }

        return role;
    }

    /**
     * @param uniqueChar A unique character to identify the object.
     * @param content The content of the file
     * @return a fileResource object
     */
    public static FileResource createFileResource( char uniqueChar, byte[] content )
    {
        String filename = "filename" + uniqueChar;
        String contentMd5 = Hashing.md5().hashBytes( content ).toString();
        String contentType = MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE;

        FileResource fileResource = new FileResource( filename, contentType, content.length, contentMd5, FileResourceDomain.DATA_VALUE );
        fileResource.setAssigned( false );
        fileResource.setCreated( new Date() );
        fileResource.setAutoFields();

        return fileResource;
    }

    /**
     * @param uniqueChar A unique character to identify the object.
     * @param content The content of the file
     * @return an externalFileResource object
     */
    public static ExternalFileResource createExternalFileResource( char uniqueChar, byte[] content )
    {
        FileResource fileResource = createFileResource( uniqueChar, content );
        ExternalFileResource externalFileResource = new ExternalFileResource();

        externalFileResource.setFileResource( fileResource );
        fileResource.setAssigned( true );
        externalFileResource.setAccessToken( String.valueOf( uniqueChar ) );
        return externalFileResource;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param value           The value for constant
     * @return a constant instance
     */
    public static Constant createConstant( char uniqueCharacter, double value )
    {
        Constant constant = new Constant();
        constant.setAutoFields();

        constant.setName( "Constant" + uniqueCharacter );
        constant.setValue( value );

        return constant;
    }

    public static OptionSet createOptionSet( char uniqueCharacter )
    {
        OptionSet optionSet = new OptionSet();
        optionSet.setAutoFields();

        optionSet.setName( "OptionSet" + uniqueCharacter );
        optionSet.setCode( "OptionSetCode" + uniqueCharacter );

        return optionSet;
    }

    public static OptionSet createOptionSet( char uniqueCharacter, Option... options )
    {
        OptionSet optionSet = createOptionSet( uniqueCharacter );

        for ( Option option : options )
        {
            optionSet.getOptions().add( option );
            option.setOptionSet( optionSet );
        }

        return optionSet;
    }

    public static Option createOption( char uniqueCharacter )
    {
        Option option = new Option();
        option.setAutoFields();

        option.setName( "Option" + uniqueCharacter );
        option.setCode( "OptionCode" + uniqueCharacter );

        return option;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    protected <T extends IdentifiableObject> T fromJson( String path, Class<T> klass )
    {
        Assert.notNull( renderService, "RenderService must be injected in test" );

        try
        {
            return renderService.fromJson( new ClassPathResource( path ).getInputStream(), klass );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Injects the externalDir property of LocationManager to
     * user.home/dhis2_test_dir. LocationManager dependency must be retrieved from
     * the context up front.
     *
     * @param locationManager The LocationManager to be injected with the external
     *                        directory.
     */
    public void setExternalTestDir( LocationManager locationManager )
    {
        setDependency( locationManager, "externalDir", EXT_TEST_DIR, String.class );
    }

    /**
     * Attempts to remove the external test directory.
     */
    public void removeExternalTestDir()
    {
        deleteDir( new File( EXT_TEST_DIR ) );
    }

    private boolean deleteDir( File dir )
    {
        if ( dir.isDirectory() )
        {
            String[] children = dir.list();

            if ( children != null )
            {
                for ( String aChildren : children )
                {
                    boolean success = deleteDir( new File( dir, aChildren ) );

                    if ( !success )
                    {
                        return false;
                    }
                }
            }
        }

        return dir.delete();
    }

    // -------------------------------------------------------------------------
    // Allow xpath testing of DXF2
    // -------------------------------------------------------------------------

    protected String xpathTest( String xpathString, String xml )
        throws XPathExpressionException
    {
        InputSource source = new InputSource( new StringReader( xml ) );
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext( new Dxf2NamespaceResolver() );

        return xpath.evaluate( xpathString, source );
    }

    protected class Dxf2NamespaceResolver
        implements NamespaceContext
    {
        @Override
        public String getNamespaceURI( String prefix )
        {
            if ( prefix == null )
            {
                throw new IllegalArgumentException( "No prefix provided!" );
            }
            else
            {
                if ( prefix.equals( "d" ) )
                {
                    return "http://dhis2.org/schema/dxf/2.0";
                }
                else
                {
                    return XMLConstants.NULL_NS_URI;
                }
            }
        }

        @Override
        public String getPrefix( String namespaceURI )
        {
            return null;
        }

        @Override
        public Iterator<?> getPrefixes( String namespaceURI )
        {
            return null;
        }
    }

    /**
     * Creates a user and injects into the security context with username
     * "username". Requires <code>identifiableObjectManager</code> and
     * <code>userService</code> to be injected into the test.
     *
     * @param allAuth                   whether to grant the ALL authority.
     * @param auths                     authorities to grant to user.
     * @return the user.
     */
    protected User createUserAndInjectSecurityContext( boolean allAuth, String... auths )
    {
        Assert.notNull( userService, "UserService must be injected in test" );

        Set<String> authorities = new HashSet<>();

        if ( allAuth )
        {
            authorities.add( UserAuthorityGroup.AUTHORITY_ALL );
        }

        if ( auths != null )
        {
            authorities.addAll( Lists.newArrayList( auths ) );
        }

        UserAuthorityGroup userAuthorityGroup = new UserAuthorityGroup();
        userAuthorityGroup.setName( "Superuser" );
        userAuthorityGroup.getAuthorities().addAll( authorities );

        userService.addUserAuthorityGroup( userAuthorityGroup );

        User user = createUser( 'A' );

        user.getUserCredentials().getUserAuthorityGroups().add( userAuthorityGroup );
        userService.addUser( user );
        user.getUserCredentials().setUserInfo( user );
        userService.addUserCredentials( user.getUserCredentials() );

        Set<GrantedAuthority> grantedAuths = authorities.stream().map( a -> new SimpleGrantedAuthority( a ) ).collect( Collectors.toSet() );

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getUserCredentials().getUsername(), user.getUserCredentials().getPassword(), grantedAuths );

        Authentication authentication = new UsernamePasswordAuthenticationToken( userDetails, "", grantedAuths );
        SecurityContextHolder.getContext().setAuthentication( authentication );

        return user;
    }

    protected void saveAndInjectUserSecurityContext( User user )
    {
        userService.addUser( user );
        userService.addUserCredentials( user.getUserCredentials() );

        List<GrantedAuthority> grantedAuthorities = user.getUserCredentials().getAllAuthorities()
            .stream().map( SimpleGrantedAuthority::new ).collect( Collectors.toList() );

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getUserCredentials().getUsername(), user.getUserCredentials().getPassword(), grantedAuthorities );

        Authentication authentication = new UsernamePasswordAuthenticationToken( userDetails, "", grantedAuthorities );
        SecurityContextHolder.getContext().setAuthentication( authentication );
    }

    protected User createUser( String username, String... authorities )
    {
        Assert.notNull( userService, "UserService must be injected in test" );

        String password = "district";

        UserAuthorityGroup userAuthorityGroup = new UserAuthorityGroup();
        userAuthorityGroup.setCode( username );
        userAuthorityGroup.setName( username );
        userAuthorityGroup.setDescription( username );
        userAuthorityGroup.setAuthorities( Sets.newHashSet( authorities ) );

        userService.addUserAuthorityGroup( userAuthorityGroup );

        User user = new User();
        user.setCode( username );
        user.setFirstName( username );
        user.setSurname( username );

        userService.addUser( user );

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setCode( username );
        userCredentials.setUser( user );
        userCredentials.setUserInfo( user );
        userCredentials.setUsername( username );
        userCredentials.getUserAuthorityGroups().add( userAuthorityGroup );

        userService.encodeAndSetPassword( userCredentials, password );
        userService.addUserCredentials( userCredentials );

        user.setUserCredentials( userCredentials );
        userService.updateUser( user );

        return user;
    }

    protected User createAdminUser( String... authorities )
    {
        Assert.notNull( userService, "UserService must be injected in test" );

        String username = "admin";
        String password = "district";

        UserAuthorityGroup userAuthorityGroup = new UserAuthorityGroup();
        userAuthorityGroup.setUid( "yrB6vc5Ip3r" );
        userAuthorityGroup.setCode( "Superuser" );
        userAuthorityGroup.setName( "Superuser" );
        userAuthorityGroup.setDescription( "Superuser" );
        userAuthorityGroup.setAuthorities( Sets.newHashSet( authorities ) );

        userService.addUserAuthorityGroup( userAuthorityGroup );

        User user = new User();
        user.setUid( "M5zQapPyTZI" );
        user.setCode( username );
        user.setFirstName( username );
        user.setSurname( username );

        userService.addUser( user );

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUid( "KvMx6c1eoYo" );
        userCredentials.setCode( username );
        userCredentials.setUser( user );
        userCredentials.setUserInfo( user );
        userCredentials.setUsername( username );
        userCredentials.getUserAuthorityGroups().add( userAuthorityGroup );

        userService.encodeAndSetPassword( userCredentials, password );
        userService.addUserCredentials( userCredentials );

        user.setUserCredentials( userCredentials );
        userService.updateUser( user );

        return user;
    }

    protected User createAndInjectAdminUser()
    {
        return createAndInjectAdminUser( "ALL" );
    }

    protected User createAndInjectAdminUser( String... authorities )
    {
        User user = createAdminUser( authorities );

        List<GrantedAuthority> grantedAuthorities = user.getUserCredentials().getAllAuthorities()
            .stream().map( SimpleGrantedAuthority::new ).collect( Collectors.toList() );

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getUserCredentials().getUsername(), user.getUserCredentials().getPassword(), grantedAuthorities );

        Authentication authentication = new UsernamePasswordAuthenticationToken( userDetails, "", grantedAuthorities );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication( authentication );
        SecurityContextHolder.setContext( context );

        return user;
    }

    protected void injectSecurityContext( User user )
    {
        List<GrantedAuthority> grantedAuthorities = user.getUserCredentials().getAllAuthorities()
            .stream().map( SimpleGrantedAuthority::new ).collect( Collectors.toList() );

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getUserCredentials().getUsername(), user.getUserCredentials().getPassword(), grantedAuthorities );

        Authentication authentication = new UsernamePasswordAuthenticationToken( userDetails, "", grantedAuthorities );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication( authentication );
        SecurityContextHolder.setContext( context );
    }

    protected void clearSecurityContext()
    {
        if ( SecurityContextHolder.getContext() != null )
        {
            SecurityContextHolder.getContext().setAuthentication( null );
        }

        SecurityContextHolder.clearContext();
    }

    protected static String getStackTrace( Throwable t )
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter( sw, true );
        t.printStackTrace( pw );
        pw.flush();
        sw.flush();

        return sw.toString();
    }

    protected void enableDataSharing( User user, IdentifiableObject object, String access )
    {
        object.getUserAccesses().clear();

        UserAccess userAccess = new UserAccess();
        userAccess.setUser( user );
        userAccess.setAccess( access );

        object.getUserAccesses().add( userAccess );
    }
}
