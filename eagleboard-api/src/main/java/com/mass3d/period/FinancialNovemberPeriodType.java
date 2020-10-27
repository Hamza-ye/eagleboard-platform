package com.mass3d.period;

import java.util.Calendar;
import com.mass3d.calendar.DateTimeUnit;

public class FinancialNovemberPeriodType 
    extends FinancialPeriodType
{

    private static final long serialVersionUID = -8443905531396977357L;

    private static final String ISO_FORMAT = "yyyyNov";

    private static final String ISO8601_DURATION = "P1Y";

    public static final String NAME = "FinancialNov";

    @Override
    public int getBaseMonth()
    {
        return Calendar.NOVEMBER;
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public String getIsoDate( DateTimeUnit dateTimeUnit, com.mass3d.calendar.Calendar calendar )
    {
        return String.format( "%dNov", dateTimeUnit.getYear() + 1 );
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
