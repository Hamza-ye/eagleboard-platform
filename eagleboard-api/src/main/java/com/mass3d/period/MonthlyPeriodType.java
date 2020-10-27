package com.mass3d.period;

import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import com.mass3d.calendar.Calendar;
import com.mass3d.calendar.DateTimeUnit;

/**
 * PeriodType for monthly Periods. A valid monthly Period has startDate set to
 * the first day of a calendar month, and endDate set to the last day of the
 * same month.
 *
 * @version $Id: MonthlyPeriodType.java 2971 2007-03-03 18:54:56Z torgeilo $
 */
public class MonthlyPeriodType
    extends CalendarPeriodType
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = -6920058214699654387L;

    private static final String ISO_FORMAT = "yyyyMM";

    private static final String ISO8601_DURATION = "P1M";

    /**
     * The name of the MonthlyPeriodType, which is "Monthly".
     */
    public static final String NAME = "Monthly";

    public static final int FREQUENCY_ORDER = 30;

    public static final String SQL_INTERVAL = "1 month";

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
        DateTimeUnit start = new DateTimeUnit( dateTimeUnit );
        start.setDay( 1 );

        DateTimeUnit end = new DateTimeUnit( dateTimeUnit );
        end.setDay( calendar.daysInMonth( end.getYear(), end.getMonth() ) );

        return toIsoPeriod( start, end, calendar );
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
        return calendar.plusMonths( dateTimeUnit, offset );
    }

    /**
     * Generates monthly Periods for the whole year in which the given Period's
     * startDate exists.
     */
    @Override
    public List<Period> generatePeriods( DateTimeUnit dateTimeUnit )
    {
        Calendar cal = getCalendar();

        dateTimeUnit.setMonth( 1 );
        dateTimeUnit.setDay( 1 );

        List<Period> periods = Lists.newArrayList();

        int year = dateTimeUnit.getYear();

        while ( dateTimeUnit.getYear() == year )
        {
            periods.add( createPeriod( dateTimeUnit, cal ) );
            dateTimeUnit = cal.plusMonths( dateTimeUnit, 1 );
        }

        return periods;
    }

    /**
     * Generates the last 12 months where the last one is the month which the
     * given date is inside.
     */
    @Override
    public List<Period> generateRollingPeriods( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        dateTimeUnit.setDay( 1 );
        DateTimeUnit iterationDateTimeUnit = calendar.minusMonths( dateTimeUnit, 11 );

        List<Period> periods = Lists.newArrayList();

        for ( int i = 0; i < 12; i++ )
        {
            periods.add( createPeriod( iterationDateTimeUnit, calendar ) );
            iterationDateTimeUnit = calendar.plusMonths( iterationDateTimeUnit, 1 );
        }

        return periods;
    }

    @Override
    public String getIsoDate( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        return String.format( "%d%02d", dateTimeUnit.getYear(), dateTimeUnit.getMonth() );
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

        DateTimeUnit dateTimeUnit = cal.fromIso( DateTimeUnit.fromJdkDate( date ) );
        dateTimeUnit = cal.minusMonths( dateTimeUnit, rewindedPeriods );

        return cal.toIso( dateTimeUnit ).toJdkDate();
    }
}
