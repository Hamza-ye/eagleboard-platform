package com.mass3d.period;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.mass3d.calendar.DateTimeUnit;

/**
 * PeriodType for two-yearly Periods. A valid two-yearly Period has startDate
 * set to January 1st on an even year (2000, 2002, 2004, etc), and endDate set
 * to the last day of the next year.
 *
 * @version $Id: TwoYearlyPeriodType.java 2975 2007-03-03 22:24:36Z torgeilo $
 */
public class TwoYearlyPeriodType
    extends CalendarPeriodType
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 5813755542990991810L;

    /**
     * The name of the TwoYearlyPeriodType, which is "TwoYearly".
     */
    public static final String NAME = "TwoYearly";
    
    private static final String ISO8601_DURATION = "P2Y";

    public static final int FREQUENCY_ORDER = 730;

    public static final String SQL_INTERVAL = "2 years";

    // -------------------------------------------------------------------------
    // PeriodType functionality
    // -------------------------------------------------------------------------

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public Period createPeriod( Calendar cal )
    {
        //TODO: use local calendar instead of hard coded Gregorian
        cal.set( Calendar.YEAR, cal.get( Calendar.YEAR ) - cal.get( Calendar.YEAR ) % 2 );
        cal.set( Calendar.DAY_OF_YEAR, 1 );

        Date startDate = cal.getTime();

        cal.add( Calendar.YEAR, 1 );
        cal.set( Calendar.DAY_OF_YEAR, cal.getActualMaximum( Calendar.DAY_OF_YEAR ) );

        return new Period( this, startDate, cal.getTime() );
    }

    @Override
    public Period createPeriod( DateTimeUnit dateTimeUnit, com.mass3d.calendar.Calendar calendar )
    {
        return createPeriod( createCalendarInstance( dateTimeUnit.toJdkDate() ) );
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
    public DateTimeUnit getDateWithOffset( DateTimeUnit dateTimeUnit, int offset, com.mass3d.calendar.Calendar calendar )
    {
        dateTimeUnit.setYear( dateTimeUnit.getYear() - dateTimeUnit.getYear() % 2 + ( 2 * offset ) );

        return dateTimeUnit;
    }

    /**
     * Generates two-yearly Periods for the last 10, current and next 10 years.
     */
    @Override
    public List<Period> generatePeriods( Date date )
    {
        //TODO: use local calendar instead of hard coded Gregorian
        Calendar cal = createCalendarInstance( date );
        cal.add( Calendar.YEAR, cal.get( Calendar.YEAR ) % 2 == 0 ? -10 : -9 );
        cal.set( Calendar.DAY_OF_YEAR, 1 );

        ArrayList<Period> twoYears = new ArrayList<>();

        for ( int i = 0; i < 11; ++i )
        {
            Date startDate = cal.getTime();
            cal.add( Calendar.YEAR, 1 );
            cal.set( Calendar.DAY_OF_YEAR, cal.getActualMaximum( Calendar.DAY_OF_YEAR ) );
            twoYears.add( new Period( this, startDate, cal.getTime() ) );
            cal.add( Calendar.DAY_OF_YEAR, 1 );
        }

        return twoYears;
    }

    @Override
    public List<Period> generatePeriods( DateTimeUnit dateTimeUnit )
    {
        return null; // TODO
    }

    @Override
    public List<Period> generateRollingPeriods( Date date )
    {
        return generateLast5Years( date );
    }

    @Override
    public List<Period> generateRollingPeriods( DateTimeUnit dateTimeUnit, com.mass3d.calendar.Calendar calendar )
    {
        return generateLast5Years( calendar.toIso( dateTimeUnit ).toJdkDate() );
    }

    @Override
    public List<Period> generateLast5Years( Date date )
    {
        //TODO: use local calendar instead of hard coded Gregorian
        Calendar cal = createCalendarInstance( date );
        cal.add( Calendar.YEAR, cal.get( Calendar.YEAR ) % 2 == 0 ? -10 : -9 );
        cal.set( Calendar.DAY_OF_YEAR, 1 );

        ArrayList<Period> twoYears = new ArrayList<>();

        for ( int i = 0; i < 5; ++i )
        {
            Date startDate = cal.getTime();
            cal.add( Calendar.YEAR, 1 );
            cal.set( Calendar.DAY_OF_YEAR, cal.getActualMaximum( Calendar.DAY_OF_YEAR ) );
            twoYears.add( new Period( this, startDate, cal.getTime() ) );
            cal.add( Calendar.DAY_OF_YEAR, 1 );
        }

        return twoYears;
    }

    @Override
    public String getIsoDate( Period period )
    {
        return null; // TODO
    }

    @Override
    public String getIsoDate( DateTimeUnit dateTimeUnit, com.mass3d.calendar.Calendar calendar )
    {
        return null; // TODO
    }

    @Override
    public Period createPeriod( String isoDate )
    {
        return null; // TODO
    }

    @Override
    public String getIsoFormat()
    {
        return null; // TODO
    }
    
    @Override
    public String getIso8601Duration() 
    {
        return ISO8601_DURATION; 
    }


    @Override
    public Date getRewindedDate( Date date, Integer rewindedPeriods )
    {
        date = date != null ? date : new Date();
        rewindedPeriods = rewindedPeriods != null ? rewindedPeriods : 1;

        //TODO: use local calendar instead of hard coded Gregorian
        Calendar cal = createCalendarInstance( date );
        cal.add( Calendar.YEAR, (rewindedPeriods * -2) );

        return cal.getTime();
    }
}
