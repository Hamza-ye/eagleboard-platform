package com.mass3d.dxf2.metadata.objectbundle.hooks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hibernate.Session;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.period.PeriodType;
import com.mass3d.schema.Property;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.Schema;
import com.mass3d.schema.validation.SchemaValidator;
import com.mass3d.system.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmbeddedObjectObjectBundleHook
    extends AbstractObjectBundleHook
{
//    @Autowired
//    private DefaultAnalyticalObjectImportHandler analyticalObjectImportHandler;

    @Autowired
    private SchemaValidator schemaValidator;

    @Override
    public List<ErrorReport> validate( IdentifiableObject object, ObjectBundle bundle )
    {
        List<ErrorReport> errors = new ArrayList<>();

        Class<? extends IdentifiableObject> klass = object.getClass();
        Schema schema = schemaService.getDynamicSchema( klass );

        schema.getEmbeddedObjectProperties().keySet()
            .stream()
            .forEach( propertyName -> {
                Property property = schema.getEmbeddedObjectProperties().get( propertyName );
                Object propertyObject = ReflectionUtils.invokeMethod( object, property.getGetterMethod() );

                if ( property.getPropertyType().equals( PropertyType.COMPLEX ) )
                {
                    List<ErrorReport> unformattedErrors = schemaValidator
                        .validateEmbeddedObject( propertyObject, klass );
                    errors.addAll( formatEmbeddedErrorReport( unformattedErrors, propertyName ) );
                }
                else if ( property.getPropertyType().equals( PropertyType.COLLECTION ) )
                {
                    Collection<?> collection = (Collection<?>) propertyObject;
                    for ( Object item : collection )
                    {
                        List<ErrorReport> unformattedErrors = schemaValidator
                            .validateEmbeddedObject( property.getItemKlass().cast( item ), klass );
                        errors.addAll( formatEmbeddedErrorReport( unformattedErrors, propertyName ) );
                    }
                }
            } );

        return errors;
    }

    private List<ErrorReport> formatEmbeddedErrorReport( List<ErrorReport> errors, String embeddedPropertyName )
    {
        for ( ErrorReport errorReport : errors )
        {
            errorReport.setErrorProperty( embeddedPropertyName + "." + errorReport.getErrorProperty() );
        }

        return errors;
    }

    @Override
    public <T extends IdentifiableObject> void preCreate( T object, ObjectBundle bundle )
    {
        Schema schema = schemaService.getDynamicSchema( object.getClass() );

        if ( schema == null || schema.getEmbeddedObjectProperties().isEmpty() )
        {
            return;
        }

        Collection<Property> properties = schema.getEmbeddedObjectProperties().values();

        handleEmbeddedObjects( object, bundle, properties );
    }

    @Override
    public <T extends IdentifiableObject> void preUpdate( T object, T persistedObject, ObjectBundle bundle )
    {
        Schema schema = schemaService.getDynamicSchema( object.getClass() );

        if ( schema == null || schema.getEmbeddedObjectProperties().isEmpty() )
        {
            return;
        }

        Collection<Property> properties = schema.getEmbeddedObjectProperties().values();

        clearEmbeddedObjects( persistedObject, bundle, properties );
        handleEmbeddedObjects( object, bundle, properties );
    }

    private <T extends IdentifiableObject> void clearEmbeddedObjects( T object, ObjectBundle bundle, Collection<Property> properties )
    {
        for ( Property property : properties )
        {
            if ( property.isCollection() )
            {
                if ( ReflectionUtils.isSharingProperty( property ) && bundle.isSkipSharing() )
                {
                    continue;
                }

                ( (Collection<?>) ReflectionUtils.invokeMethod( object, property.getGetterMethod() ) ).clear();
            }
            else
            {
                ReflectionUtils.invokeMethod( object, property.getSetterMethod(), (Object) null );
            }
        }
    }

    private <T extends IdentifiableObject> void handleEmbeddedObjects( T object, ObjectBundle bundle, Collection<Property> properties )
    {
        for ( Property property : properties )
        {
            Object propertyObject =  ReflectionUtils.invokeMethod( object, property.getGetterMethod() );

            if ( property.isCollection() )
            {
                Collection<?> objects = (Collection<?>) propertyObject;
                objects.forEach( itemPropertyObject ->
                {
                    handleProperty( itemPropertyObject, bundle, property );
                    handleEmbeddedAnalyticalProperty( itemPropertyObject, bundle, property );
                } );
            }
            else
            {
                handleProperty( propertyObject, bundle, property );
                handleEmbeddedAnalyticalProperty( propertyObject, bundle, property );
            }
        }
    }

    private void handleProperty( Object object, ObjectBundle bundle, Property property )
    {
        if ( object == null || bundle == null || property == null )
        {
            return;
        }

        if ( property.isIdentifiableObject() )
        {
            ((BaseIdentifiableObject) object).setAutoFields();
        }

        Schema embeddedSchema = schemaService.getDynamicSchema( object.getClass() );
        for ( Property embeddedProperty : embeddedSchema.getPropertyMap().values() )
        {
            if ( PeriodType.class.isAssignableFrom( embeddedProperty.getKlass() ) )
            {
                PeriodType periodType = ReflectionUtils.invokeMethod( object, embeddedProperty.getGetterMethod() );

                if ( periodType != null )
                {
                    periodType = bundle.getPreheat().getPeriodTypeMap().get( periodType.getName() );
                    ReflectionUtils.invokeMethod( object, embeddedProperty.getSetterMethod(), periodType );
                }
            }
        }

        preheatService.connectReferences( object, bundle.getPreheat(), bundle.getPreheatIdentifier() );
    }

    private void handleEmbeddedAnalyticalProperty( Object identifiableObject, ObjectBundle bundle, Property property )
    {
        if ( identifiableObject == null || property == null || !property.isAnalyticalObject() )
        {
            return;
        }

        Session session = sessionFactory.getCurrentSession();

        Schema propertySchema = schemaService.getDynamicSchema( property.getItemKlass() );

//        analyticalObjectImportHandler.handleAnalyticalObject( session, propertySchema, ( BaseAnalyticalObject ) identifiableObject, bundle );
    }
}
