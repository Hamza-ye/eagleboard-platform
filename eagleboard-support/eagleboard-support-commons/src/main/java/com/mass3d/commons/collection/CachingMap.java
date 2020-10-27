package com.mass3d.commons.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * Map which allows storing a {@link Callable} and caches its return value on
 * the first call to get(Object, Callable). Subsequent calls returns the cached value.
 *
 */
public class CachingMap<K, V>
    extends HashMap<K, V>
{
    // -------------------------------------------------------------------------
    // Internal variables
    // -------------------------------------------------------------------------

    private long cacheHitCount;

    private long cacheMissCount;

    private long cacheLoadCount;

    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------

    /**
     * Returns the cached value if available or executes the {@link Callable} and returns
     * the value, which is also cached. Will not attempt to fetch values for null
     * keys, to avoid potentially expensive and pointless operations. Will cache
     * entries with null values.
     *
     * @param key the key.
     * @param callable the {@link Callable}.
     * @return the return value of the {@link Callable}, either from cache or immediate execution.
     */
    public V get( K key, Callable<V> callable )
    {
        if ( key == null )
        {
            return null;
        }

        V value = null;

        if ( super.containsKey( key ) )
        {
            value = super.get( key );

            cacheHitCount++;
        }
        else
        {
            try
            {
                value = callable.call();

                super.put( key, value );

                cacheMissCount++;
            }
            catch ( Exception ex )
            {
                throw new RuntimeException( ex );
            }
        }

        return value;
    }

    /**
     * Returns the cached value if available or executes the {@link Callable} and returns
     * the value, which is also cached. If the value produced, the default value
     * will be returned. Will not attempt to fetch values for null keys, to
     * avoid potentially expensive and pointless operations.
     *
     * @param key the key.
     * @param callable the {@link Callable}.
     * @param defaultValue the default value.
     * @return the return value of the {@link Callable}, either from cache or immediate execution.
     */
    public V get( K key, Callable<V> callable, V defaultValue )
    {
        V value = get( key, callable );

        return value != null ? value : defaultValue;
    }

    /**
     * Loads the cache with the given content. Entries for which the key is a
     * null reference are ignored.
     *
     * @param collection the content collection.
     * @param keyMapper the function to produce the cache key for a content item.
     * @return a reference to this caching map.
     */
    public CachingMap<K, V> load( Collection<V> collection, Function<V, K> keyMapper )
    {
        for ( V item : collection )
        {
            K key = keyMapper.apply( item );

            if ( key != null )
            {
                super.put( key, item );
            }
        }

        cacheLoadCount++;

        return this;
    }

    /**
     * Returns the number of cache hits from calling the {@code get} method.
     *
     * @return the number of cache hits.
     */
    public long getCacheHitCount()
    {
        return cacheHitCount;
    }

    /**
     * Returns the number of cache misses from calling the {@code get} method.
     *
     * @return the number of cache misses.
     */
    public long getCacheMissCount()
    {
        return cacheMissCount;
    }

    /**
     * Returns the ratio between cache hits and misses from calling the
     * {@code get} method.
     *
     * @return the cache hit versus miss ratio.
     */
    public double getCacheHitRatio()
    {
        return (double) cacheHitCount / (double) cacheMissCount;
    }

    /**
     * Returns the number of times the cache has been loaded.
     *
     * @return the number of times the cache has been loaded.
     */
    public long getCacheLoadCount()
    {
        return cacheLoadCount;
    }

    /**
     * Indicates whether the cache has been loaded at least one time.
     *
     * @return true if the cache has been loaded at least one time.
     */
    public boolean isCacheLoaded()
    {
        return cacheLoadCount > 0;
    }

    @Override
    public String toString()
    {
        return "[" +
            "Size: " + size() + ", " +
            "Hit count: " + cacheHitCount + ", " +
            "Miss count: " + cacheMissCount + ", " +
            "Hit ratio: " + getCacheHitRatio() + ", " +
            "Load count: " + cacheLoadCount +
            "]";
    }
}
