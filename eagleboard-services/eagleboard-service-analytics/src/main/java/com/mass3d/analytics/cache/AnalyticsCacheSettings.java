package com.mass3d.analytics.cache;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Math.max;
import static com.mass3d.analytics.AnalyticsCacheTtlMode.FIXED;
import static com.mass3d.analytics.AnalyticsCacheTtlMode.PROGRESSIVE;
import static com.mass3d.common.cache.CacheStrategy.NO_CACHE;
import static com.mass3d.setting.SettingKey.ANALYTICS_CACHE_PROGRESSIVE_TTL_FACTOR;
import static com.mass3d.setting.SettingKey.ANALYTICS_CACHE_TTL_MODE;
import static com.mass3d.setting.SettingKey.CACHE_STRATEGY;

import java.util.Date;
import com.mass3d.analytics.AnalyticsCacheTtlMode;
import com.mass3d.common.cache.CacheStrategy;
import com.mass3d.setting.SystemSettingManager;
import org.springframework.stereotype.Component;

/**
 * Holds the configuration settings for the analytics caching.
 */
@Component
public class AnalyticsCacheSettings
{
    private final SystemSettingManager systemSettingManager;

    public AnalyticsCacheSettings( final SystemSettingManager systemSettingManager )
    {
        checkNotNull( systemSettingManager );
        this.systemSettingManager = systemSettingManager;
    }

    /**
     * Returns true if the analytics cache mode, at application level, is set to
     * PROGRESSIVE. If enabled, it overrides the fixed predefined settings.
     * 
     * @see AnalyticsCacheTtlMode#PROGRESSIVE
     * 
     * @return true if the current cache is enabled and set to PROGRESSIVE, false
     *         otherwise.
     */
    public boolean isProgressiveCachingEnabled()
    {
        final AnalyticsCacheTtlMode analyticsCacheMode = (AnalyticsCacheTtlMode) systemSettingManager
            .getSystemSetting( ANALYTICS_CACHE_TTL_MODE );

        return PROGRESSIVE == analyticsCacheMode;
    }

    /**
     * Returns true if the analytics cache mode, at application level, is correctly
     * set to FIXED.
     * 
     * @see AnalyticsCacheTtlMode#FIXED
     * 
     * @return true if the current cache mode is set to FIXED, false otherwise.
     */
    public boolean isFixedCachingEnabled()
    {
        final AnalyticsCacheTtlMode analyticsCacheMode = (AnalyticsCacheTtlMode) systemSettingManager
            .getSystemSetting( ANALYTICS_CACHE_TTL_MODE );

        final CacheStrategy cacheStrategy = (CacheStrategy) systemSettingManager.getSystemSetting( CACHE_STRATEGY );

        return FIXED == analyticsCacheMode && cacheStrategy != null && cacheStrategy.hasExpirationTimeSet();
    }

    /**
     * Encapsulates the calculation of the progressive expiration time for the
     * analytics caching at application level, if the PROGRESSIVE mode is set.
     * 
     * @param dateBeforeToday the date to be used during the calculation of the
     *        progressive expiration time.
     * 
     * @return the expiration time computed based on the given "dateBeforeToday".
     */
    public long progressiveExpirationTimeOrDefault( final Date dateBeforeToday )
    {
        return new TimeToLive( dateBeforeToday, getProgressiveTtlFactorOrDefault() ).compute();
    }

    /**
     * Retrieves the expiration time in seconds based on the system settings defined
     * by the {@link com.mass3d.setting.SettingKey#CACHE_STRATEGY}
     * 
     * @see CacheStrategy
     * 
     * @return the predefined expiration time set or 0 (ZERO) if nothing is set.
     */
    public long fixedExpirationTimeOrDefault()
    {
        final CacheStrategy cacheStrategy = (CacheStrategy) systemSettingManager.getSystemSetting( CACHE_STRATEGY );

        if ( cacheStrategy != null && cacheStrategy.hasExpirationTimeSet() )
        {
            return cacheStrategy.toSeconds();
        }
        else
        {
            // Try to get a default value
            final CacheStrategy defaultExpirationTime = (CacheStrategy) CACHE_STRATEGY.getDefaultValue();

            if ( defaultExpirationTime.hasExpirationTimeSet() )
            {
                return defaultExpirationTime.toSeconds();
            }
            else
            {
                // Return ZERO (always expire)
                return NO_CACHE.toSeconds();
            }
        }
    }

    /**
     * Checks if any the caching feature (PROGRESSIVE or FIXED) is enabled.
     *
     * @return true if there is any expiration time set, false otherwise.
     */
    public boolean isCachingEnabled()
    {
        return isFixedCachingEnabled() || isProgressiveCachingEnabled();
    }

    /**
     * Returns the TTL factor set in system settings or 1 (when the factor is set to
     * ZERO or negative).
     *
     * @return the ttl factor
     */
    private int getProgressiveTtlFactorOrDefault()
    {
        final Integer ttlFactor = (Integer) systemSettingManager
            .getSystemSetting( ANALYTICS_CACHE_PROGRESSIVE_TTL_FACTOR );

        return max( ttlFactor, 1 );
    }
}
