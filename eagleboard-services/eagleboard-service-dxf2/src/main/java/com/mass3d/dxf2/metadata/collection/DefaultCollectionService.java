package com.mass3d.dxf2.metadata.collection;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import com.mass3d.cache.HibernateCacheManager;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dbms.DbmsManager;
import com.mass3d.dxf2.webmessage.WebMessageException;
import com.mass3d.dxf2.webmessage.WebMessageUtils;
import com.mass3d.hibernate.exception.UpdateAccessDeniedException;
import com.mass3d.schema.Property;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaService;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service ( "com.mass3d.dxf2.metadata.collection.CollectionService" )
@Transactional
public class DefaultCollectionService
    implements CollectionService
{
    private IdentifiableObjectManager manager;

    private DbmsManager dbmsManager;

    private HibernateCacheManager cacheManager;

    private AclService aclService;

    private SchemaService schemaService;

    private CurrentUserService currentUserService;

    public DefaultCollectionService( IdentifiableObjectManager manager, DbmsManager dbmsManager,
        HibernateCacheManager cacheManager, AclService aclService, SchemaService schemaService,
        CurrentUserService currentUserService )
    {
        checkNotNull( manager );
        checkNotNull( dbmsManager );
        checkNotNull( cacheManager );
        checkNotNull( aclService );
        checkNotNull( schemaService );
        checkNotNull( currentUserService );

        this.manager = manager;
        this.dbmsManager = dbmsManager;
        this.cacheManager = cacheManager;
        this.aclService = aclService;
        this.schemaService = schemaService;
        this.currentUserService = currentUserService;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public void addCollectionItems( IdentifiableObject object, String propertyName, List<IdentifiableObject> objects ) throws Exception
    {
        Schema schema = schemaService.getDynamicSchema( object.getClass() );

        if ( !aclService.canUpdate( currentUserService.getCurrentUser(), object ) )
        {
            throw new UpdateAccessDeniedException( "You don't have the proper permissions to update this object." );
        }

        if ( !schema.haveProperty( propertyName ) )
        {
            throw new WebMessageException( WebMessageUtils.notFound( "Property " + propertyName + " does not exist on " + object.getClass().getName() ) );
        }

        Property property = schema.getProperty( propertyName );

        if ( !property.isCollection() || !property.isIdentifiableObject() )
        {
            throw new WebMessageException( WebMessageUtils.conflict( "Only identifiable object collections can be added to." ) );
        }

        Collection<String> itemCodes = objects.stream().map( IdentifiableObject::getUid ).collect( Collectors
            .toList() );

        if ( itemCodes.isEmpty() )
        {
            return;
        }

        List<? extends IdentifiableObject> items = manager.get( ((Class<? extends IdentifiableObject>) property.getItemKlass()), itemCodes );

        manager.refresh( object );

        if ( property.isOwner() )
        {
            Collection<IdentifiableObject> collection = (Collection<IdentifiableObject>) property.getGetterMethod().invoke( object );

            for ( IdentifiableObject item : items )
            {
                if ( !collection.contains( item ) ) collection.add( item );
            }

            manager.update( object );
        }
        else
        {
            Schema owningSchema = schemaService.getDynamicSchema( property.getItemKlass() );
            Property owningProperty = owningSchema.propertyByRole( property.getOwningRole() );

            for ( IdentifiableObject item : items )
            {
                try
                {
                    Collection<IdentifiableObject> collection = (Collection<IdentifiableObject>) owningProperty.getGetterMethod().invoke( item );

                    if ( !collection.contains( object ) )
                    {
                        collection.add( object );
                        manager.update( item );
                    }
                }
                catch ( Exception ex )
                {
                }
            }
        }

        dbmsManager.clearSession();
        cacheManager.clearCache();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public void delCollectionItems( IdentifiableObject object, String propertyName, List<IdentifiableObject> objects ) throws Exception
    {
        Schema schema = schemaService.getDynamicSchema( object.getClass() );

        if ( !aclService.canUpdate( currentUserService.getCurrentUser(), object ) )
        {
            throw new UpdateAccessDeniedException( "You don't have the proper permissions to update this object." );
        }

        if ( !schema.haveProperty( propertyName ) )
        {
            throw new WebMessageException( WebMessageUtils.notFound( "Property " + propertyName + " does not exist on " + object.getClass().getName() ) );
        }

        Property property = schema.getProperty( propertyName );

        if ( !property.isCollection() || !property.isIdentifiableObject() )
        {
            throw new WebMessageException( WebMessageUtils.conflict( "Only identifiable object collections can be removed from." ) );
        }

        Collection<String> itemCodes = objects.stream().map( IdentifiableObject::getUid ).collect( Collectors
            .toList() );

        if ( itemCodes.isEmpty() )
        {
            return;
        }

        List<? extends IdentifiableObject> items = manager.get( ((Class<? extends IdentifiableObject>) property.getItemKlass()), itemCodes );

        manager.refresh( object );

        if ( property.isOwner() )
        {
            Collection<IdentifiableObject> collection = (Collection<IdentifiableObject>) property.getGetterMethod().invoke( object );

            for ( IdentifiableObject item : items )
            {
                if ( collection.contains( item ) ) collection.remove( item );
            }
        }
        else
        {
            Schema owningSchema = schemaService.getDynamicSchema( property.getItemKlass() );
            Property owningProperty = owningSchema.propertyByRole( property.getOwningRole() );

            for ( IdentifiableObject item : items )
            {
                try
                {
                    Collection<IdentifiableObject> collection = (Collection<IdentifiableObject>) owningProperty.getGetterMethod().invoke( item );

                    if ( collection.contains( object ) )
                    {
                        collection.remove( object );
                        manager.update( item );
                    }
                }
                catch ( Exception ex )
                {
                }
            }
        }

        manager.update( object );

        dbmsManager.clearSession();
        cacheManager.clearCache();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public void clearCollectionItems( IdentifiableObject object, String pvProperty ) throws WebMessageException, InvocationTargetException, IllegalAccessException
    {
        Schema schema = schemaService.getDynamicSchema( object.getClass() );

        if ( !schema.haveProperty( pvProperty ) )
        {
            throw new WebMessageException( WebMessageUtils.notFound( "Property " + pvProperty + " does not exist on " + object.getClass().getName() ) );
        }

        Property property = schema.getProperty( pvProperty );

        if ( !property.isCollection() || !property.isIdentifiableObject() )
        {
            throw new WebMessageException( WebMessageUtils.conflict( "Only identifiable collections are allowed to be cleared." ) );
        }

        Collection<IdentifiableObject> collection = (Collection<IdentifiableObject>) property.getGetterMethod().invoke( object );

        manager.refresh( object );

        if ( property.isOwner() )
        {
            collection.clear();
            manager.update( object );
        }
        else
        {
            for ( IdentifiableObject itemObject : collection )
            {
                Schema itemSchema = schemaService.getDynamicSchema( property.getItemKlass() );
                Property itemProperty = itemSchema.propertyByRole( property.getOwningRole() );
                Collection<IdentifiableObject> itemCollection = (Collection<IdentifiableObject>) itemProperty.getGetterMethod().invoke( itemObject );
                itemCollection.remove( object );

                manager.update( itemObject );
                manager.refresh( itemObject );
            }
        }
    }
}
