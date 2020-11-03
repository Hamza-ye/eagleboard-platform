package com.mass3d.program.notification.event;

import org.springframework.context.ApplicationEvent;

public class ProgramRuleStageEvent extends ApplicationEvent
{
    private long template;

    private long programStageInstance;

    public ProgramRuleStageEvent( Object source, long template, long programStageInstance )
    {
        super( source );
        this.template = template;
        this.programStageInstance = programStageInstance;
    }

    public long getTemplate()
    {
        return template;
    }

    public long getProgramStageInstance()
    {
        return programStageInstance;
    }
}
