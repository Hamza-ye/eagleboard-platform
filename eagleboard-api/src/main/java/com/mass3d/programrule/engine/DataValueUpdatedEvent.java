package com.mass3d.programrule.engine;

import org.springframework.context.ApplicationEvent;

public class DataValueUpdatedEvent extends ApplicationEvent
{
    private long programStageInstance;

    public DataValueUpdatedEvent( Object source, long programStageInstance )
    {
        super( source );
        this.programStageInstance = programStageInstance;
    }

    public long getProgramStageInstance()
    {
        return programStageInstance;
    }
}
