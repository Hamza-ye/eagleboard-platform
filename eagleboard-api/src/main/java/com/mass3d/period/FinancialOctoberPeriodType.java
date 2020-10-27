package com.mass3d.period;

import java.util.Calendar;
import com.mass3d.calendar.DateTimeUnit;

public class FinancialOctoberPeriodType
    extends FinancialPeriodType
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = -1623576547899897811L;

    private static final String ISO_FORMAT = "yyyyOct";

    private static final String ISO8601_DURATION = "P1Y";

    public static final String NAME = "FinancialOct";

    @Override
    public int getBaseMonth()
    {
        return Calendar.OCTOBER;
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public String getIsoDate( DateTimeUnit dateTimeUnit, com.mass3d.calendar.Calendar calendar )
    {
        return String.format( "%dOct", dateTimeUnit.getYear() );
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
}
