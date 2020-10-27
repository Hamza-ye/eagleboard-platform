package com.mass3d.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class TestCache<V>
    implements
    Cache<V>
{

    private Map<String, V> mapCache = new HashMap<>();

    @Override
    public Optional<V> getIfPresent( String key )
    {
        if ( mapCache.containsKey( key ) )
        {
            return get( key );
        }
        else
        {
            return Optional.empty();
        }
    }

    @Override
    public Optional<V> get( String key )
    {
        return Optional.ofNullable( mapCache.get( key ) );
    }

    @Override
    public Optional<V> get( String key, Function<String, V> mappingFunction )
    {
        return Optional.empty();
    }

    @Override
    public Collection<V> getAll()
    {
        return mapCache.values();
    }

    @Override
    public void put( String key, V value )
    {
        mapCache.put( key, value );
    }

    @Override
    public void put( String key, V value, long ttlInSeconds)
    {
        // Ignoring ttl for this testing cache
        mapCache.put( key, value );
    }

    @Override
    public void invalidate( String key )
    {
        mapCache.remove( key );
    }

    @Override
    public void invalidateAll()
    {
        mapCache = new HashMap<>();
    }

    @Override
    public CacheType getCacheType()
    {
        return null;
    }
}
