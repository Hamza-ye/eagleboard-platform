package com.mass3d.common.cache;

/**
 * Implementers of this interface gain the cacheStrategy property which allows
 * specifically setting and persisting the CacheStrategy for the object.
 * The chosen CacheStrategy should be honored on a per-object-basis for any Cacheable
 * and will ultimately decide the cache parameters of any web request returning the object.
 *
 */
public interface Cacheable
{
    CacheStrategy DEFAULT_CACHE_STRATEGY = CacheStrategy.RESPECT_SYSTEM_SETTING;

    /**
     * Returns the CacheStrategy for this Cacheable. Should never return null.
     *
     * @return the CacheStrategy of this object.
     */
    CacheStrategy getCacheStrategy();
}
