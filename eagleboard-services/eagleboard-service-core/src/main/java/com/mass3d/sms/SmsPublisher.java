package com.mass3d.sms;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ScheduledFuture;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.sms.SmsPublisher" )
public class SmsPublisher
{
    private final MessageQueue messageQueue;

    private final SmsConsumerThread smsConsumer;

    private final TaskScheduler taskScheduler;

    public SmsPublisher( MessageQueue messageQueue, SmsConsumerThread smsConsumer, TaskScheduler taskScheduler )
    {

        checkNotNull( messageQueue );
        checkNotNull( smsConsumer );
        checkNotNull( taskScheduler );

        this.messageQueue = messageQueue;
        this.smsConsumer = smsConsumer;
        this.taskScheduler = taskScheduler;
    }

    private ScheduledFuture<?> future;

    public void start()
    {
        messageQueue.initialize();

        future = taskScheduler.scheduleWithFixedDelay(smsConsumer::spawnSmsConsumer, 5000 );
    }

    public void stop()
    {
        future.cancel( true );
    }
}
