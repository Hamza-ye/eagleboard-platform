package com.mass3d.commons.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Class for functions to be used in JEXL expression evaluation.
 *
 */
public class ExpressionFunctions
{
    public static final String NAMESPACE = "d2";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );

    /**
     * Function which will return zero if the argument is a negative number.
     *
     * @param value the value, must be a number.
     * @return a Double.
     */
    public static Double zing( Number value )
    {
        if ( value == null )
        {
            return null;
        }

        return Math.max( 0d, value.doubleValue() );
    }

    /**
     * Function which will return one if the argument is zero or a positive
     * number, and zero if not.
     *
     * @param value the value, must be a number.
     * @return a Double.
     */
    public static Double oizp( Number value )
    {
        if ( value == null )
        {
            return null;
        }

        return ( value.doubleValue() >= 0d ) ? 1d : 0d;
    }

    /**
     * Function which will return the count of zero or positive values among the
     * given argument values.
     *
     * @param values the arguments.
     * @return an Integer.
     */
    public static Integer zpvc( Number... values )
    {
        if ( values == null || values.length == 0 )
        {
            throw new IllegalArgumentException( "Argument is null or empty" );
        }

        int count = 0;

        for ( Number value : values )
        {
            if ( value != null && value.doubleValue() >= 0d )
            {
                count++;
            }
        }

        return count;
    }

    /**
     * Functions which will return the true value if the condition is true, false
     * value if not.
     *
     * @param condititon the condition.
     * @param trueValue the true value.
     * @param falseValue the false value.
     * @return a String.
     */
    public static Object condition( String condititon, Object trueValue, Object falseValue )
    {
        return ExpressionUtils.isTrue( condititon, null ) ? trueValue : falseValue;
    }

    /**
     * Function which will return the number of days between the two given dates.
     *
     * @param start the start date.
     * @param end the end date.
     * @return number of days between dates.
     * @throws ParseException if start or end could not be parsed.
     */
    public static Long daysBetween( String start, String end )
    {
        LocalDate st = LocalDate.parse( start, DATE_FORMAT );
        LocalDate en = LocalDate.parse( end, DATE_FORMAT );

        return ChronoUnit.DAYS.between( st, en );
    }

    /**
     * Function which will return the number of months between the two given dates.
     *
     * @param start the start date.
     * @param end the end date.
     * @return number of months between dates.
     * @throws ParseException if start or end could not be parsed.
     */
    public static Long monthsBetween( String start, String end )
    {
        LocalDate st = LocalDate.parse( start, DATE_FORMAT );
        LocalDate en = LocalDate.parse( end, DATE_FORMAT );

        return ChronoUnit.MONTHS.between( st, en );
    }

    /**
     * Function which will return the number of days between the two given dates.
     *
     * @param start the start date.
     * @param end the end date.
     * @return number of years between dates.
     * @throws ParseException if start or end could not be parsed.
     */
    public static Long yearsBetween( String start, String end )
    {
        LocalDate st = LocalDate.parse( start, DATE_FORMAT );
        LocalDate en = LocalDate.parse( end, DATE_FORMAT );

        return ChronoUnit.YEARS.between( st, en );
    }

    /**
     * Function will will return true only if the parameter value is not null.
     *
     * @param value to check whether has a value.
     * @return true if the parameter has a value.
     */
    public static boolean hasValue( Object value )
    {
        return value != null;
    }
}
