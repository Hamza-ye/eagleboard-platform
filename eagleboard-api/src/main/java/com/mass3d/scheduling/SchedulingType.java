package com.mass3d.scheduling;

public enum SchedulingType
{
    /**
     * Scheduling based on a expression.
     */
    CRON,
    /**
     * Scheduling based on a fixed delay between invocations.
     */
    FIXED_DELAY;
}
