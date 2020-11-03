package com.mass3d.program.notification;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Calendar;
import com.mass3d.message.MessageService;
import com.mass3d.scheduling.AbstractJob;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.scheduling.JobType;
import com.mass3d.system.notification.NotificationLevel;
import com.mass3d.system.notification.Notifier;
import com.mass3d.system.util.Clock;
import org.springframework.stereotype.Component;

@Component( "programNotificationsJob" )
public class ProgramNotificationJob extends AbstractJob
{
    private final ProgramNotificationService programNotificationService;

    private final MessageService messageService;

    private final Notifier notifier;

    public ProgramNotificationJob( ProgramNotificationService programNotificationService, MessageService messageService,
        Notifier notifier )
    {
        checkNotNull( programNotificationService );
        checkNotNull( messageService );
        checkNotNull( notifier );

        this.programNotificationService = programNotificationService;
        this.messageService = messageService;
        this.notifier = notifier;
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public JobType getJobType()
    {
        return JobType.PROGRAM_NOTIFICATIONS;
    }

    @Override
    public void execute( JobConfiguration jobConfiguration )
    {
        final Clock clock = new Clock().startClock();

        notifier.notify( jobConfiguration, "Generating and sending scheduled program notifications" );

        try
        {
            runInternal();

            notifier.notify( jobConfiguration, NotificationLevel.INFO, "Generated and sent scheduled program notifications: " + clock.time(), true );
        }
        catch ( RuntimeException ex )
        {
            notifier.notify( jobConfiguration, NotificationLevel.ERROR, "Process failed: " + ex.getMessage(), true );

            messageService.sendSystemErrorNotification( "Generating and sending scheduled program notifications failed", ex );

            throw ex;
        }
    }

    private void runInternal()
    {
        // Today at 00:00:00
        Calendar calendar = Calendar.getInstance();
        calendar.set( Calendar.HOUR, 0 );

        programNotificationService.sendScheduledNotificationsForDay( calendar.getTime() );
        programNotificationService.sendScheduledNotifications();
    }

}
