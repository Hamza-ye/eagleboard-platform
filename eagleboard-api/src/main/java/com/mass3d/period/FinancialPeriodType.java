package com.mass3d.period;

import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import com.mass3d.calendar.Calendar;
import com.mass3d.calendar.DateTimeUnit;

public abstract class FinancialPeriodType
    extends CalendarPeriodType
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 2649990007010207631L;

    public static final int FREQUENCY_ORDER = 365;

    public static final String SQL_INTERVAL = "1 year";

    // -------------------------------------------------------------------------
    // Abstract methods
    // -------------------------------------------------------------------------

    public abstract int getBaseMonth();

    // -------------------------------------------------------------------------
    // PeriodType functionality
    // -------------------------------------------------------------------------

    @Override
    public Period createPeriod( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        boolean past = dateTimeUnit.getMonth() >= (getBaseMonth() + 1);

        if ( !past )
        {
            dateTimeUnit = calendar.minusYears( dateTimeUnit, 1 );
        }

        dateTimeUnit.setMonth( getBaseMonth() + 1 );
        dateTimeUnit.setDay( 1 );

        DateTimeUnit start = new DateTimeUnit( dateTimeUnit );
        DateTimeUnit end = new DateTimeUnit( dateTimeUnit );

        end = calendar.plusYears( end, 1 );
        end = calendar.minusDays( end, 1 );

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
    public DateTimeUnit getDateWithOffset(  DateTimeUnit dateTimeUnit, int offset, Calendar calendar )
    {
        return calendar.plusYears( dateTimeUnit, offset );
    }

    /**
     * Generates financial yearly periods for the last 5, current and next 5
     * financial years.
     */
    @Override
    public List<Period> generatePeriods( DateTimeUnit dateTimeUnit )
    {
        Calendar cal = getCalendar();

        boolean past = dateTimeUnit.getMonth() >= (getBaseMonth() + 1);

        List<Period> periods = Lists.newArrayList();

        dateTimeUnit = cal.minusYears( dateTimeUnit, past ? 5 : 6 );
        dateTimeUnit.setMonth( getBaseMonth() + 1 );
        dateTimeUnit.setDay( 1 );

        Calendar calendar = getCalendar();

        for ( int i = 0; i < 11; i++ )
        {
            periods.add( createPeriod( dateTimeUnit, cal ) );
            dateTimeUnit = calendar.plusYears( dateTimeUnit, 1 );
        }

        return periods;
    }

    /**
     * Generates the last 5 financial years where the last one is the financial
     * year which the given date is inside.
     */
    @Override
    public List<Period> generateRollingPeriods( Date date )
    {
        return generateLast5Years( date );
    }

    @Override
    public List<Period> generateRollingPeriods( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        return generateLast5Years( calendar.toIso( dateTimeUnit ).toJdkDate() );
    }

    @Override
    public List<Period> generateLast5Years( Date date )
    {
        Calendar cal = getCalendar();

        DateTimeUnit dateTimeUnit = createLocalDateUnitInstance( date, cal );
        boolean past = dateTimeUnit.getMonth() >= (getBaseMonth() + 1);

        List<Period> periods = Lists.newArrayList();

        dateTimeUnit = cal.minusYears( dateTimeUnit, past ? 4 : 5 );
        dateTimeUnit.setMonth( getBaseMonth() + 1 );
        dateTimeUnit.setDay( 1 );

        for ( int i = 0; i < 5; i++ )
        {
            periods.add( createPeriod( dateTimeUnit, cal ) );
            dateTimeUnit = cal.plusYears( dateTimeUnit, 1 );
        }

        return periods;
    }

    @Override
    public Date getRewindedDate( Date date, Integer rewindedPeriods )
    {
        Calendar cal = getCalendar();

        date = date != null ? date : new Date();
        rewindedPeriods = rewindedPeriods != null ? rewindedPeriods : 1;

        DateTimeUnit dateTimeUnit = createLocalDateUnitInstance( date, cal );
        dateTimeUnit = cal.minusYears( dateTimeUnit, rewindedPeriods );

        return cal.toIso( dateTimeUnit ).toJdkDate();
    }

    @Override
    public boolean spansMultipleCalendarYears()
    {
        return true;
    }
}
