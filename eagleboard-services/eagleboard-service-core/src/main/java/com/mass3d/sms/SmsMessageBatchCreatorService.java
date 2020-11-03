package com.mass3d.sms;

import java.util.List;
import java.util.stream.Collectors;
import com.mass3d.common.DeliveryChannel;
import com.mass3d.outboundmessage.OutboundMessage;
import com.mass3d.outboundmessage.OutboundMessageBatch;
import com.mass3d.program.message.MessageBatchCreatorService;
import com.mass3d.program.message.ProgramMessage;
import org.springframework.stereotype.Service;

@Service( "com.mass3d.sms.SmsMessageBatchCreatorService" )
public class SmsMessageBatchCreatorService
    implements MessageBatchCreatorService
{
    @Override
    public OutboundMessageBatch getMessageBatch( List<ProgramMessage> programMessages )
    {
        List<OutboundMessage> messages = programMessages.parallelStream()
            .filter( pm -> pm.getDeliveryChannels().contains( DeliveryChannel.SMS ) )
            .map(this::createSmsMessage)
            .collect( Collectors.toList() );

        return new OutboundMessageBatch( messages, DeliveryChannel.SMS );
    }

    private OutboundMessage createSmsMessage( ProgramMessage programMessage )
    {
        return new OutboundMessage( programMessage.getSubject(), programMessage.getText(),
                programMessage.getRecipients().getPhoneNumbers() );
    }
}
