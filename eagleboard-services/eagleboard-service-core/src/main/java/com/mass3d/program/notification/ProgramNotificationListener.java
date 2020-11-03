package com.mass3d.program.notification;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.program.notification.event.ProgramEnrollmentCompletionNotificationEvent;
import com.mass3d.program.notification.event.ProgramEnrollmentNotificationEvent;
import com.mass3d.program.notification.event.ProgramRuleEnrollmentEvent;
import com.mass3d.program.notification.event.ProgramRuleStageEvent;
import com.mass3d.program.notification.event.ProgramStageCompletionNotificationEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Async
@Component( "com.mass3d.program.notification.ProgramNotificationListener" )
public class ProgramNotificationListener
{
    private final ProgramNotificationService programNotificationService;

    public ProgramNotificationListener( ProgramNotificationService programNotificationService )
    {
        checkNotNull( programNotificationService );
        this.programNotificationService = programNotificationService;
    }

    @TransactionalEventListener
    public void onEnrollment( ProgramEnrollmentNotificationEvent event )
    {
        programNotificationService.sendEnrollmentNotifications( event.getProgramInstance() );
    }

    @TransactionalEventListener
    public void onCompletion( ProgramEnrollmentCompletionNotificationEvent event )
    {
        programNotificationService.sendEnrollmentCompletionNotifications( event.getProgramInstance() );
    }

    @TransactionalEventListener
    public void onEvent( ProgramStageCompletionNotificationEvent event )
    {
        programNotificationService.sendEventCompletionNotifications( event.getProgramStageInstance() );
    }

    // Published by rule engine
    @TransactionalEventListener
    public void onProgramRuleEnrollment( ProgramRuleEnrollmentEvent event )
    {
        programNotificationService.sendProgramRuleTriggeredNotifications( event.getTemplate(), event.getProgramInstance() );
    }

    @TransactionalEventListener
    public void onProgramRuleEvent( ProgramRuleStageEvent event )
    {
        programNotificationService.sendProgramRuleTriggeredEventNotifications( event.getTemplate(), event.getProgramStageInstance() );
    }
}
