package com.mass3d.system.startup;

/**
 * Defines a startup routine which should be executed when the system is
 * started. The runlevel can be used to group startup routines that are
 * dependent on other startup routines, without too much detail and knowledge.
 *
 * @version $Id: StartupRoutine.java 5781 2008-10-01 12:12:48Z larshelg $
 */
public interface StartupRoutine
{
    /**
     * Executes the startup routine. It should fail hard if it is required to be
     * executed successfully, or if any other unexpected errors occur.
     *
     * @throws Exception if anything goes wrong.
     */
    void execute()
        throws Exception;

    /**
     * Returns the name of the startup routine.
     *
     * @return the name.
     */
    String getName();

    /**
     * StartupRoutines with lower runlevels will be executed before
     * StartupRoutines with higher runlevel.
     *
     * @return the runlevel for the StartupRoutine.
     */
    int getRunlevel();

    /**
     * Returns whether this StartupRoutine is to be skipped in tests or not.
     * @return true if this StartupRoutine is skipped in tests, false otherwise.
     */
    boolean skipInTests();
}
