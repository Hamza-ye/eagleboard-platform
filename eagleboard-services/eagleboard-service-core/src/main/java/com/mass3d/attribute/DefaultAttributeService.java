package com.mass3d.attribute;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.hibernate.SessionFactory;
import com.mass3d.attribute.exception.NonUniqueAttributeValueException;
import com.mass3d.cache.Cache;
import com.mass3d.cache.SimpleCacheBuilder;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.commons.util.SystemUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.attribute.AttributeService" )
public class DefaultAttributeService
    implements AttributeService
{
    private Cache<Attribute> attributeCache;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final AttributeStore attributeStore;

    private final IdentifiableObjectManager manager;

    private SessionFactory sessionFactory;

    private final Environment env;

    public DefaultAttributeService( AttributeStore attributeStore, IdentifiableObjectManager manager,
        SessionFactory sessionFactory, Environment env )
    {
        checkNotNull( attributeStore );
        checkNotNull( manager );

        this.attributeStore = attributeStore;
        this.manager = manager;
        this.sessionFactory = sessionFactory;
        this.env = env;
    }

    @PostConstruct
    public void init()
    {
        attributeCache = new SimpleCacheBuilder<Attribute>()
            .forRegion( "metadataAttributes" )
            .expireAfterWrite( 12, TimeUnit.HOURS )
            .withMaximumSize( SystemUtils.isTestRun( env.getActiveProfiles() ) ? 0 : 10000 ).build();
    }

    // -------------------------------------------------------------------------
    // Attribute implementation
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void addAttribute( Attribute attribute )
    {
        attributeStore.save( attribute );
    }

    @Override
    @Transactional
    public void deleteAttribute( Attribute attribute )
    {
        attributeCache.invalidate( attribute.getUid() );
        attributeStore.delete( attribute );
    }

    @Override
    public void invalidateCachedAttribute( String attributeUid )
    {
        attributeCache.invalidate( attributeUid );
    }

    @Override
    @Transactional(readOnly = true)
    public Attribute getAttribute( long id )
    {
        return attributeStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public Attribute getAttribute( String uid )
    {
        Optional<Attribute> attribute = attributeCache.get( uid, attr -> attributeStore.getByUid( uid ) );
        return attribute.orElse( null );
    }

    @Override
    @Transactional(readOnly = true)
    public Attribute getAttributeByName( String name )
    {
        return attributeStore.getByName( name );
    }

    @Override
    @Transactional(readOnly = true)
    public Attribute getAttributeByCode( String code )
    {
        return attributeStore.getByCode( code );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attribute> getAllAttributes()
    {
        return new ArrayList<>( attributeStore.getAll() );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attribute> getAttributes( Class<?> klass )
    {
        return new ArrayList<>( attributeStore.getAttributes( klass ) );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attribute> getMandatoryAttributes( Class<?> klass )
    {
        return new ArrayList<>( attributeStore.getMandatoryAttributes( klass ) );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attribute> getUniqueAttributes( Class<?> klass )
    {
        return new ArrayList<>( attributeStore.getUniqueAttributes( klass ) );
    }

    // -------------------------------------------------------------------------
    // AttributeValue implementation
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public <T extends IdentifiableObject> void addAttributeValue( T object, AttributeValue attributeValue ) throws NonUniqueAttributeValueException
    {
        if ( object == null || attributeValue == null || attributeValue.getAttribute() == null )
        {
            return;
        }

        Attribute attribute = getAttribute( attributeValue.getAttribute().getUid() );

        if ( Objects.isNull( attribute ) || !attribute.getSupportedClasses().contains( object.getClass() ) )
        {
            return;
        }
        if ( attribute.isUnique() )
        {

            if (  !manager.isAttributeValueUnique( object.getClass(), object, attributeValue) )
            {
                throw new NonUniqueAttributeValueException( attributeValue );
            }
        }

        object.getAttributeValues().add( attributeValue );
        sessionFactory.getCurrentSession().save( object );
    }

    @Override
    @Transactional
    public <T extends IdentifiableObject> void deleteAttributeValue( T object, AttributeValue attributeValue )
    {
        object.getAttributeValues()
                .removeIf( a -> a.getAttribute() == attributeValue.getAttribute() );
        manager.update( object );
    }

    @Override
    @Transactional
    public <T extends IdentifiableObject> void deleteAttributeValues( T object, Set<AttributeValue> attributeValues )
    {
        object.getAttributeValues().removeAll( attributeValues );

        manager.update( object );
    }

    @Override
    @Transactional( readOnly = true )
    public <T extends IdentifiableObject> void generateAttributes( List<T> entityList )
    {
        entityList.forEach( entity -> entity.getAttributeValues()
            .forEach( attributeValue -> attributeValue.setAttribute( getAttribute( attributeValue.getAttribute().getUid() ) ) ) );
    }
}
