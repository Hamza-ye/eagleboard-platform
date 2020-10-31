package com.mass3d.trackedentity.comparator;

import java.util.Comparator;
import com.mass3d.trackedentity.TrackedEntityAttribute;

/**
 * 
 * @version $ TrackedEntityAttributeSortOrderInListNoProgramComparator.java Jan
 *          8, 2014 9:24:20 PM $
 */
public class TrackedEntityAttributeSortOrderInListNoProgramComparator
    implements Comparator<TrackedEntityAttribute>
{
    @Override
    public int compare( TrackedEntityAttribute attribute0, TrackedEntityAttribute attribute1 )
    {
        if ( attribute0.getSortOrderInListNoProgram() == null || attribute0.getSortOrderInListNoProgram() == 0 )
        {
            return attribute0.getName().compareTo( attribute1.getName() );
        }

        if ( attribute1.getSortOrderInListNoProgram() == null || attribute1.getSortOrderInListNoProgram() == 0 )
        {
            return attribute0.getName().compareTo( attribute1.getName() );
        }

        return attribute0.getSortOrderInListNoProgram() - attribute1.getSortOrderInListNoProgram();
    }
}