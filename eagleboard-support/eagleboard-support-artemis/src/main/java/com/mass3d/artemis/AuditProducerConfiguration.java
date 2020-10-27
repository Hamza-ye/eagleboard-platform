package com.mass3d.artemis;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuditProducerConfiguration
{
    /**
     * if true, every Audit message is placed into an in-memory queue
     * before being dispatched to the Message Broker
     */
    private boolean useQueue;
}
