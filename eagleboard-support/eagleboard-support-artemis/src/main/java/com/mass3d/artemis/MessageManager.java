package com.mass3d.artemis;

import org.apache.qpid.jms.JmsQueue;
import org.apache.qpid.jms.JmsTopic;
import com.mass3d.render.RenderService;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageManager
{
    private final JmsTemplate jmsTopicTemplate;
    private final JmsTemplate jmsQueueTemplate;
    private final RenderService renderService;

    public MessageManager( JmsTemplate jmsTopicTemplate, JmsTemplate jmsQueueTemplate, RenderService renderService )
    {
        this.jmsTopicTemplate = jmsTopicTemplate;
        this.jmsQueueTemplate = jmsQueueTemplate;
        this.renderService = renderService;
    }

    public void send( String destinationName, Message message )
    {
        jmsTopicTemplate.send( destinationName, session -> session.createTextMessage( renderService.toJsonAsString( message ) ) );
    }

    public void sendTopic( String destinationName, Message message )
    {
        jmsTopicTemplate.send( new JmsTopic( destinationName ), session -> session.createTextMessage( renderService.toJsonAsString( message ) ) );
    }

    public void sendQueue( String destinationName, Message message )
    {
        jmsQueueTemplate.send( new JmsQueue( destinationName ), session -> session.createTextMessage( renderService.toJsonAsString( message ) ) );
    }
}
