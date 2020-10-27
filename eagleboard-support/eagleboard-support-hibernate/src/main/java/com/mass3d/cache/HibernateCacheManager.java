package com.mass3d.cache;

import org.hibernate.stat.Statistics;
import com.mass3d.common.event.ApplicationCacheClearedEvent;

public interface HibernateCacheManager
{
    /**
     * Evicts all entities and collections from the cache.
     */
    void clearObjectCache();

    /**
     * Evicts all queries from the cache.
     */
    void clearQueryCache();

    /**
     * Evicts all entities, collections and queries from the cache.
     */
    void clearCache();

    /**
     * Gets the statistics.
     *
     * @return the statistics.
     */
    Statistics getStatistics();

    /**
     * Event handler for {@link ApplicationCacheClearedEvent}.
     *
     * @param event the {@link ApplicationCacheClearedEvent}.
     */
    void handleApplicationCachesCleared(ApplicationCacheClearedEvent event);
}
