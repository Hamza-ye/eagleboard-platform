package com.mass3d.message;

import java.util.Set;
import java.util.concurrent.Future;
import com.mass3d.outboundmessage.OutboundMessageBatch;
import com.mass3d.outboundmessage.OutboundMessageResponse;
import com.mass3d.outboundmessage.OutboundMessageResponseSummary;
import com.mass3d.user.User;
import org.springframework.util.concurrent.ListenableFuture;

public interface MessageSender
{
    /**
     * Sends a message. The given message will be sent to the given set of
     * users.
     * 
     * @param subject the message subject.
     * @param text the message text.
     * @param footer the message footer. Optionally included by the implementation.
     * @param users the users to send the message to.
     * @param forceSend force sending the message despite user settings.
     */
    OutboundMessageResponse sendMessage(String subject, String text, String footer, User sender,
        Set<User> users, boolean forceSend);
    
    Future<OutboundMessageResponse> sendMessageAsync(String subject, String text, String footer,
        User sender, Set<User> users, boolean forceSend);

    OutboundMessageResponse sendMessage(String subject, String text, Set<String> recipient);

    OutboundMessageResponse sendMessage(String subject, String text, String recipient);

    /**
     * Sends message batch based on DeliveryChannels configured.
     * @param batch batch of messages to be processed.
     */
    OutboundMessageResponseSummary sendMessageBatch(OutboundMessageBatch batch);

    /**
     * sends message batch asynchronously
     */
    ListenableFuture<OutboundMessageResponseSummary> sendMessageBatchAsync(
        OutboundMessageBatch batch);

    /**
     * To check if given service is configured and ready to use.
     */
    boolean isConfigured();
}
