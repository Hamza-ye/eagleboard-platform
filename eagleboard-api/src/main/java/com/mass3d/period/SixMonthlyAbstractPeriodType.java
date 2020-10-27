package com.mass3d.period;

import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import com.mass3d.calendar.Calendar;
import com.mass3d.calendar.DateTimeUnit;

/**
 * Abstract class for SixMonthly period types, including those starting
 * at the beginning of the calendar year and those starting at the beginning
 * of other months.
 *
 */

public abstract class SixMonthlyAbstractPeriodType
    extends CalendarPeriodType
{
    private static final long serialVersionUID = -7135018015977806913L;

    public static final int FREQUENCY_ORDER = 182;

    public static final String SQL_INTERVAL = "6 months";

    // -------------------------------------------------------------------------
    // Abstract methods
    // -------------------------------------------------------------------------

    protected abstract int getBaseMonth();

    // -------------------------------------------------------------------------
    // PeriodType functionality
    // -------------------------------------------------------------------------

    @Override
    public Period createPeriod( DateTimeUnit dateTimeUnit, com.mass3d.calendar.Calendar calendar )
    {
        DateTimeUnit start = new DateTimeUnit( dateTimeUnit );

        int baseMonth = getBaseMonth();

        int year = start.getMonth() < baseMonth ? (start.getYear() - 1) : start.getYear();
        int month = start.getMonth() >= baseMonth && start.getMonth() < (baseMonth + 6) ? baseMonth : (baseMonth + 6);

        start.setYear( year );
        start.setMonth( month );
        start.setDay( 1 );

        DateTimeUnit end = new DateTimeUnit( start );
        end = calendar.plusMonths( end, 5 );
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
        return calendar.plusMonths( dateTimeUnit, offset * 6 );
    }

    /**
     * Generates six-monthly Periods for the whole year in which the given
     * Period's startDate exists.
     */
    @Override
    public List<Period> generatePeriods( DateTimeUnit dateTimeUnit )
    {
        Calendar cal = getCalendar();

        Period period = createPeriod( dateTimeUnit, cal );
        dateTimeUnit = createLocalDateUnitInstance( period.getStartDate(), cal );

        List<Period> periods = Lists.newArrayList();

        if ( dateTimeUnit.getMonth() == getBaseMonth() )
        {
            periods.add( period );
            periods.add( getNextPeriod( period ) );
        }
        else
        {
            periods.add( getPreviousPeriod( period ) );
            periods.add( period );
        }

        return periods;
    }

    /**
     * Generates the last 2 six-months where the last one is the six-month
     * which the given date is inside.
     */
    @Override
    public List<Period> generateRollingPeriods( Date date )
    {
        Period period = createPeriod( date );

        List<Period> periods = Lists.newArrayList();

        periods.add( getPreviousPeriod( period ) );
        periods.add( period );

        return periods;
    }

    @Override
    public List<Period> generateRollingPeriods( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        return generateRollingPeriods( calendar.toIso( dateTimeUnit ).toJdkDate() );
    }

    @Override
    public Date getRewindedDate( Date date, Integer rewindedPeriods )
    {
        Calendar cal = getCalendar();

        date = date != null ? date : new Date();
        rewindedPeriods = rewindedPeriods != null ? rewindedPeriods : 1;

        DateTimeUnit dateTimeUnit = createLocalDateUnitInstance( date );
        dateTimeUnit = cal.minusMonths( dateTimeUnit, rewindedPeriods * 6 );

        return cal.toIso( dateTimeUnit ).toJdkDate();
    }
}
