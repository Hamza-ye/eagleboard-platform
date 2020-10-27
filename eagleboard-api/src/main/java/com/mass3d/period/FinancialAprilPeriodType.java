package com.mass3d.period;

import java.util.Calendar;
import com.mass3d.calendar.DateTimeUnit;

public class FinancialAprilPeriodType
    extends FinancialPeriodType
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 8790198046182231889L;

    private static final String ISO_FORMAT = "yyyyApril";

    private static final String ISO8601_DURATION = "P1Y";

    public static final String NAME = "FinancialApril";

    @Override
    public int getBaseMonth()
    {
        return Calendar.APRIL;
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public String getIsoDate( DateTimeUnit dateTimeUnit, com.mass3d.calendar.Calendar calendar )
    {
        return String.format( "%dApril", dateTimeUnit.getYear() );
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
