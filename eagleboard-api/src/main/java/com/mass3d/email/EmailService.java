package com.mass3d.email;

import java.util.Set;
import com.mass3d.outboundmessage.OutboundMessageResponse;

public interface EmailService
{
    /**
     * Indicates whether email is configured.
     * 
     * @return true if email is configured.
     */
    boolean emailConfigured();

    /**
     * Sends an email to the recipient user from the sender.
     *
     * @param email the email to send.
     */
    OutboundMessageResponse sendEmail(Email email);

    /**
     * Sends an email to the list of recipient users from the sender.
     *
     * @param subject the subject.
     * @param message the message.
     * @param recipients the recipients.
     * @return the {@link OutboundMessageResponse}.
     */
    OutboundMessageResponse sendEmail(String subject, String message, Set<String> recipients);

    /**
     * Sends an automatically generated email message to the current user.
     * Useful for testing the SMTP configuration of the system.
     * 
     * @return the {@link OutboundMessageResponse}.
     */
    OutboundMessageResponse sendTestEmail();
    
    /**
     * Sends an email using the system notification email as recipient. Requires
     * that a valid system notification email address has been specified. Only
     * the subject and text properties of the given email are read.
     * 
     * @param email the email to send.
     * @return the {@link OutboundMessageResponse}.
     */
    OutboundMessageResponse sendSystemEmail(Email email);
}
