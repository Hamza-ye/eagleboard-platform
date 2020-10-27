package com.mass3d.schema.validation;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.validator.GenericValidator;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.preheat.Preheat;
import com.mass3d.schema.Property;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaService;
import com.mass3d.system.util.ReflectionUtils;
import com.mass3d.system.util.ValidationUtils;
import com.mass3d.user.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service( "com.mass3d.schema.validation.SchemaValidator" )
public class DefaultSchemaValidator implements SchemaValidator
{
    private Pattern BCRYPT_PATTERN = Pattern.compile( "\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}" );

    private final SchemaService schemaService;

    public DefaultSchemaValidator( SchemaService schemaService )
    {
        checkNotNull( schemaService );
        this.schemaService = schemaService;
    }

    @Override
    public List<ErrorReport> validate( Object object )
    {
        return validate( object, true );
    }

    @Override
    public List<ErrorReport> validateEmbeddedObject( Object object, Class<?> parentClass )
    {
        return validate( object, true, parentClass );
    }

    @Override
    public List<ErrorReport> validate( Object object, boolean persisted )
    {
        return validate( object, persisted, object.getClass() );
    }

    public List<ErrorReport> validate( Object object, boolean persisted, Class<?> mainErrorClass )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        if ( object == null )
        {
            return errorReports;
        }

        Schema schema = schemaService.getDynamicSchema( object.getClass() );

        if ( schema == null )
        {
            return errorReports;
        }

        for ( Property property : schema.getProperties() )
        {
            if ( persisted && !property.isPersisted() )
            {
                continue;
            }

            Object value = ReflectionUtils.invokeMethod( object, property.getGetterMethod() );

            if ( value == null )
            {
                if ( property.isRequired() && !Preheat.isDefaultClass( property.getKlass() ) )
                {
                    errorReports.add( new ErrorReport( mainErrorClass, ErrorCode.E4000, property.getName() )
                        .setErrorKlass( property.getKlass() ).setErrorProperty( property.getName() ) );
                }

                continue;
            }

            errorReports.addAll( validateString( mainErrorClass, value, property ) );
            errorReports.addAll( validateCollection( mainErrorClass, value, property ) );
            errorReports.addAll( validateInteger( mainErrorClass, value, property ) );
            errorReports.addAll( validateFloat( mainErrorClass, value, property ) );
            errorReports.addAll( validateDouble( mainErrorClass, value, property ) );
        }

        if ( User.class.isInstance( object ) )
        {
            User user = (User) object;

            if ( user.getUserCredentials() != null )
            {
                errorReports.addAll( validate( user.getUserCredentials(), persisted ) );
            }
        }

