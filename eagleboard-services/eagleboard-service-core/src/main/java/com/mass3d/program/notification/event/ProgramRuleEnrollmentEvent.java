package com.mass3d.program.notification.event;

import org.springframework.context.ApplicationEvent;

public class ProgramRuleEnrollmentEvent extends ApplicationEvent
{
    private long template;

    private long programInstance;

    public ProgramRuleEnrollmentEvent( Object source, long template, long programInstance )
    {
        super( source );
        this.template = template;
        this.programInstance = programInstance;
    }

    public long getTemplate()
    {
        return template;
    }

    public long getProgramInstance()
    {
        return programInstance;
    }
}
