package com.mass3d.program.notification;

import com.mass3d.notification.NotificationRecipient;

public enum ProgramNotificationRecipient
    implements NotificationRecipient
{
    TRACKED_ENTITY_INSTANCE( true ),
    ORGANISATION_UNIT_CONTACT( true ),
    USERS_AT_ORGANISATION_UNIT( false ),
    USER_GROUP( false ),
    PROGRAM_ATTRIBUTE( true ),
    DATA_ELEMENT( true );

    private boolean external;

    ProgramNotificationRecipient( boolean external )
    {
        this.external = external;
    }

    @Override
    public boolean isExternalRecipient()
    {
        return external;
    }
}
