package com.mass3d.programrule.engine;

import org.springframework.context.ApplicationEvent;

public class StageScheduledEvaluationEvent extends ApplicationEvent
{
    private long programStageInstance;

    public StageScheduledEvaluationEvent( Object source, long programStageInstance )
    {
        super( source );
        this.programStageInstance = programStageInstance;
    }

    public long getProgramStageInstance()
    {
        return programStageInstance;
    }
}
