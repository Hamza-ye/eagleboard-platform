package com.mass3d.period;

import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import com.mass3d.calendar.Calendar;
import com.mass3d.calendar.DateTimeUnit;

/**
 * PeriodType for quarterly Periods. A valid quarterly Period has startDate set
 * to the first day of a calendar quarter, and endDate set to the last day of
 * the same quarter.
 *
 * @version $Id: QuarterlyPeriodType.java 2971 2007-03-03 18:54:56Z torgeilo $
 */
public class QuarterlyPeriodType
    extends CalendarPeriodType
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = -5973809094923012052L;

    private static final String ISO_FORMAT = "yyyyQn";

    private static final String ISO8601_DURATION = "P3M";

    private static final String ISO_CALENDAR_NAME = com.mass3d.calendar.impl.Iso8601Calendar.getInstance().name();

    /**
     * The name of the QuarterlyPeriodType, which is "Quarterly".
     */
    public static final String NAME = "Quarterly";

    public static final int FREQUENCY_ORDER = 91;

    public static final String SQL_INTERVAL = "3 months";

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

        start.setMonth( ((dateTimeUnit.getMonth() - 1) - ((dateTimeUnit.getMonth() - 1) % 3)) + 1 );
        start.setDay( 1 );

        if ( start.getMonth() > 12 )
        {
            start.setYear( start.getYear() + 1 );
            start.setMonth( 1 );
        }

        DateTimeUnit end = new DateTimeUnit( start );
        end = calendar.plusMonths( end, 2 );
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
        return calendar.plusMonths( dateTimeUnit, offset * 3 );
    }

    /**
     * Generates quarterly Periods for the whole year in which the given
     * Period's startDate exists.
     */
    @Override
    public List<Period> generatePeriods( DateTimeUnit dateTimeUnit )
    {
        com.mass3d.calendar.Calendar cal = getCalendar();

        dateTimeUnit.setMonth( 1 );
        dateTimeUnit.setDay( 1 );

        int year = dateTimeUnit.getYear();
        List<Period> periods = Lists.newArrayList();

        while ( year == dateTimeUnit.getYear() )
        {
            periods.add( createPeriod( dateTimeUnit, cal ) );
            dateTimeUnit = cal.plusMonths( dateTimeUnit, 3 );
        }

        return periods;
    }

    /**
     * Generates the last 4 quarters where the last one is the quarter which the
     * given date is inside.
     */
    @Override
    public List<Period> generateRollingPeriods( Date date )
    {
        date = createPeriod( date ).getStartDate();

        return generateRollingPeriods( createLocalDateUnitInstance( date ), getCalendar() );
    }

    @Override
    public List<Period> generateRollingPeriods( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        dateTimeUnit.setDay( 1 );

        DateTimeUnit iterationDateTimeUnit = calendar.minusMonths( dateTimeUnit, 9 );

        List<Period> periods = Lists.newArrayList();

        for ( int i = 0; i < 4; i++ )
        {
            periods.add( createPeriod( iterationDateTimeUnit, calendar ) );
            iterationDateTimeUnit = calendar.plusMonths( iterationDateTimeUnit, 3 );
        }

        return periods;
    }

    @Override
    public String getIsoDate( DateTimeUnit dateTimeUnit, Calendar calendar )
    {
        DateTimeUnit newUnit = dateTimeUnit;
        
        if ( !calendar.name().equals( ISO_CALENDAR_NAME ) && newUnit.isIso8601() )
        {
            newUnit = calendar.fromIso( newUnit );
        }

        switch ( newUnit.getMonth() )
        {
            case 1:
                return newUnit.getYear() + "Q1";
            case 4:
                return newUnit.getYear() + "Q2";
            case 7:
                return newUnit.getYear() + "Q3";
            case 10:
                return newUnit.getYear() + "Q4";
            default:
                throw new IllegalArgumentException( "Month not valid [1,4,7,10], was given " + dateTimeUnit.getMonth() );
        }
    }

    /**
     * n refers to the quarter, can be [1-4].
     */
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

        DateTimeUnit dateTimeUnit = createLocalDateUnitInstance( date );
        dateTimeUnit = cal.minusMonths( dateTimeUnit, rewindedPeriods * 3 );

        return cal.toIso( dateTimeUnit ).toJdkDate();
    }
}
