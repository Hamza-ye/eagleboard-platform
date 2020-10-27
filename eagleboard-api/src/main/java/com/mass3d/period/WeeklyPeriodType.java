package com.mass3d.period;

/**
 * PeriodType for weekly Periods. A valid weekly Period has startDate set to
 * Monday and endDate set to Sunday the same week, assuming Monday is the first
 * day and Sunday is the last day of the week.
 *
 */
public class WeeklyPeriodType
    extends WeeklyAbstractPeriodType
{
    public static final String NAME = "Weekly";

    public WeeklyPeriodType()
    {
        super( NAME, 1, "yyyyWn", "P7D", 7, "1 week", "W" );
    }
}
