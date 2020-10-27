package com.mass3d.period;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PeriodHierarchy
{
    private Map<Long, Set<Long>> intersectingPeriods = new HashMap<>();
    
    private Map<Long, Set<Long>> periodsBetween = new HashMap<>();
    
    public Map<Long, Set<Long>> getIntersectingPeriods()
    {
        return intersectingPeriods;
    }

    public Set<Long> getIntersectingPeriods( Period period )
    {
        return new HashSet<>( intersectingPeriods.get( period.getId() ) );
    }
    
    public Set<Long> getIntersectingPeriods( Collection<Period> periods )
    {
        periods = new HashSet<>( periods );
        
        Set<Long> set = new HashSet<>();
        
        for ( Period period : periods )
        {
            if ( intersectingPeriods.containsKey( period.getId() ) )
            {
                set.addAll( intersectingPeriods.get( period.getId() ) );
            }
        }
        
        return set;
    }

    public Map<Long, Set<Long>> getPeriodsBetween()
    {
        return periodsBetween;
    }

    public Set<Long> getPeriodsBetween( Period period )
    {
        return new HashSet<>( periodsBetween.get( period.getId() ) );
    }
    
    public Set<Long> getPeriodsBetween( Collection<Period> periods )
    {
        periods = new HashSet<>( periods );
        
        Set<Long> set = new HashSet<>();
        
        for ( Period period : periods )
        {
            if ( periodsBetween.containsKey( period.getId() ) )
            {
                set.addAll( periodsBetween.get( period.getId() ) );
            }
        }
        
        return set;
    }
}
