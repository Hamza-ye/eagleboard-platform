package com.mass3d.period;

/**
 * PeriodType for weekly Periods. A valid weekly Period has startDate set to
 * Saturday and endDate set to Friday the same week, assuming Saturday is the first
 * day and Friday is the last day of the week.
 *
 */
public class WeeklySaturdayPeriodType
    extends WeeklyAbstractPeriodType
{
    public static final String NAME = "WeeklySaturday";

    public WeeklySaturdayPeriodType()
    {
        super( NAME, 6, "yyyySatWn", "P7D", 7, "1 week", "SatW" );
    }
}
