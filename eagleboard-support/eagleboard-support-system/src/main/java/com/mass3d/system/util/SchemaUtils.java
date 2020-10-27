package com.mass3d.system.util;

import static com.mass3d.schema.PropertyType.*;
import static com.mass3d.schema.PropertyType.BOOLEAN;
import static com.mass3d.schema.PropertyType.CONSTANT;
import static com.mass3d.schema.PropertyType.DATE;
import static com.mass3d.schema.PropertyType.REFERENCE;

import com.google.common.collect.Sets;
import com.google.common.primitives.Primitives;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.schema.Property;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.annotation.PropertyRange;
import com.mass3d.schema.annotation.PropertyTransformer;
import org.springframework.util.Assert;

public final class SchemaUtils
{
    private static final Set<PropertyType> PROPS_IGNORE_MINMAX = Sets
        .newHashSet( REFERENCE, BOOLEAN, DATE, CONSTANT );

    public static void updatePropertyTypes( Property property )
    {
        Assert.notNull( property, "Property cannot be null" );
        Assert.notNull( property.getKlass(), "Property class cannot be null" );

        property.setPropertyType( getPropertyType( property.getKlass() ) );

        if ( property.isCollection() )
        {
            property.setItemPropertyType( getPropertyType( property.getItemKlass() ) );
        }

        if ( property.isWritable() )
        {
            if ( AnnotationUtils.isAnnotationPresent( property.getGetterMethod(), com.mass3d.schema.annotation.Property.class ) )
            {
                com.mass3d.schema.annotation.Property pAnnotation = AnnotationUtils.getAnnotation( property.getGetterMethod(), com.mass3d.schema.annotation.Property.class );
                property.setPropertyType( pAnnotation.value() );

                if ( pAnnotation.required() != com.mass3d.schema.annotation.Property.Value.DEFAULT )
                {
                    property.setRequired( pAnnotation.required() == com.mass3d.schema.annotation.Property.Value.TRUE );
                }

                if ( pAnnotation.persisted() != com.mass3d.schema.annotation.Property.Value.DEFAULT )
                {
                    property.setPersisted( pAnnotation.persisted() == com.mass3d.schema.annotation.Property.Value.TRUE );
                }

                if ( pAnnotation.owner() != com.mass3d.schema.annotation.Property.Value.DEFAULT )
                {
                    property.setOwner( pAnnotation.owner() == com.mass3d.schema.annotation.Property.Value.TRUE );
                }

                if ( com.mass3d.schema.annotation.Property.Access.READ_ONLY == pAnnotation.access() )
                {
                    property.setWritable( false );
                }

                if ( com.mass3d.schema.annotation.Property.Access.WRITE_ONLY == pAnnotation.access() )
                {
                    property.setReadable( false );
                }
            }

            if ( AnnotationUtils.isAnnotationPresent( property.getGetterMethod(), PropertyTransformer.class ) )
            {
                PropertyTransformer propertyTransformer = AnnotationUtils.getAnnotation( property.getGetterMethod(), PropertyTransformer.class );
                property.setPropertyTransformer( propertyTransformer.value() );
            }

            if ( AnnotationUtils.isAnnotationPresent( property.getGetterMethod(), PropertyRange.class ) )
            {
                PropertyRange propertyRange = AnnotationUtils.getAnnotation( property.getGetterMethod(), PropertyRange.class );

                double max = propertyRange.max();
                double min = propertyRange.min();

                if ( property.is( PropertyType.INTEGER ) || property.is( PropertyType.TEXT ) || property.is( PropertyType.COLLECTION ) )
                {
                    if ( max > Integer.MAX_VALUE )
                    {
                        max = Integer.MAX_VALUE;
                    }
                }

                if ( property.is( PropertyType.COLLECTION ) && min < 0 )
                {
                    min = 0d;
                }

                //Max will be applied from PropertyRange annotation only if it is more restrictive than hibernate max (or its a collection)
                if ( property.getMax() == null || max < property.getMax() || property.is( PropertyType.COLLECTION ) )
                {
                    property.setMax( max );
                }

                //Min is not set by hibernate (always 0) hence the min from PropertyRange will always be applied.
                property.setMin( min );
            }

            if ( property.getMin() == null )
            {
                property.setMin( 0d );
            }

            if ( property.getMax() == null )
            {
                if ( property.is( PropertyType.INTEGER ) || property.is( PropertyType.TEXT ) )
                {
                    property.setMax( (double) Integer.MAX_VALUE );
                }
                else
                {
                    property.setMax( Double.MAX_VALUE );
                }
            }

            if ( PROPS_IGNORE_MINMAX.contains( property.getPropertyType() ) )
            {
                property.setMin( null );
                property.setMax( null );
            }
        }
        else
        {
            property.setMin( null );
            property.setMax( null );
        }
    }

    private static PropertyType getPropertyType( Class<?> klass )
    {
        if ( isAssignableFrom( klass, String.class )
            || isAssignableFrom( klass, Character.class )
            || isAssignableFrom( klass, Byte.class ) )
        {
            return PropertyType.TEXT;
        }
        else if ( isAssignableFrom( klass, Integer.class ) )
        {
            return PropertyType.INTEGER;
        }
        else if ( isAssignableFrom( klass, Boolean.class ) )
        {
            return PropertyType.BOOLEAN;
        }
        else if ( isAssignableFrom( klass, Float.class )
            || isAssignableFrom( klass, Double.class ) )
        {
            return PropertyType.NUMBER;
        }
        else if ( isAssignableFrom( klass, Date.class )
            || isAssignableFrom( klass, java.sql.Date.class ) )
        {
            return PropertyType.DATE;
        }
        else if ( isAssignableFrom( klass, Enum.class ) )
        {
            return PropertyType.CONSTANT;
        }
        else if ( isAssignableFrom( klass, IdentifiableObject.class ) )
        {
            return PropertyType.REFERENCE;
        }
        else if ( isAssignableFrom( klass, Collection.class ) )
        {
            return PropertyType.COLLECTION;
        }

        // if klass is primitive (but unknown), fall back to text, if its not then assume reference
        return Primitives.isWrapperType( klass ) ? PropertyType.TEXT : PropertyType.COMPLEX;
    }

    private static boolean isAssignableFrom( Class<?> propertyKlass, Class<?> klass )
    {
        return klass.isAssignableFrom( propertyKlass );
    }

    private SchemaUtils()
    {
    }
}
