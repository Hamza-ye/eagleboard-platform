package com.mass3d.cache;

import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.util.Assert.hasText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import org.cache2k.Cache2kBuilder;

/**
 * Local cache implementation of {@link Cache}. This implementation is backed by
 * Caffeine library which uses an in memory Map implementation.
 *
 */
public class LocalCache<V> implements Cache<V>
{
    private org.cache2k.Cache<String, V> cache2kInstance;

    private V defaultValue;

    /**
     * Constructor to instantiate LocalCache object.
     *
     * @param cacheBuilder CacheBuilder instance
     */
    @SuppressWarnings("unchecked")
    public LocalCache( final CacheBuilder<V> cacheBuilder )
    {
        Cache2kBuilder<?, ?> builder = Cache2kBuilder.forUnknownTypes();

        if ( cacheBuilder.isExpiryEnabled() )
        {
            builder.eternal( false );
            if ( cacheBuilder.isRefreshExpiryOnAccess() )
            {
                // TODO https://github.com/cache2k/cache2k/issues/39 is still
                // Open. Once the issue is resolved it can be updated here
                builder.expireAfterWrite( cacheBuilder.getExpiryInSeconds(), SECONDS );
            }
            else
            {
                builder.expireAfterWrite( cacheBuilder.getExpiryInSeconds(), SECONDS );
            }
        }
        else
        {
            builder.eternal( true );
        }
        if ( cacheBuilder.getMaximumSize() > 0 )
        {
            builder.entryCapacity( cacheBuilder.getMaximumSize() );
        }

        // Using unknown typed key for builder and casting it
        this.cache2kInstance = (org.cache2k.Cache<String, V>) builder.build();
        this.defaultValue = cacheBuilder.getDefaultValue();
    }

    @Override
    public Optional<V> getIfPresent( String key )
    {
        return Optional.ofNullable( cache2kInstance.get( key ) );
    }

    @Override
    public Optional<V> get( String key )
    {
        return Optional.ofNullable( Optional.ofNullable( cache2kInstance.get( key ) ).orElse( defaultValue ) );
    }

    @Override
    public Optional<V> get( String key, Function<String, V> mappingFunction )
    {
        if ( null == mappingFunction )
        {
            throw new IllegalArgumentException( "MappingFunction cannot be null" );
        }

        V value = cache2kInstance.get( key );

        if ( value == null )
        {
            value = mappingFunction.apply( key );

            if ( value != null )
            {
                cache2kInstance.put( key, value );
            }
        }

        return Optional.ofNullable( Optional.ofNullable( value ).orElse( defaultValue ) );
    }

    @Override
    public Collection<V> getAll()
    {
        return new ArrayList<V>( cache2kInstance.asMap().values() );
    }

    @Override
    public void put( String key, V value )
    {
        if ( null == value )
        {
            throw new IllegalArgumentException( "Value cannot be null" );
        }
        cache2kInstance.put( key, value );
    }

    @Override
    public void put( String key, V value, long ttlInSeconds )
    {
        hasText( key, "Value cannot be null" );
        cache2kInstance.invoke( key,
            e -> e.setValue( value ).setExpiryTime( currentTimeMillis() + SECONDS.toMillis( ttlInSeconds ) ) );
    }

    @Override
    public void invalidate( String key )
    {
        cache2kInstance.remove( key );
    }

    @Override
    public void invalidateAll()
    {
        cache2kInstance.clear();
    }

    @Override
    public CacheType getCacheType()
    {
        return CacheType.IN_MEMORY;
    }
}
