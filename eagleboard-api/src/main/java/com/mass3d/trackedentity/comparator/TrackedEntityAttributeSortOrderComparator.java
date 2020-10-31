package com.mass3d.trackedentity.comparator;

import java.util.Comparator;
import com.mass3d.trackedentity.TrackedEntityAttribute;

/**
 * @version $ TrackedEntityAttributeSortOrderComparator.java Jun 5, 2013
 *          10:24:33 AM $
 */
public class TrackedEntityAttributeSortOrderComparator
    implements Comparator<TrackedEntityAttribute>
{
    @Override
    public int compare( TrackedEntityAttribute attribute0, TrackedEntityAttribute attribute1 )
    {
        if ( attribute0.getSortOrderInVisitSchedule() == null || attribute0.getSortOrderInVisitSchedule() == 0 )
        {
            return attribute0.getName().compareTo( attribute1.getName() );
        }

        if ( attribute1.getSortOrderInVisitSchedule() == null || attribute1.getSortOrderInVisitSchedule() == 0 )
        {
            return attribute0.getName().compareTo( attribute1.getName() );
        }

        return attribute0.getSortOrderInVisitSchedule() - attribute1.getSortOrderInVisitSchedule();
    }
}
