package com.mass3d.artemis.audit;

import com.google.common.base.Strings;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.artemis.MessageManager;
import com.mass3d.audit.AuditScope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuditProducerSupplier
{
    private final MessageManager messageManager;
    private final Map<AuditScope, String> auditScopeDestinationMap;

    public AuditProducerSupplier(
        MessageManager messageManager,
        Map<AuditScope, String> auditScopeDestinationMap )
    {
        this.messageManager = messageManager;
        this.auditScopeDestinationMap = auditScopeDestinationMap;
    }

    public void publish( Audit audit )
    {
        String topic = getTopicName( audit );

        if ( !Strings.isNullOrEmpty( topic ) )
        {
            if ( log.isDebugEnabled() )
            {
                log.debug( "sending auditing message to topic: [" + topic + "] with content: " + audit.toLog() );
            }
            this.messageManager.send( topic, audit );
        }
        else
        {
            log.error( String.format( "Unable to map AuditScope [%s] to a topic name. Sending aborted",
                audit.getAuditScope() ) );
        }
    }

    private String getTopicName( Audit audit )
    {
        return auditScopeDestinationMap.get( audit.getAuditScope() );
    }
}
