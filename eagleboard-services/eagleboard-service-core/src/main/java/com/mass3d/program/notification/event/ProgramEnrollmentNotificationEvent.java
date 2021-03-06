package com.mass3d.program.notification.event;

import org.springframework.context.ApplicationEvent;

public class ProgramEnrollmentNotificationEvent extends ApplicationEvent
{
    private long programInstance;

    public ProgramEnrollmentNotificationEvent( Object source, long programInstance )
    {
        super( source );
        this.programInstance = programInstance;
    }

    public long getProgramInstance()
    {
        return programInstance;
    }
}
