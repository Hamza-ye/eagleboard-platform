package com.mass3d.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Primitives;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.common.EmbeddedObject;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.NameableObject;
import com.mass3d.common.annotation.Description;
import com.mass3d.system.util.AnnotationUtils;
import com.mass3d.system.util.ReflectionUtils;
import com.mass3d.system.util.SchemaUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Default PropertyIntrospectorService implementation that uses Reflection and Jackson annotations
 * for reading in properties.
 *
 */
@Slf4j
@Service( "com.mass3d.schema.PropertyIntrospectorService" )
public class Jackson2PropertyIntrospectorService
    extends AbstractPropertyIntrospectorService
{
    @Override
    protected Map<String, Property> scanClass( Class<?> clazz )
    {
        Map<String, Property> propertyMap = Maps.newHashMap();
        Map<String, Property> hibernatePropertyMap = getPropertiesFromHibernate( clazz );
        List<String> classFieldNames = ReflectionUtils.getAllFieldNames( clazz );

        // TODO this is quite nasty, should find a better way of exposing properties at class-level
        if ( AnnotationUtils.isAnnotationPresent( clazz, JacksonXmlRootElement.class ) )
        {
            Property property = new Property();

            JacksonXmlRootElement jacksonXmlRootElement = AnnotationUtils.getAnnotation( clazz, JacksonXmlRootElement.class );

            if ( !StringUtils.isEmpty( jacksonXmlRootElement.localName() ) )
            {
                property.setName( jacksonXmlRootElement.localName() );
            }

            if ( !StringUtils.isEmpty( jacksonXmlRootElement.namespace() ) )
            {
                property.setNamespace( jacksonXmlRootElement.namespace() );
            }

            propertyMap.put( "__self__", property );
        }

        List<Property> properties = collectProperties( clazz );

        for ( Property property : properties )
        {
            Method getterMethod = property.getGetterMethod();
            JsonProperty jsonProperty = AnnotationUtils.getAnnotation( getterMethod, JsonProperty.class );

            String fieldName = getFieldName( getterMethod );
            property.setName( !StringUtils.isEmpty( jsonProperty.value() ) ? jsonProperty.value() : fieldName );

            if ( property.getGetterMethod() != null )
            {
                property.setReadable( true );
            }

            if ( property.getSetterMethod() != null )
            {
                property.setWritable( true );
            }

            if ( classFieldNames.contains( fieldName ) )
            {
                property.setFieldName( fieldName );
            }

            if ( hibernatePropertyMap.containsKey( fieldName ) )
            {
                Property hibernateProperty = hibernatePropertyMap.get( fieldName );
                property.setPersisted( true );
                property.setWritable( true );
                property.setUnique( hibernateProperty.isUnique() );
                property.setRequired( hibernateProperty.isRequired() );
                property.setLength( hibernateProperty.getLength() );
                property.setMax( hibernateProperty.getMax() );
                property.setMin( hibernateProperty.getMin() );
                property.setCollection( hibernateProperty.isCollection() );
                property.setCascade( hibernateProperty.getCascade() );
                property.setOwner( hibernateProperty.isOwner() );
                property.setManyToMany( hibernateProperty.isManyToMany() );
                property.setOneToOne( hibernateProperty.isOneToOne() );
                property.setManyToOne( hibernateProperty.isManyToOne() );
                property.setOwningRole( hibernateProperty.getOwningRole() );
                property.setInverseRole( hibernateProperty.getInverseRole() );

                property.setGetterMethod( hibernateProperty.getGetterMethod() );
                property.setSetterMethod( hibernateProperty.getSetterMethod() );
            }

            if ( AnnotationUtils.isAnnotationPresent( property.getGetterMethod(), Description.class ) )
            {
                Description description = AnnotationUtils.getAnnotation( property.getGetterMethod(), Description.class );
                property.setDescription( description.value() );
            }

            if ( AnnotationUtils.isAnnotationPresent( property.getGetterMethod(), JacksonXmlProperty.class ) )
            {
                JacksonXmlProperty jacksonXmlProperty = AnnotationUtils.getAnnotation( getterMethod, JacksonXmlProperty.class );

                if ( StringUtils.isEmpty( jacksonXmlProperty.localName() ) )
                {
                    property.setName( property.getName() );
                }
                else
                {
                    property.setName( jacksonXmlProperty.localName() );
                }

                if ( !StringUtils.isEmpty( jacksonXmlProperty.namespace() ) )
                {
                    property.setNamespace( jacksonXmlProperty.namespace() );
                }

                property.setAttribute( jacksonXmlProperty.isAttribute() );
            }

            Class<?> returnType = property.getGetterMethod().getReturnType();
            property.setKlass( Primitives.wrap( returnType ) );

            if ( Collection.class.isAssignableFrom( returnType ) )
            {
                property.setCollection( true );
                property.setCollectionName( property.getName() );
                property.setOrdered( List.class.isAssignableFrom( returnType ) );

                Type type = property.getGetterMethod().getGenericReturnType();

                if ( ParameterizedType.class.isInstance( type ) )
                {
                    Class<?> klass = (Class<?>) getInnerType( (ParameterizedType) type );
                    property.setItemKlass( Primitives.wrap( klass ) );

                    if ( collectProperties( klass ).isEmpty() )
                    {
                        property.setSimple( true );
                    }

                    property.setIdentifiableObject( IdentifiableObject.class.isAssignableFrom( klass ) );
                    property.setNameableObject( NameableObject.class.isAssignableFrom( klass ) );
                    property.setEmbeddedObject( EmbeddedObject.class.isAssignableFrom( klass ) );
//                    property.setAnalyticalObject( AnalyticalObject.class.isAssignableFrom( klass ) );
                }
            }
            else
            {
                if ( collectProperties( returnType ).isEmpty() )
                {
                    property.setSimple( true );
                }
            }

            if ( property.isCollection() )
            {
                if ( AnnotationUtils.isAnnotationPresent( property.getGetterMethod(), JacksonXmlElementWrapper.class ) )
                {
                    JacksonXmlElementWrapper jacksonXmlElementWrapper = AnnotationUtils.getAnnotation( getterMethod, JacksonXmlElementWrapper.class );
                    property.setCollectionWrapping( jacksonXmlElementWrapper.useWrapping() );

                    // TODO what if element-wrapper have different namespace?
                    if ( !StringUtils.isEmpty( jacksonXmlElementWrapper.localName() ) )
                    {
                        property.setCollectionName( jacksonXmlElementWrapper.localName() );
                    }
                }

                propertyMap.put( property.getCollectionName(), property );
            }
            else
            {
                propertyMap.put( property.getName(), property );
            }

            if ( Enum.class.isAssignableFrom( property.getKlass() ) )
            {
                Object[] enumConstants = property.getKlass().getEnumConstants();
                List<String> enumValues = new ArrayList<>();

                for ( Object value : enumConstants )
                {
                    enumValues.add( value.toString() );
                }

                property.setConstants( enumValues );
            }

            SchemaUtils.updatePropertyTypes( property );
        }

        return propertyMap;
    }

    private String getFieldName( Method method )
    {
        String name;

        String[] getters = new String[]{
            "is", "has", "get"
        };

        name = method.getName();

        for ( String getter : getters )
        {
            if ( name.startsWith( getter ) )
            {
                name = name.substring( getter.length() );
            }
        }

        return StringUtils.uncapitalize( name );
    }

    private List<Property> collectProperties( Class<?> klass )
    {
        Multimap<String, Method> multimap = ReflectionUtils.getMethodsMultimap( klass );
        List<String> fieldNames = ReflectionUtils.getAllFields( klass ).stream().map( Field::getName ).collect( Collectors
            .toList() );
        List<Property> properties = new ArrayList<>();

        Map<String, Method> methodMap = multimap.keySet().stream()
            .filter( key ->
            {
                List<Method> methods = multimap.get( key ).stream()
                    .filter( method -> AnnotationUtils.isAnnotationPresent( method, JsonProperty.class ) && method.getParameterTypes().length == 0 )
                    .collect( Collectors.toList() );

                if ( methods.size() > 1 )
                {
                    log.error( "More than one web-api exposed method with name '" + key + "' found on class '" + klass.getName()
                        + "' please fix as this is known to cause issues with Schema / Query services." );

                    log.debug( "Methods found: " + methods );
                }

                return methods.size() == 1;
            } )
            .collect( Collectors.toMap( Function.identity(), key ->
            {
                List<Method> collect = multimap.get( key ).stream()
                    .filter( method -> AnnotationUtils.isAnnotationPresent( method, JsonProperty.class ) && method.getParameterTypes().length == 0 )
                    .collect( Collectors.toList() );

                return collect.get( 0 );
            } ) );

        methodMap.keySet().forEach( key ->
        {
            String fieldName = getFieldName( methodMap.get( key ) );
            String setterName = "set" + StringUtils.capitalize( fieldName );

            Property property = new Property( klass, methodMap.get( key ), null );

            if ( fieldNames.contains( fieldName ) )
            {
                property.setFieldName( fieldName );
            }

            Iterator<Method> methodIterator = multimap.get( setterName ).iterator();

            if ( methodIterator.hasNext() )
            {
                property.setSetterMethod( methodIterator.next() );
            }

            properties.add( property );
        } );

        return properties;
    }

    private Type getInnerType( ParameterizedType parameterizedType )
    {
        ParameterizedType innerType = parameterizedType;

        while ( ParameterizedType.class.isInstance( innerType.getActualTypeArguments()[0] ) )
        {
            innerType = (ParameterizedType) parameterizedType.getActualTypeArguments()[0];
        }

        return innerType.getActualTypeArguments()[0];
    }
}
