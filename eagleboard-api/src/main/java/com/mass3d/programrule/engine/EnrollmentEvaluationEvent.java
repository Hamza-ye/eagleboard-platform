package com.mass3d.programrule.engine;

import org.springframework.context.ApplicationEvent;

public class  EnrollmentEvaluationEvent extends ApplicationEvent
{
    private long programInstance;

    public EnrollmentEvaluationEvent( Object source, long programInstance )
    {
        super( source );
        this.programInstance = programInstance;
    }

    public long getProgramInstance()
    {
        return programInstance;
    }
}
