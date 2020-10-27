package com.mass3d.webapi.service;

import javassist.util.proxy.ProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


import org.hibernate.collection.spi.PersistentCollection;
import com.mass3d.common.Pager;
import com.mass3d.schema.Property;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaService;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.trimToNull;

@Service
@Slf4j
public class DefaultLinkService implements LinkService
{
    /**
     * The default URL encoding that is used for query parameter values.
     */
    private static final String DEFAULT_URL_ENCODING = "UTF-8";

    private final SchemaService schemaService;

    private final ContextService contextService;

    public DefaultLinkService( SchemaService schemaService, ContextService contextService )
    {
        checkNotNull( schemaService );
        checkNotNull( contextService );

        this.schemaService = schemaService;
        this.contextService = contextService;
    }

    // since classes won't change during runtime, use a map to cache setHref lookups
    private Map<Class<?>, Method> setterCache = new HashMap<>();

    @Override
    public void generatePagerLinks( Pager pager, Class<?> klass )
    {
        if ( pager == null )
        {
            return;
        }

        Schema schema = schemaService.getDynamicSchema( klass );

        if ( !schema.haveApiEndpoint() )
        {
            return;
        }

        generatePagerLinks( pager, schema.getRelativeApiEndpoint() );
    }

    @Override
    public void generatePagerLinks( Pager pager, String relativeApiEndpoint )
    {
        if ( pager == null || trimToNull( relativeApiEndpoint ) == null )
        {
            return;
        }

        final String endpoint = contextService.getApiPath() + relativeApiEndpoint + getContentTypeSuffix();
        final String parameters = getParametersString();

        if ( pager.getPage() < pager.getPageCount() )
        {
            String nextPath = endpoint + "?page=" + (pager.getPage() + 1);
            nextPath += pager.pageSizeIsDefault() ? "" : "&pageSize=" + pager.getPageSize();

            if ( !parameters.isEmpty() )
            {
                nextPath += "&" + parameters;
            }

            pager.setNextPage( nextPath );
        }

        if ( pager.getPage() > 1 )
        {
            if ( (pager.getPage() - 1) == 1 )
            {
                String prevPath = endpoint;

                if ( pager.pageSizeIsDefault() )
                {
                    if ( !parameters.isEmpty() )
                    {
                        prevPath += "?" + parameters;
                    }
                }
                else
                {
                    prevPath += "?pageSize=" + pager.getPageSize();

                    if ( !parameters.isEmpty() )
                    {
                        prevPath += "&" + parameters;
                    }
                }

                pager.setPrevPage( prevPath );
            }
            else
            {
                String prevPath = endpoint + "?page=" + (pager.getPage() - 1);
                prevPath += pager.pageSizeIsDefault() ? "" : "&pageSize=" + pager.getPageSize();

                if ( !parameters.isEmpty() )
                {
                    prevPath += "&" + parameters;
                }

                pager.setPrevPage( prevPath );
            }
        }
    }

    @Override
    public <T> void generateLinks( T object, boolean deepScan )
    {
        generateLinks( object, contextService.getApiPath(), deepScan );
    }

    @Override
    public <T> void generateLinks( T object, String hrefBase, boolean deepScan )
    {
        if ( Collection.class.isInstance( object ) )
        {
            Collection<?> collection = (Collection<?>) object;

            for ( Object collectionObject : collection )
            {
                generateLink( collectionObject, hrefBase, deepScan );
            }
        }
        else
        {
            generateLink( object, hrefBase, deepScan );
        }
    }

    @Override
    public void generateSchemaLinks( List<Schema> schemas )
    {
        schemas.forEach( this::generateSchemaLinks );
    }

    @Override
    public void generateSchemaLinks( Schema schema )
    {
        generateSchemaLinks( schema, contextService.getServletPath() );
    }

    @Override
    public void generateSchemaLinks( Schema schema, String hrefBase )
    {
        schema.setHref( hrefBase + "/schemas/" + schema.getSingular() );

        if ( schema.haveApiEndpoint() )
        {
            schema.setApiEndpoint( hrefBase + schema.getRelativeApiEndpoint() );
        }

        for ( Property property : schema.getProperties() )
        {
            if ( PropertyType.REFERENCE == property.getPropertyType() )
            {
                Schema klassSchema = schemaService.getDynamicSchema( property.getKlass() );
                property.setHref( hrefBase + "/schemas/" + klassSchema.getSingular() );

                if ( klassSchema.haveApiEndpoint() )
                {
                    property.setRelativeApiEndpoint( klassSchema.getRelativeApiEndpoint() );
                    property.setApiEndpoint( hrefBase + klassSchema.getRelativeApiEndpoint() );
                }
            }
            else if ( PropertyType.REFERENCE == property.getItemPropertyType() )
            {
                Schema klassSchema = schemaService.getDynamicSchema( property.getItemKlass() );
                property.setHref( hrefBase + "/schemas/" + klassSchema.getSingular() );

                if ( klassSchema.haveApiEndpoint() )
                {
                    property.setRelativeApiEndpoint( klassSchema.getRelativeApiEndpoint() );
                    property.setApiEndpoint( hrefBase + klassSchema.getRelativeApiEndpoint() );
                }
            }

        }
    }

