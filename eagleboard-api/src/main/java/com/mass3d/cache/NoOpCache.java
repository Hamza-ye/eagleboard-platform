package com.mass3d.cache;

import static org.springframework.util.Assert.hasText;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

/**
 * A No operation implementation of {@link Cache}. The implementation will not
 * cache anything and can be used during system testing when caching has to be
 * disabled.
 * 
 */
public class NoOpCache<V> implements Cache<V>
{
    private V defaultValue;

    public NoOpCache( CacheBuilder<V> cacheBuilder )
    {
        this.defaultValue = cacheBuilder.getDefaultValue();
    }

    @Override
    public Optional<V> getIfPresent( String key )
    {
        return Optional.empty();
    }

    @Override
    public Optional<V> get( String key )
    {
        return Optional.ofNullable( defaultValue );
    }

    @Override
    public Optional<V> get( String key, Function<String, V> mappingFunction )
    {
        if ( null == mappingFunction )
        {
            throw new IllegalArgumentException( "MappingFunction cannot be null" );
        }
        return Optional.ofNullable( Optional.ofNullable( mappingFunction.apply( key ) ).orElse( defaultValue ) );
    }

    @Override
    public Collection<V> getAll()
    {
        return Sets.newHashSet();
    }

    @Override
    public void put( String key, V value )
    {
        if ( null == value )
        {
            throw new IllegalArgumentException( "Value cannot be null" );
        }
        // No operation
    }

    @Override
    public void put( String key, V value, long ttlInSeconds)
    {
        hasText( key, "Value cannot be null" );
        // No operation
    }

    @Override
    public void invalidate( String key )
    {
        // No operation
    }

    @Override
    public void invalidateAll()
    {
        // No operation
    }

    @Override
    public CacheType getCacheType()
    {
        return CacheType.NONE;
    }
}
