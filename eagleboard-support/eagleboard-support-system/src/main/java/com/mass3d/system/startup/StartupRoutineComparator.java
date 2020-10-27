package com.mass3d.system.startup;

import java.util.Comparator;

/**
 * Compares StartupRoutines based on their runlevel values.
 *
 * @version $Id: StartupRoutineComparator.java 3217 2007-04-02 08:54:21Z torgeilo $
 */
public class StartupRoutineComparator
    implements Comparator<StartupRoutine>
{
    @Override
    public int compare( StartupRoutine routineA, StartupRoutine routineB )
    {
        return routineA.getRunlevel() - routineB.getRunlevel();
    }
}
