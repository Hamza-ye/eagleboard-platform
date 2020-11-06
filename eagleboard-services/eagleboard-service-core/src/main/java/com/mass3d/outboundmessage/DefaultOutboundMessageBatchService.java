package com.mass3d.outboundmessage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.common.DeliveryChannel;
import com.mass3d.message.MessageSender;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class DefaultOutboundMessageBatchService
    implements OutboundMessageBatchService
{
    // ---------------------------------------------------------------------
    // Dependencies
    // ---------------------------------------------------------------------

    private Map<DeliveryChannel, MessageSender> messageSenders;

    public void setMessageSenders( Map<DeliveryChannel, MessageSender> messageSenders )
    {
        this.messageSenders = messageSenders;
    }

    // ---------------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------------

    public DefaultOutboundMessageBatchService()
    {
    }

    // ---------------------------------------------------------------------
    // OutboundMessageService implementation
    // ---------------------------------------------------------------------

    @Override
    @Transactional( readOnly = true )
    public List<OutboundMessageResponseSummary> sendBatches( List<OutboundMessageBatch> batches )
    {
        // Partition by channel (sender) first to avoid sender config checks
        return batches.stream()
            .collect( Collectors.groupingBy( OutboundMessageBatch::getDeliveryChannel ) )
            .entrySet().stream()
            .flatMap( entry -> entry.getValue().stream().map( this::send ) )
            .collect( Collectors.toList() );
    }

    // ---------------------------------------------------------------------
    // Supportive Methods
    // ---------------------------------------------------------------------

    private OutboundMessageResponseSummary send( OutboundMessageBatch batch )
    {
        DeliveryChannel channel = batch.getDeliveryChannel();
        MessageSender sender = messageSenders.get( channel );

        if ( sender == null )
        {
            String errorMessage = String
                .format( "No server/gateway found for delivery channel %s", channel );
            log.error( errorMessage );

            return new OutboundMessageResponseSummary(
                errorMessage,
                channel,
                OutboundMessageBatchStatus.FAILED
            );
        }
        else if ( !sender.isConfigured() )
        {
            String errorMessage = String
                .format( "Server/gateway for delivery channel %s is not configured", channel );
            log.error( errorMessage );

            return new OutboundMessageResponseSummary(
                errorMessage,
                channel,
                OutboundMessageBatchStatus.FAILED
            );
        }

        log.info( "Invoking message sender: " + sender.getClass().getSimpleName() );

        return sender.sendMessageBatch( batch );
    }
}
