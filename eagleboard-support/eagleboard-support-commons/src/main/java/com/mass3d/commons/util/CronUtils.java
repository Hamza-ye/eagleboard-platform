package com.mass3d.commons.util;

public class CronUtils
{
    public enum Weekday
    {
        SUNDAY( "SUN" ),
        MONDAY( "MON" ),
        TUESDAY( "TUE" ),
        WEDNESDAY( "WED" ),
        THURSDAY( "THU" ),
        FRIDAY( "FRI" ),
        SATURDAY( "SAT" );

        private final String name;

        Weekday( String name )
        {
            this.name = name;
        }
    }

    /**
     * Generates a cron pattern that will execute every day at the given hour:minute
     *
     * @param minutes
     * @param hours
     * @return a cron pattern
     */
    public static String getDailyCronExpression( int minutes, int hours )
    {
        return getCronExpression(
            "0",
            String.valueOf( minutes ),
            String.valueOf( hours ),
            "*/1",
            null,
            null
        );
    }

    /**
     * Generates a cron pattern that will execute every week at the dayOfWeek at hour:minute
     *
     * @param minutes
     * @param hours
     * @param dayOfWeek can be 0-7. 0 and 7 both resolve to sunday
     * @return a cron pattern
     */
    public static String getWeeklyCronExpression( int minutes, int hours, int dayOfWeek )
    {
        return getCronExpression(
            "0",
            String.valueOf( minutes ),
            String.valueOf( hours ),
            null,
            null,
            Weekday.values()[(dayOfWeek % 7)].name // both 0 and 7 are valid as Sunday in crontab patterns
        );
    }

    /**
     * Generates a cron pattern that will execute every month at the dayOfMonth at hour:minute
     *
     * @param minutes
     * @param hours
     * @param dayOfMonth
     * @return a cron pattern
     */
    public static String getMonthlyCronExpression( int minutes, int hours, int dayOfMonth )
    {
        return getCronExpression(
            "0",
            String.valueOf( minutes ),
            String.valueOf( hours ),
            String.valueOf( dayOfMonth ),
            "*/1",
            null
        );
    }

    /**
     * Joins together each segment of a cron pattern into a complete cron pattern.
     *
     * @param seconds a valid cron segment
     * @param minutes a valid cron segment
     * @param hours   a valid cron segment
     * @param days    a valid cron segment
     * @param months  a valid cron segment
     * @param weekday a valid cron segment (MON-SUN)
     * @return a cron pattern
     */
    public static String getCronExpression( String seconds, String minutes, String hours, String days, String months,
        String weekday )
    {
        return String.join( " ",
            (seconds == null ? "*" : seconds),
            (minutes == null ? "*" : minutes),
            (hours == null ? "*" : hours),
            (days == null ? "*" : days),
            (months == null ? "*" : months),
            (weekday == null ? "*" : weekday)
        );
    }
}
