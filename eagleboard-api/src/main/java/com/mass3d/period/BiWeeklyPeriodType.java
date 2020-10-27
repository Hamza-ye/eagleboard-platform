package com.mass3d.period;

public class BiWeeklyPeriodType
    extends BiWeeklyAbstractPeriodType
{
    public static final String NAME = "BiWeekly";

    public BiWeeklyPeriodType()
    {
        super( NAME, 1, "yyyyBiWn", "P14D", 14, "2 weeks", "BiW" );
    }
}
