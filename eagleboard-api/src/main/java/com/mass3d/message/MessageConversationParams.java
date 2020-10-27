package com.mass3d.message;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.fileresource.FileResource;
import com.mass3d.user.User;

public class MessageConversationParams
{
    /* Required properties */

    private Set<User> recipients = new HashSet<>();

    private User sender;

    private String subject;

    private String text;

    private MessageType messageType;

    /* Optional properties */

    private String metadata;

    private User assignee;

    private MessageConversationPriority priority = MessageConversationPriority.NONE;

    private MessageConversationStatus status = MessageConversationStatus.NONE;

    private boolean forceNotification;
    
    private MessageConversationParams()
    {
    }

    private Set<FileResource> attachments;

    private MessageConversationParams( Collection<User> recipients, User sender, String subject, String text,
        MessageType messageType )
    {
        this.recipients = new HashSet<>( recipients );
        this.sender = sender;
        this.subject = subject;
        this.text = text;
        this.messageType = messageType;

        this.priority = MessageConversationPriority.NONE;
        this.status = MessageConversationStatus.NONE;
        this.forceNotification = false;
    }

    public Set<User> getRecipients()
    {
        return new HashSet<>( recipients );
    }

    public User getSender()
    {
        return sender;
    }

    public String getSubject()
    {
        return subject;
    }

    public String getText()
    {
        return text;
    }

    public MessageType getMessageType()
    {
        return messageType;
    }

    public String getMetadata()
    {
        return metadata;
    }

    public User getAssignee()
    {
        return assignee;
    }

    public MessageConversationPriority getPriority()
    {
        return priority;
    }

    public MessageConversationStatus getStatus()
    {
        return status;
    }

    public boolean isForceNotification()
    {
        return forceNotification;
    }

    public Set<FileResource> getAttachments() { return attachments; }

    public MessageConversation createMessageConversation()
    {
        MessageConversation conversation = new MessageConversation( subject, sender, messageType );

        conversation.setAssignee( assignee );
        conversation.setStatus( status );
        conversation.setPriority( priority );

        return conversation;
    }

    public static class Builder
    {
        private MessageConversationParams params;

        public Builder()
        {
            this.params = new MessageConversationParams();
        }
        
        public Builder( Collection<User> recipients, User sender, String subject, String text, MessageType messageType )
        {
            this.params = new MessageConversationParams( recipients, sender, subject, text, messageType );
        }
        
        public Builder withRecipients( Set<User> recipients )
        {
            this.params.recipients = new HashSet<>( recipients );
            return this;
        }
        
        public Builder withSender( User sender )
        {
            this.params.sender = sender;
            return this;
        }
        
        public Builder withSubject( String subject )
        {
            this.params.subject = subject;
            return this;
        }
        
        public Builder withText( String text )
        {
            this.params.text = text;
            return this;
        }
        
        public Builder withMessageType( MessageType messageType )
        {
            this.params.messageType = messageType;
            return this;
        }

        public Builder withMetaData( String metaData )
        {
            this.params.metadata = metaData;
            return this;
        }

        public Builder withAssignee( User assignee )
        {
            this.params.assignee = assignee;
            return this;
        }

        public Builder withPriority( MessageConversationPriority priority )
        {
            this.params.priority = priority;
            return this;
        }

        public Builder withStatus( MessageConversationStatus status )
        {
            this.params.status = status;
            return this;
        }

        public Builder withForceNotification( boolean forceNotification )
        {
            this.params.forceNotification = forceNotification;
            return this;
        }

        public Builder withAttachments( Set<FileResource> attachments )
        {
            this.params.attachments = attachments;
            return this;
        }

        public MessageConversationParams build()
        {
            return this.params;
        }
    }
}
