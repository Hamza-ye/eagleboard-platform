package com.mass3d.notification;

public interface NotificationRecipient
{
    /**
     * Does the NotificationRecipient represent an 'external' recipient?
     *
     * Specifically:
     *  Does the recipient ultimately resolve to a DHIS2 ("internal") message recipient
     *  (User, UserGroup) or a piece of external contact information (phone number, e-mail address).
     */
    boolean isExternalRecipient();
}
