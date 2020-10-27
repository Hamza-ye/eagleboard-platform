package com.mass3d.user;

public enum UserInvitationStatus
{
    NONE( "none" ), ALL( "all" ), EXPIRED( "expired" );

    private final String value;

    UserInvitationStatus( String value )
    {
        this.value = value;
    }

    public static UserInvitationStatus fromValue( String value )
    {
        for ( UserInvitationStatus status : UserInvitationStatus.values() )
        {
            if ( status.value.equalsIgnoreCase( value ) )
            {
                return status;
            }
        }

        return null;
    }

    public String getValue()
    {
        return value;
    }
}
