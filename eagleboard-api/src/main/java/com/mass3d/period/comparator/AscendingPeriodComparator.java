package com.mass3d.period.comparator;

import java.util.Comparator;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodType;

/**
 * Sorts periods ascending based on the start date, then the end date, i.e. the earliest
 * period comes first. The start date and end date properties cannot be null.
 * 
 */
public class AscendingPeriodComparator
    implements Comparator<Period>
{
    public static final AscendingPeriodComparator INSTANCE = new AscendingPeriodComparator();
    
    @Override
    public int compare( Period period1, Period period2 )
    {
        PeriodType a = period1.getPeriodType();
        PeriodType b = period2.getPeriodType();

        int freqCompare = Integer.compare( a.getFrequencyOrder(), b.getFrequencyOrder() );
        int nameCompare = a.getName().compareTo( b.getName() );

        return freqCompare == 0 ? ( nameCompare == 0 ? period1.getStartDate().compareTo(period2.getStartDate() ) : nameCompare ) : freqCompare;
    }
}
