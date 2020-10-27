package com.mass3d.common.cache;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static com.mass3d.util.DateUtils.getSecondsUntilTomorrow;

/**
 * CacheStrategies express web request caching settings.
 * Note that {@link #RESPECT_SYSTEM_SETTING} should only be used on a
 * per-object-basis (i.e. never as a system wide setting).
 *
 */
public enum CacheStrategy
{
    NO_CACHE,
    CACHE_1_MINUTE,
    CACHE_5_MINUTES,
    CACHE_10_MINUTES,
    CACHE_15_MINUTES,
    CACHE_30_MINUTES,
    CACHE_1_HOUR,
    CACHE_6AM_TOMORROW,
    CACHE_TWO_WEEKS,
    RESPECT_SYSTEM_SETTING;

    public Long toSeconds()
    {
        switch ( this )
        {
        case CACHE_1_MINUTE:
            return MINUTES.toSeconds( 1 );
        case CACHE_5_MINUTES:
            return MINUTES.toSeconds( 5 );
        case CACHE_10_MINUTES:
            return MINUTES.toSeconds( 10 );
        case CACHE_15_MINUTES:
            return MINUTES.toSeconds( 15 );
        case CACHE_30_MINUTES:
            return MINUTES.toSeconds( 30 );
        case CACHE_1_HOUR:
            return HOURS.toSeconds( 1 );
        case CACHE_TWO_WEEKS:
            return DAYS.toSeconds( 14 );
        case CACHE_6AM_TOMORROW:
            return getSecondsUntilTomorrow( 6 );
        case NO_CACHE:
            return 0l;
        case RESPECT_SYSTEM_SETTING:
        default:
            throw new UnsupportedOperationException();
        }
    }

    public boolean hasExpirationTimeSet()
    {
        return this != NO_CACHE && this != RESPECT_SYSTEM_SETTING;
    }
}
