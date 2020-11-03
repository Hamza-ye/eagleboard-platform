package com.mass3d.sms.job;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashSet;
import com.mass3d.message.MessageSender;
import com.mass3d.outboundmessage.OutboundMessageResponse;
import com.mass3d.scheduling.AbstractJob;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.scheduling.JobType;
import com.mass3d.scheduling.parameters.SmsJobParameters;
import com.mass3d.sms.outbound.OutboundSms;
import com.mass3d.sms.outbound.OutboundSmsService;
import com.mass3d.sms.outbound.OutboundSmsStatus;
import com.mass3d.system.notification.Notifier;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component( "sendSmsJob" )
public class SendSmsJob
    extends AbstractJob
{
    private final MessageSender smsSender;

    private final Notifier notifier;

    private final OutboundSmsService outboundSmsService;

    public SendSmsJob( @Qualifier( "smsMessageSender" ) MessageSender smsSender, Notifier notifier,
        OutboundSmsService outboundSmsService )
    {
        checkNotNull( smsSender );
        checkNotNull( notifier );
        checkNotNull( outboundSmsService );

        this.smsSender = smsSender;
        this.notifier = notifier;
        this.outboundSmsService = outboundSmsService;
    }

    // -------------------------------------------------------------------------
    // I18n
    // -------------------------------------------------------------------------

    @Override
    public JobType getJobType()
    {
        return JobType.SMS_SEND;
    }

    @Override
    public void execute( JobConfiguration jobConfiguration )
    {
        SmsJobParameters parameters = (SmsJobParameters) jobConfiguration.getJobParameters();
        OutboundSms sms = new OutboundSms();
        sms.setSubject( parameters.getSmsSubject() );
        sms.setMessage( parameters.getMessage() );
        sms.setRecipients( new HashSet<>( parameters.getRecipientsList() ) );

        notifier.notify( jobConfiguration, "Sending SMS" );

        OutboundMessageResponse status = smsSender.sendMessage( sms.getSubject(), sms.getMessage(), sms.getRecipients() );

        if ( status.isOk() )
        {
            notifier.notify( jobConfiguration, "Message sending successful" );

            sms.setStatus( OutboundSmsStatus.SENT );
        }
        else
        {
            notifier.notify( jobConfiguration, "Message sending failed" );

            sms.setStatus( OutboundSmsStatus.FAILED );
        }

        outboundSmsService.save( sms );
    }

}