    @Nonnull
    private String getParametersString()
    {
        final Map<String, List<String>> parameters = contextService.getParameterValuesMap();
        final StringBuilder result = new StringBuilder();

        parameters.forEach( ( name, values ) -> {
            if ( !"page".equals( name ) && !"pageSize".equals( name ) )
            {
                values.forEach( value -> {
                    if ( result.length() > 0 )
                    {
                        result.append( '&' );
                    }

                    try
                    {
                        result.append( URLEncoder.encode( name, DEFAULT_URL_ENCODING ) );
                        result.append( '=' );
                        result.append( URLEncoder.encode( value, DEFAULT_URL_ENCODING ) );
                    }
                    catch ( UnsupportedEncodingException e )
                    {
                        throw new IllegalStateException( "Encoding for URL values is not supported: " + DEFAULT_URL_ENCODING );
                    }
                } );
            }
        } );

        return result.toString();
    }

    @Nonnull
    protected String getContentTypeSuffix()
    {
        String requestUri = contextService.getRequest().getRequestURI();

        if ( requestUri == null )
        {
            return StringUtils.EMPTY;
        }

        int index = requestUri.lastIndexOf( '/' );

        if ( index >= 0 )
        {
            // the request URI itself may have dots inside
            requestUri = requestUri.substring( index );
        }

        index = requestUri.indexOf( '.' );

        if ( index < 0 )
        {
            return StringUtils.EMPTY;
        }

        return requestUri.substring( index );
    }

    private <T> void generateLink( T object, String hrefBase, boolean deepScan )
    {
        Schema schema = schemaService.getDynamicSchema( object.getClass() );

        if ( schema == null )
        {
            log.warn( "Could not find schema for object of type " + object.getClass().getName() + "." );
            return;
        }

        generateHref( object, hrefBase );

        if ( !deepScan )
        {
            return;
        }

        for ( Property property : schema.getProperties() )
        {
            try
            {
                // TODO should we support non-idObjects?
                if ( property.isIdentifiableObject() )
                {
                    Object propertyObject = property.getGetterMethod().invoke( object );

                    if ( propertyObject == null )
                    {
                        continue;
                    }

                    // unwrap hibernate PersistentCollection
                    if ( PersistentCollection.class.isAssignableFrom( propertyObject.getClass() ) )
                    {
                        PersistentCollection collection = (PersistentCollection) propertyObject;
                        propertyObject = collection.getValue();
                    }

                    if ( !property.isCollection() )
                    {
                        generateHref( propertyObject, hrefBase );
                    }
                    else
                    {
                        Collection<?> collection = (Collection<?>) propertyObject;

                        for ( Object collectionObject : collection )
                        {
                            generateHref( collectionObject, hrefBase );
                        }
                    }

                }
            }
            catch ( InvocationTargetException | IllegalAccessException ignored )
            {
            }
        }
    }

    private <T> void generateHref( T object, String hrefBase )
    {
        if ( object == null || getSetter( object.getClass() ) == null )
        {
            return;
        }

        Class<?> klass = object.getClass();

        if ( ProxyFactory.isProxyClass( klass ) )
        {
            klass = klass.getSuperclass();
        }

        Schema schema = schemaService.getDynamicSchema( klass );

        if ( !schema.haveApiEndpoint() || schema.getProperty( "id" ) == null || schema.getProperty( "id" ).getGetterMethod() == null )
        {
            return;
        }

        Property id = schema.getProperty( "id" );

        try
        {
            Object value = id.getGetterMethod().invoke( object );

            if ( !String.class.isInstance( value ) )
            {
                log.warn( "id on object of type " + object.getClass().getName() + " does not return a String." );
                return;
            }

            Method setHref = getSetter( object.getClass() );
            setHref.invoke( object, hrefBase + schema.getRelativeApiEndpoint() + "/" + value );
        }
        catch ( InvocationTargetException | IllegalAccessException ignored )
        {
        }
    }

    private Method getSetter( Class<?> klass )
    {
        if ( !setterCache.containsKey( klass ) )
        {
            try
            {
                setterCache.put( klass, klass.getMethod( "setHref", String.class ) );
            }
            catch ( NoSuchMethodException ignored )
            {
            }
        }

        return setterCache.get( klass );
    }
}
