package com.mass3d.cache;

import java.util.Map;

/**
 * Provides cache builder to build instances.
 *
 */
public interface CacheProvider
{
    /**
     * Creates a new {@link ExtendedCacheBuilder} that can be used to build a cache that
     * stores the valueType specified.
     *
     * @param valueType The class type of values to be stored in cache.
     * @return A cache builder instance for the specified value type. Returns a
     *          {@link ExtendedCacheBuilder}.
     */
    <V> CacheBuilder<V> newCacheBuilder(Class<V> valueType);

    /**
     * Creates a new {@link ExtendedCacheBuilder} that can be used to build a cache that
     * stores the Map of keyType and valueType specified.
     *
     * @param valueType The class type of values to be stored in cache.
     * @return A cache builder instance for the specified value type. Returns a
     *          {@link ExtendedCacheBuilder}.
     */
    <K,V> ExtendedCacheBuilder<Map<K,V>> newCacheBuilder(Class<K> keyType, Class<V> valueType);
}
