package com.mass3d.system.startup;

/**
 * Executes a collection of StartupRoutines when the system is started.
 *
 */
public interface StartupRoutineExecutor
{
    String ID = StartupRoutineExecutor.class.getName();

    /**
     * Executes the StartupRoutines.
     *
     * @throws Exception on execution failure.
     */
    void execute()
        throws Exception;

    /**
     * Executes the StartupRoutines for testing.
     *
     * @throws Exception on execution failure.
     */
    void executeForTesting()
        throws Exception;
}
