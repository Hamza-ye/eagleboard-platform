package com.mass3d.notification;

import com.mass3d.message.MessageConversationPriority;
import com.mass3d.message.MessageConversationStatus;

public class NotificationMessage
{
    private String subject = "";
    private String message = "";
    private MessageConversationPriority priority = MessageConversationPriority.NONE;
    private MessageConversationStatus status = MessageConversationStatus.NONE;

    public NotificationMessage( String subject, String message )
    {
        this.subject = subject;
        this.message = message;
    }

    public String getSubject()
    {
        return subject;
    }

    public String getMessage()
    {
        return message;
    }

    public MessageConversationPriority getPriority()
    {
        return priority;
    }

    public void setPriority( MessageConversationPriority priority )
    {
        this.priority = priority;
    }

    public MessageConversationStatus getStatus()
    {
        return status;
    }

    public void setStatus( MessageConversationStatus status )
    {
        this.status = status;
    }
}
