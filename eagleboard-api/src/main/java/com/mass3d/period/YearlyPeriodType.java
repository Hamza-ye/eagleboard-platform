package com.mass3d.period;

import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import com.mass3d.calendar.Calendar;
import com.mass3d.calendar.DateTimeUnit;

/**
 * PeriodType for yearly Periods. A valid yearly Period has startDate set to
 * January 1st and endDate set to the last day of the same year.
 *
 * @version $Id: YearlyPeriodType.java 2971 2007-03-03 18:54:56Z torgeilo $
 */
public class YearlyPeriodType
    extends CalendarPeriodType
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 3893035414025085437L;

    private static final String ISO_FORMAT = "yyyy";

    private static final String ISO8601_DURATION = "P1Y";

    /**
     * The name of the YearlyPeriodType, which is "Yearly".
     */
    public static final String NAME = "Yearly";

    public static final int FREQUENCY_ORDER = 365;

    public static final String SQL_INTERVAL = "1 year";

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
        DateTimeUnit end = new DateTimeUnit( dateTimeUnit );

        start.setDay( 1 );
        start.setMonth( 1 );

        end.setMonth( calendar.monthsInYear() );
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
        return calendar.plusYears( dateTimeUnit, offset );
    }

    /**
     * Generates yearly periods for the last 5, current and next 5 years.
     */
    @Override
    public List<Period> generatePeriods( DateTimeUnit dateTimeUnit )
    {
        Calendar calendar = getCalendar();

        dateTimeUnit = calendar.minusYears( dateTimeUnit, 5 );
        dateTimeUnit.setDay( 1 );
        dateTimeUnit.setMonth( 1 );

        List<Period> periods = Lists.newArrayList();

        for ( int i = 0; i < 11; ++i )
        {
            periods.add( createPeriod( dateTimeUnit, calendar ) );
            dateTimeUnit = calendar.plusYears( dateTimeUnit, 1 );
        }

        return periods;
    }

    /**
     * Generates the last 5 years where the last one is the year which the given
     * date is inside.
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

    /**
     * Generates the last 5 years where the last one is the year which the given
     * date is inside.
     */
    @Override
    public List<Period> generateLast5Years( Date date )
    {
        Calendar calendar = getCalendar();

        DateTimeUnit dateTimeUnit = createLocalDateUnitInstance( date );
        dateTimeUnit = calendar.minusYears( dateTimeUnit, 4 );
        dateTimeUnit.setDay( 1 );
        dateTimeUnit.setMonth( 1 );

        List<Period> periods = Lists.newArrayList();

        for ( int i = 0; i < 5; ++i )
        {
            periods.add( createPeriod( dateTimeUnit, calendar ) );
            dateTimeUnit = calendar.plusYears( dateTimeUnit, 1 );
        }

        return periods;
    }

    @Override
    public String getIsoDate( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        return String.valueOf( dateTimeUnit.getYear() );
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
        Calendar calendar = getCalendar();

        date = date != null ? date : new Date();
        rewindedPeriods = rewindedPeriods != null ? rewindedPeriods : 1;

        DateTimeUnit dateTimeUnit = createLocalDateUnitInstance( date );
        dateTimeUnit = calendar.minusYears( dateTimeUnit, rewindedPeriods );

        return calendar.toIso( dateTimeUnit ).toJdkDate();
    }
}
