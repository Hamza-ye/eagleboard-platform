package com.mass3d.period;

/**
 * PeriodType for weekly Periods. A valid weekly Period has startDate set to
 * Sunday and endDate set to Saturday the same week, assuming Sunday is the first
 * day and Saturday is the last day of the week.
 *
 */
public class WeeklySundayPeriodType
    extends WeeklyAbstractPeriodType
{
    public static final String NAME = "WeeklySunday";

    public WeeklySundayPeriodType()
    {
        super( NAME, 7, "yyyySunWn", "P7D", 7, "1 week", "SunW" );
    }
}
