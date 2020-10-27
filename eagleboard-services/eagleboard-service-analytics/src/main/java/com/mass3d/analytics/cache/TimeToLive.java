package com.mass3d.analytics.cache;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import java.time.Instant;
import java.util.Date;

/**
 * This class is responsible for computing a time to live value based on the
 * given date before today and a TTL factor. The calculation of the TTL will be
 * done by the method compute() - check this method for the calculation details.
 */
public class TimeToLive
    implements
    Computable
{

    static final long DEFAULT_MULTIPLIER = 1;

    private final Date dateBeforeToday;

    private final int ttlFactor;

    public TimeToLive( final Date dateBeforeToday, final int ttlFactor )
    {
        notNull( dateBeforeToday, "Param dateBeforeToday must not be null" );
        isTrue( ttlFactor > 0, "Param ttlFactor must be greater than zero" );

        this.dateBeforeToday = dateBeforeToday;
        this.ttlFactor = ttlFactor;
    }

    /**
     * Execute the internal rules in order to calculate a TTL for the given
     * parameters. The current rules are based on a configurable timeout "ttlFactor"
     * (through SettingKey) which will be used in the calculation of this time to
     * live. Basically:
     *
     * Older the "dateBeforeToday", higher the "ttlFactor", longer the TTL. The
     * formula is basically: TTL = "ttlFactor" * (diff between now and the
     * "dateBeforeToday")
     *
     * @return the computed TTL value.
     */
    @Override
    public long compute()
    {
        /*
         * If the difference between the most recent date and NOW is 0 (zero) it means
         * the current day, so set the days multiplier to 1 (one) avoiding multiplying
         * by 0 (zero).
         */
        final long daysDiff = daysBetweenDateBeforeTodayAndNow( dateBeforeToday.toInstant() );
        final long daysMultiplier = daysDiff > 0 ? daysDiff : DEFAULT_MULTIPLIER;

        return ttlFactor * daysMultiplier;
    }

    /**
     * Calculates the difference between now and the given date. It has a the
     * particularity of returning ZERO (0) if the diff is negative (because it means
     * that the input date is ahead of now).
     *
     * @param dateBeforeToday the date to subtract from now
     * @return the difference of days
     */
    private long daysBetweenDateBeforeTodayAndNow( final Instant dateBeforeToday )
    {
        final long diff = DAYS.between( ofInstant( dateBeforeToday, systemDefault() ), now() );
        return diff >= 0 ? diff : 0;
    }
}
