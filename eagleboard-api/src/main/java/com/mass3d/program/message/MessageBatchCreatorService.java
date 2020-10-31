package com.mass3d.program.message;

import java.util.List;
import com.mass3d.outboundmessage.OutboundMessageBatch;

public interface MessageBatchCreatorService
{
    /**
     * Create batch of messages based on DeliveryChannel. It also populates
     * required fields for that DeliveryChannel.
     *
     * @param programMessages list of ProgramMessages.
     */
    OutboundMessageBatch getMessageBatch(List<ProgramMessage> programMessages);
}