        return errorReports;
    }

    private List<? extends ErrorReport> validateString( Class<?> klass, Object propertyObject, Property property )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        // TODO How should empty strings be handled? they are not valid color, password, url, etc of course.
        if ( !String.class.isInstance( propertyObject ) || StringUtils.isEmpty( propertyObject ) )
        {
            return errorReports;
        }

        String value = (String) propertyObject;

        // Check column max length
        if ( value.length() > property.getLength() )
        {
            errorReports.add( new ErrorReport( klass, ErrorCode.E4001, property.getName(), property.getLength(), value.length() )
                .setErrorKlass( property.getKlass() ).setErrorProperty( property.getName() ) );
            return errorReports;
        }

        if ( value.length() < property.getMin() || value.length() > property.getMax() )
        {
            errorReports.add( new ErrorReport( klass, ErrorCode.E4002, property.getName(), property.getMin(), property.getMax(), value.length() )
                .setErrorKlass( property.getKlass() ).setErrorProperty( property.getName() ) );
        }

        if ( PropertyType.EMAIL == property.getPropertyType() && !GenericValidator.isEmail( value ) )
        {
            errorReports.add( new ErrorReport( klass, ErrorCode.E4003, property.getName(), value )
                .setErrorKlass( property.getKlass() ).setErrorProperty( property.getName() ) );
        }
        else if ( PropertyType.URL == property.getPropertyType() && !isUrl( value ) )
        {
            errorReports.add( new ErrorReport( klass, ErrorCode.E4004, property.getName(), value )
                .setErrorKlass( property.getKlass() ).setErrorProperty( property.getName() ) );
        }
        else if ( !BCRYPT_PATTERN.matcher( value ).matches() && PropertyType.PASSWORD == property.getPropertyType() && !ValidationUtils.passwordIsValid( value ) )
        {
            errorReports.add( new ErrorReport( klass, ErrorCode.E4005, property.getName(), value )
                .setErrorKlass( property.getKlass() ).setErrorProperty( property.getName() ) );
        }
        else if ( PropertyType.COLOR == property.getPropertyType() && !ValidationUtils.isValidHexColor( value ) )
        {
            errorReports.add( new ErrorReport( klass, ErrorCode.E4006, property.getName(), value )
                .setErrorKlass( property.getKlass() ).setErrorProperty( property.getName() ) );
        }

        /* TODO add proper validation for both Points and Polygons, ValidationUtils only supports points at this time
        if ( PropertyType.GEOLOCATION == property.getPropertyType() && !ValidationUtils.coordinateIsValid( value ) )
        {
            validationViolations.add( new ValidationViolation( "Value is not a valid coordinate pair [lon, lat]." ) );
        }
        */

        return errorReports;
    }

    // Commons validator have some issues in latest version, replacing with a very simple test for now
    private boolean isUrl( String url )
    {
        return !StringUtils.isEmpty( url ) && (url.startsWith( "http://" ) || url.startsWith( "https://" ));
    }

    private List<? extends ErrorReport> validateCollection( Class<?> klass, Object propertyObject, Property property )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        if ( !Collection.class.isInstance( propertyObject ) )
        {
            return errorReports;
        }

        Collection<?> value = (Collection<?>) propertyObject;

        if ( value.size() < property.getMin() || value.size() > property.getMax() )
        {
            errorReports.add( new ErrorReport( klass, ErrorCode.E4007, property.getName(), property.getMin(), property.getMax(), value.size() )
                .setErrorKlass( property.getKlass() ).setErrorProperty( property.getName() ) );
        }

        return errorReports;
    }

    private List<? extends ErrorReport> validateInteger( Class<?> klass, Object propertyObject, Property property )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        if ( !Integer.class.isInstance( propertyObject ) )
        {
            return errorReports;
        }

        Integer value = (Integer) propertyObject;

        if ( !GenericValidator.isInRange( value, property.getMin(), property.getMax() ) )
        {
            errorReports.add( new ErrorReport( klass, ErrorCode.E4008, property.getName(), property.getMin(), property.getMax(), value )
                .setErrorKlass( property.getKlass() ).setErrorProperty( property.getName() ) );
        }

        return errorReports;
    }

    private List<? extends ErrorReport> validateFloat( Class<?> klass, Object propertyObject, Property property )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        if ( !Float.class.isInstance( propertyObject ) )
        {
            return errorReports;
        }

        Float value = (Float) propertyObject;

        if ( !GenericValidator.isInRange( value, property.getMin(), property.getMax() ) )
        {
            errorReports.add( new ErrorReport( klass, ErrorCode.E4008, property.getName(), property.getMin(), property.getMax(), value )
                .setErrorKlass( property.getKlass() ).setErrorProperty( property.getName() ) );
        }

        return errorReports;
    }

    private List<? extends ErrorReport> validateDouble( Class<?> klass, Object propertyObject, Property property )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        if ( !Double.class.isInstance( propertyObject ) )
        {
            return errorReports;
        }

        Double value = (Double) propertyObject;

        if ( !GenericValidator.isInRange( value, property.getMin(), property.getMax() ) )
        {
            errorReports.add( new ErrorReport( klass, ErrorCode.E4008, property.getName(), property.getMin(), property.getMax(), value )
                .setErrorKlass( property.getKlass() ).setErrorProperty( property.getName() ) );
        }

        return errorReports;
    }
}
