package com.mass3d.period;

import java.util.Calendar;
import java.util.Date;
import com.mass3d.calendar.CalendarService;
import com.mass3d.calendar.DateTimeUnit;
import com.mass3d.calendar.impl.Iso8601Calendar;

/**
 * An abstraction over a calendar implementation, expects input to be in whatever the current
 * system calendar is using, and all output will be in ISO 8601.
 *
 */
public class Cal
{
    private static CalendarService calendarService;

    public static void setCalendarService( CalendarService calendarService )
    {
        Cal.calendarService = calendarService;
    }

    public static com.mass3d.calendar.Calendar getCalendar()
    {
        if ( calendarService != null )
        {
            return calendarService.getSystemCalendar();
        }

        return Iso8601Calendar.getInstance();
    }

    private DateTimeUnit dateTimeUnit = new DateTimeUnit( 1, 1, 1 );

    public Cal()
    {
        dateTimeUnit = getCalendar().today();
    }

    /**
     * @param year  the year starting at AD 1.
     * @param month the month starting at 1.
     * @param day   the day of the month starting at 1.
     */
    public Cal( int year, int month, int day )
    {
        dateTimeUnit = new DateTimeUnit( year, month, day );
    }

    /**
     * @param year    the year starting at AD 1.
     * @param month   the month starting at 1.
     * @param day     the day of the month starting at 1.
     * @param iso8601 is this period an iso period
     */
    public Cal( int year, int month, int day, boolean iso8601 )
    {
        dateTimeUnit = new DateTimeUnit( year, month, day, iso8601 );
    }

    /**
     * @param date the date.
     */
    public Cal( Date date )
    {
        dateTimeUnit = DateTimeUnit.fromJdkDate( date );
    }

    /**
     * Sets the time of the calendar to now.
     */
    public Cal now()
    {
        dateTimeUnit = getCalendar().today();
        return this;
    }

    /**
     * Adds the given amount of time to the given calendar field.
     *
     * @param field  the calendar field.
     * @param amount the amount of time.
     */
    public Cal add( int field, int amount )
    {
        switch ( field )
        {
            case Calendar.YEAR:
                dateTimeUnit = getCalendar().plusYears( dateTimeUnit, amount );
            case Calendar.MONTH:
                dateTimeUnit = getCalendar().plusMonths( dateTimeUnit, amount );
            case Calendar.DAY_OF_MONTH:
            case Calendar.DAY_OF_YEAR:
                dateTimeUnit = getCalendar().plusDays( dateTimeUnit, amount );
            break;
            default:
                throw new UnsupportedOperationException();
        }

        return this;
    }

    /**
     * Subtracts the given amount of time to the given calendar field.
     *
     * @param field  the calendar field.
     * @param amount the amount of time.
     */
    public Cal subtract( int field, int amount )
    {
        switch ( field )
        {
            case Calendar.YEAR:
                dateTimeUnit = getCalendar().minusYears( dateTimeUnit, amount );
            case Calendar.MONTH:
                dateTimeUnit = getCalendar().minusMonths( dateTimeUnit, amount );
            case Calendar.DAY_OF_MONTH:
            case Calendar.DAY_OF_YEAR:
                dateTimeUnit = getCalendar().minusDays( dateTimeUnit, amount );
            break;
            default:
                throw new UnsupportedOperationException();
        }

        return this;
    }

    /**
     * Returns the value of the given calendar field.
     *
     * @param field the field.
     */
    public int get( int field )
    {
        return getCalendar().toIso( dateTimeUnit ).toJdkCalendar().get( field );
    }

    /**
     * Returns the current year.
     *
     * @return current year
     */
    public int getYear()
    {
        return getCalendar().toIso( dateTimeUnit ).toJdkCalendar().get( Calendar.YEAR );
    }

    /**
     * Sets the current time.
     *
     * @param year  the year starting at AD 1.
     * @param month the month starting at 1.
     * @param day   the day of the month starting at 1.
     */
    public Cal set( int year, int month, int day )
    {
        dateTimeUnit = new DateTimeUnit( year, month, day );
        return this;
    }

    /**
     * Sets the current month and day.
     *
     * @param month the month starting at 1.
     * @param day   the day of the month starting at 1.
     */
    public Cal set( int month, int day )
    {
        dateTimeUnit.setMonth( month );
        dateTimeUnit.setDay( day );
        return this;
    }

    /**
     * Sets the current time.
     *
     * @param date the date to base time on.
     */
    public Cal set( Date date )
    {
        dateTimeUnit = getCalendar().fromIso( DateTimeUnit.fromJdkDate( date ) );
        return this;
    }

    /**
     * Returns the current date the cal.
     */
    public Date time()
    {
        return getCalendar().toIso( dateTimeUnit ).toJdkDate();
    }
}
