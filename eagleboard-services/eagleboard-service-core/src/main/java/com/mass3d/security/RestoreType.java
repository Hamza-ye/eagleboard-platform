package com.mass3d.security;

import java.util.Calendar;

/**
 * Type of user account restore operation.
 *
 */

public enum RestoreType
{
    RECOVER_PASSWORD( Calendar.DAY_OF_MONTH, 2, "restore_message", "email_restore_subject", "restore.action" ),
    INVITE( Calendar.DAY_OF_YEAR, 7, "invite_message", "email_invite_subject", "invite.action" );

    /**
     * Type of Calendar interval before the restore expires.
     */
    private final int expiryIntervalType;

    /**
     * Count of Calendar intervals before the restore expires.
     */
    private final int expiryIntervalCount;

    /**
     * Name of the email template for this restore action type.
     */
    private final String emailTemplate;

    /**
     * Subject line of the email for this restore action type.
     */
    private final String emailSubject;

    /**
     * Return web action to put in the email message.
     */
    private final String action;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    private RestoreType( int expiryIntervalType, int expiryIntervalCount,
        String emailTemplate, String emailSubject, String action )
    {
        this.expiryIntervalType = expiryIntervalType;
        this.expiryIntervalCount = expiryIntervalCount;
        this.emailTemplate = emailTemplate;
        this.emailSubject = emailSubject;
        this.action = action;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public int getExpiryIntervalType()
    {
        return expiryIntervalType;
    }

    public int getExpiryIntervalCount()
    {
        return expiryIntervalCount;
    }

    public String getEmailTemplate()
    {
        return emailTemplate;
    }

    public String getEmailSubject()
    {
        return emailSubject;
    }

    public String getAction()
    {
        return action;
    }
}
