package com.mass3d.message;

import java.util.Collection;
import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.user.User;

public interface MessageConversationStore
    extends IdentifiableObjectStore<MessageConversation>
{
    /**
     * Returns a list of MessageConversations.
     * 
     * @param user the User for which the MessageConversations are sent to, or
     *        all if null.
     * @param first the first record number to return, or all if null.
     * @param max the max number of records to return, or all if null.
     * @return a list of MessageConversations.
     */
    List<MessageConversation> getMessageConversations(User user, MessageConversationStatus status,
        boolean followUpOnly, boolean unreadOnly, Integer first, Integer max);

    /**
     * Returns the MessageConversations given by the supplied UIDs.
     *
     * @param messageConversationUids the UIDs of the MessageConversations to get.
     * @return a collection of MessageConversations.
     */
    List<MessageConversation> getMessageConversations(Collection<String> messageConversationUids);

    long getUnreadUserMessageConversationCount(User user);

    int deleteMessages(User sender);

    int deleteUserMessages(User user);

    int removeUserFromMessageConversations(User lastSender);

    List<UserMessage> getLastRecipients(User user, Integer first, Integer max);
}
