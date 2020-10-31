package com.mass3d.program.comparator;

import java.util.Comparator;
import com.mass3d.program.ProgramStageSection;

/**
 * @version ProgramStageSectionSortOrderComparator.java 9:32:23 AM Feb 4, 2013 $
 */
public class ProgramStageSectionSortOrderComparator
    implements Comparator<ProgramStageSection>
{
    @Override
    public int compare( ProgramStageSection object0, ProgramStageSection object1 )
    {
        if ( object0.getSortOrder() == null )
        {
            return object1.getSortOrder() != null ? -1 : 0;
        }

        return object0.getSortOrder().compareTo( object1.getSortOrder() );
    }
}
