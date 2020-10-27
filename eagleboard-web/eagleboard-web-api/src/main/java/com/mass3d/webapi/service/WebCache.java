package com.mass3d.webapi.service;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.concurrent.TimeUnit.SECONDS;
import static com.mass3d.common.cache.CacheStrategy.NO_CACHE;
import static com.mass3d.common.cache.CacheStrategy.RESPECT_SYSTEM_SETTING;
import static com.mass3d.common.cache.Cacheability.PRIVATE;
import static com.mass3d.common.cache.Cacheability.PUBLIC;
import static com.mass3d.setting.SettingKey.CACHEABILITY;
import static com.mass3d.setting.SettingKey.CACHE_STRATEGY;
import static org.springframework.http.CacheControl.maxAge;
import static org.springframework.http.CacheControl.noCache;
import com.mass3d.analytics.cache.AnalyticsCacheSettings;
import com.mass3d.common.cache.CacheStrategy;
import com.mass3d.common.cache.Cacheability;
import com.mass3d.setting.SystemSettingManager;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * This component encapsulates the caching settings and object definitions
 * related to the caching at the HTTP level.
 */
@Component
public class WebCache
{
    private final SystemSettingManager systemSettingManager;

    private final AnalyticsCacheSettings analyticsCacheSettings;

    public WebCache( final SystemSettingManager systemSettingManager, AnalyticsCacheSettings analyticsCacheSettings)
    {
        checkNotNull( systemSettingManager );
        checkNotNull( analyticsCacheSettings );

        this.systemSettingManager = systemSettingManager;
        this.analyticsCacheSettings = analyticsCacheSettings;
    }

    /**
     * Defines and return a CacheControl object with the correct expiration time and
     * cacheability based on the internal system settings defined by the user. The
     * expiration time is defined through the Enum {@link CacheStrategy}
     * 
     * @param cacheStrategy
     *
     * @return a CacheControl object configured based on current system settings.
     */
    public CacheControl getCacheControlFor( CacheStrategy cacheStrategy )
    {
        final CacheControl cacheControl;

        if ( RESPECT_SYSTEM_SETTING == cacheStrategy )
        {
            cacheStrategy = (CacheStrategy) systemSettingManager.getSystemSetting( CACHE_STRATEGY );
        }

        final boolean cacheStrategyHasExpirationTimeSet = cacheStrategy != null && cacheStrategy != NO_CACHE;

        if ( cacheStrategyHasExpirationTimeSet )
        {
            cacheControl = maxAge( cacheStrategy.toSeconds(), SECONDS );

            setCacheabilityFor( cacheControl );
        }
        else
        {
            cacheControl = noCache();
        }

        return cacheControl;
    }

    /**
     * Defines and return a CacheControl object with the correct expiration time and
     * cacheability, based on a provided date, in SECONDS.
     *
     * @param latestEndDate
     *
     * @return a CacheControl object configured based on current cacheability
     *         settings and the provided time to live.
     */
    public CacheControl getCacheControlFor( final Date latestEndDate )
    {
        final CacheControl cacheControl;

        final long timeToLive = analyticsCacheSettings.progressiveExpirationTimeOrDefault( latestEndDate );

        if ( timeToLive > 0 )
        {
            cacheControl = maxAge( timeToLive, SECONDS );

            setCacheabilityFor( cacheControl );
        }
        else
        {
            cacheControl = noCache();
        }

        return cacheControl;
    }

    /**
     * See {@link AnalyticsCacheSettings#isProgressiveCachingEnabled()}
     *
     * @return true if progressive caching is enabled, false otherwise
     */
    public boolean isProgressiveCachingEnabled() {
        return analyticsCacheSettings.isProgressiveCachingEnabled();
    }

    /**
     * Sets the cacheability (defined as system setting) into the given
     * CacheControl.
     * 
     * @see com.mass3d.setting.SettingKey#CACHEABILITY
     * 
     * @param cacheControl where cacheability will be set.
     */
    private void setCacheabilityFor( final CacheControl cacheControl )
    {
        final Cacheability cacheability = (Cacheability) systemSettingManager.getSystemSetting( CACHEABILITY );

        if ( PUBLIC == cacheability )
        {
            cacheControl.cachePublic();
        }
        else if ( PRIVATE == cacheability )
        {
            cacheControl.cachePrivate();
        }
    }
}
