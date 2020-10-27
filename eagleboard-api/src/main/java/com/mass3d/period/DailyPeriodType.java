package com.mass3d.period;

import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import com.mass3d.calendar.Calendar;
import com.mass3d.calendar.DateTimeUnit;

/**
 * PeriodType for daily Periods. A valid daily Period has equal startDate and
 * endDate.
 *
 */
public class DailyPeriodType
    extends CalendarPeriodType
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 5371766471215556241L;

    public static final String ISO_FORMAT = "yyyyMMdd";

    private static final String ISO8601_DURATION = "P1D";

    /**
     * The name of the DailyPeriodType, which is "Daily".
     */
    public static final String NAME = "Daily";

    public static final int FREQUENCY_ORDER = 1;

    public static final String SQL_INTERVAL = "1 day";

    // -------------------------------------------------------------------------
    // PeriodType functionality
    // -------------------------------------------------------------------------

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public Period createPeriod( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        return toIsoPeriod( dateTimeUnit, dateTimeUnit, calendar );
    }

    @Override
    public int getFrequencyOrder()
    {
        return FREQUENCY_ORDER;
    }

    @Override
    public String getSqlInterval()
    {
        return SQL_INTERVAL;
    }

    // -------------------------------------------------------------------------
    // CalendarPeriodType functionality
    // -------------------------------------------------------------------------

    @Override
    public DateTimeUnit getDateWithOffset( DateTimeUnit dateTimeUnit, int offset, Calendar calendar )
    {
        return calendar.plusDays( dateTimeUnit, offset );
    }

    /**
     * Generates daily Periods for the whole year in which the given Period's
     * startDate exists.
     */
    @Override
    public List<Period> generatePeriods( DateTimeUnit dateTimeUnit )
    {
        dateTimeUnit.setMonth( 1 );
        dateTimeUnit.setDay( 1 );

        List<Period> periods = Lists.newArrayList();

        int year = dateTimeUnit.getYear();

        Calendar calendar = getCalendar();

        while ( year == dateTimeUnit.getYear() )
        {
            periods.add( createPeriod( dateTimeUnit, calendar ) );
            dateTimeUnit = calendar.plusDays( dateTimeUnit, 1 );
        }

        return periods;
    }

    /**
     * Generates the last 365 days where the last one is the day of the given
     * date.
     */
    @Override
    public List<Period> generateRollingPeriods( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        Calendar cal = getCalendar();

        DateTimeUnit iterationDateTimeUnit = cal.minusDays( dateTimeUnit, 364 );

        List<Period> periods = Lists.newArrayList();

        for ( int i = 0; i < 365; i++ )
        {
            periods.add( createPeriod( iterationDateTimeUnit, calendar ) );
            iterationDateTimeUnit = cal.plusDays( iterationDateTimeUnit, 1 );
        }

        return periods;
    }

    @Override
    public String getIsoDate( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        return String
            .format( "%d%02d%02d", dateTimeUnit.getYear(), dateTimeUnit.getMonth(), dateTimeUnit.getDay() );
    }

    @Override
    public String getIsoFormat()
    {
        return ISO_FORMAT;
    }

    @Override
    public String getIso8601Duration()
    {
        return ISO8601_DURATION;
    }

    @Override
    public Date getRewindedDate( Date date, Integer rewindedPeriods )
    {
        Calendar cal = getCalendar();

        date = date != null ? date : new Date();
        rewindedPeriods = rewindedPeriods != null ? rewindedPeriods : 1;

        DateTimeUnit dateTimeUnit = createLocalDateUnitInstance( date, cal );
        dateTimeUnit = cal.minusDays( dateTimeUnit, rewindedPeriods );

        return cal.toIso( dateTimeUnit ).toJdkDate();
    }
}
