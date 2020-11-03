package com.mass3d.sms.incoming;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.sms.SmsPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service( "com.mass3d.sms.incoming.SmsConsumerService" )
public class DefaultSmsConsumerService
    implements SmsConsumerService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final SmsPublisher smsPublisher;

    public DefaultSmsConsumerService( SmsPublisher smsPublisher )
    {
        checkNotNull( smsPublisher );

        this.smsPublisher = smsPublisher;
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @PostConstruct
    public void init()
    {
        startSmsConsumer();
    }

    @Override
    public void startSmsConsumer()
    {
        smsPublisher.start();

        log.info( "SMS consumer started" );
    }

    @Override
    public void stopSmsConsumer()
    {
        smsPublisher.stop();

        log.info( "SMS consumer stopped" );
    }
}
