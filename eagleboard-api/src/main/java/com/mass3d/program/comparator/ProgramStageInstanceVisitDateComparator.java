package com.mass3d.program.comparator;

import java.util.Comparator;
import java.util.Date;
import com.mass3d.program.ProgramStageInstance;

/**
 * @version ProgramStageInstanceVisitDateComparator.java 8:24:02 PM Mar 5, 2013
 *          $
 */
public class ProgramStageInstanceVisitDateComparator
    implements Comparator<ProgramStageInstance>
{
    @Override
    public int compare( ProgramStageInstance programStageInstance1, ProgramStageInstance programStageInstance2 )
    {
        Date d1 = (programStageInstance1.getExecutionDate() != null) ? programStageInstance1.getExecutionDate() : programStageInstance1.getDueDate();
        Date d2 = (programStageInstance2.getExecutionDate() != null) ? programStageInstance2.getExecutionDate() : programStageInstance2.getDueDate();
        if ( d1.before( d2 ) )
        {
            return -1;
        }
        else if ( d1.after( d2 ) )
        {
            return 1;
        }
        return 0;
    }
}