package com.mass3d.cache;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import com.mass3d.common.event.ApplicationCacheClearedEvent;
import org.springframework.context.event.EventListener;

@Slf4j
public class DefaultHibernateCacheManager
    implements HibernateCacheManager
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SessionFactory sessionFactory;

    public void setSessionFactory( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }

    // -------------------------------------------------------------------------
    // HibernateCacheManager implementation
    // -------------------------------------------------------------------------

    @Override
    public void clearObjectCache()
    {
        sessionFactory.getCache().evictEntityRegions();
        sessionFactory.getCache().evictCollectionRegions();
     }

    @Override
    public void clearQueryCache()
    {
        sessionFactory.getCache().evictDefaultQueryRegion();
        sessionFactory.getCache().evictQueryRegions();
    }

    @Override
    public void clearCache()
    {
        clearObjectCache();
        clearQueryCache();

        log.info( "Hibernate caches cleared" );
    }

    @Override
    @EventListener
    public void handleApplicationCachesCleared( ApplicationCacheClearedEvent event )
    {
        clearCache();
    }

    @Override
    public Statistics getStatistics()
    {
        return sessionFactory.getStatistics();
    }
}
