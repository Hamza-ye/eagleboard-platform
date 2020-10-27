package com.mass3d.artemis.config;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TopicSession;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.stereotype.Component;

@Component
public class NameDestinationResolver implements DestinationResolver
{
    @Override
    public Destination resolveDestinationName( Session session, String destinationName, boolean pubSubDomain )
        throws JMSException
    {
        if ( pubSubDomain )
        {
            return resolveTopic( session, destinationName );
        }
        else
        {
            return resolveQueue( session, destinationName );
        }
    }

    private Destination resolveTopic( Session session, String topicName )
        throws JMSException
    {
        if ( session instanceof TopicSession )
        {
            // Cast to TopicSession: will work on both JMS 1.1 and 1.0.2
            return session.createTopic( topicName );
        }
        else
        {
            // Fall back to generic JMS Session: will only work on JMS 1.1
            return session.createTopic( topicName );
        }
    }

    private Queue resolveQueue( Session session, String queueName )
        throws JMSException
    {
        if ( session instanceof QueueSession )
        {
            // Cast to QueueSession: will work on both JMS 1.1 and 1.0.2
            return session.createQueue( queueName );
        }
        else
        {
            // Fall back to generic JMS Session: will only work on JMS 1.1
            return session.createQueue( queueName );
        }
    }
}