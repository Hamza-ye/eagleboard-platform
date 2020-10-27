package com.mass3d.commons.timer;

/**
 * Simple interface that captures time durations and pretty prints it back to you.
 *
 */
public interface Timer
{
    static Timer startTimer()
    {
        return new SystemTimer().start();
    }

    /**
     * Starts the Timer immediately.
     * @return this Timer.
     */
    Timer start();

    /**
     * Stops the Timer immediately.
     * @return this Timer.
     */
    Timer stop();

    /**
     * Returns the elapsed time between {@link #start()} and {@link #stop()} was called.
     * @return the elapsed time in nanoseconds.
     */
    Long duration();
}
