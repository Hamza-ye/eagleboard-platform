package com.mass3d.system.filter;

import java.util.Date;
import com.mass3d.commons.filter.Filter;
import com.mass3d.period.Period;

public class PastAndCurrentPeriodFilter
    implements Filter<Period>
{
    @Override
    public boolean retain( Period period )
    {
        return period != null && period.getStartDate() != null && period.getStartDate().compareTo( new Date() ) <= 0;
    }
}
