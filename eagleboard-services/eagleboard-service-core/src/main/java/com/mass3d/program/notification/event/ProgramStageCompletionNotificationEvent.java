package com.mass3d.program.notification.event;

import org.springframework.context.ApplicationEvent;

public class ProgramStageCompletionNotificationEvent extends ApplicationEvent
{
    private long programStageInstance;

    public ProgramStageCompletionNotificationEvent( Object source, long programStageInstance )
    {
        super( source );
        this.programStageInstance = programStageInstance;
    }

    public long getProgramStageInstance()
    {
        return programStageInstance;
    }
}
