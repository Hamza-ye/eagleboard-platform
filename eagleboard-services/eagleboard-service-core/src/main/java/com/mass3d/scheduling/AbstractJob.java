package com.mass3d.scheduling;

import com.mass3d.feedback.ErrorReport;

/**
 * All jobs related to the system extends AbstractJob and can override the validate method.
 *
 */
public abstract class AbstractJob
    implements Job
{
    @Override
    public ErrorReport validate()
    {
        return null;
    }
}
