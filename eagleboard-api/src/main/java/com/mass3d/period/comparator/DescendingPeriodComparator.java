package com.mass3d.period.comparator;

import java.util.Comparator;
import com.mass3d.period.Period;

/**
 * Sorts periods descending based on the end date, then the start date, i.e. the latest period
 * comes first. The start date and end date properties cannot be null.
 * 
 */
public class DescendingPeriodComparator
    implements Comparator<Period>
{
    public static final DescendingPeriodComparator INSTANCE = new DescendingPeriodComparator();
    
    @Override
    public int compare( Period period1, Period period2 )
    {
        int endDateCompared = period2.getEndDate().compareTo( period1.getEndDate() );
        
        if ( endDateCompared != 0 )
        {
            return endDateCompared;
        }

        return period2.getStartDate().compareTo( period1.getEndDate() );
    }
}
