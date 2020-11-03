package com.mass3d.sms;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.message.MessageSender;
import com.mass3d.sms.incoming.IncomingSms;
import com.mass3d.sms.incoming.IncomingSmsListener;
import com.mass3d.sms.incoming.IncomingSmsService;
import com.mass3d.sms.incoming.SmsMessageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component( "com.mass3d.sms.SmsConsumerThread")
public class SmsConsumerThread
{
    private List<IncomingSmsListener> listeners;

    private final MessageQueue messageQueue;

    private final MessageSender smsSender;

    private final IncomingSmsService incomingSmsService;

    public SmsConsumerThread( MessageQueue messageQueue, @Qualifier( "smsMessageSender" ) MessageSender smsSender,
        IncomingSmsService incomingSmsService )
    {
        checkNotNull( messageQueue );
        checkNotNull( smsSender );
        checkNotNull( incomingSmsService );

        this.messageQueue = messageQueue;
        this.smsSender = smsSender;
        this.incomingSmsService = incomingSmsService;
    }
    

    public void spawnSmsConsumer()
    {
        IncomingSms message = messageQueue.get();

        while ( message != null )
        {
            log.info( "Received SMS: " + message.getText() );
            
            try
            {
                for ( IncomingSmsListener listener : listeners )
                {
                    if ( listener.accept( message ) )
                    {
                        listener.receive( message );
                        messageQueue.remove( message );
                        return;
                    }
                }

                log.warn( "No SMS command found in received data" );

                message.setStatus( SmsMessageStatus.UNHANDLED );

                smsSender.sendMessage( null, "No command found", message.getOriginator() );
            }
            catch ( Exception e )
            {
                e.printStackTrace();

                message.setStatus( SmsMessageStatus.FAILED );
                message.setParsed( false );
            }
            finally
            {
                messageQueue.remove( message );

                incomingSmsService.update( message );

                message = messageQueue.get();
            }
        }
    }

    @Autowired
    public void setListeners( List<IncomingSmsListener> listeners )
    {
        this.listeners = listeners;

        log.info( "Following listeners are registered: " + listeners );
    }
}
