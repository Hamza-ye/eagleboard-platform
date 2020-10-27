package com.mass3d.security;

/**
 * Options for user account restore operation. These options are represented
 * in the user account restore email as a prefix to the restore token.
 * This token is hashed, and the hash is stored in the database. This means
 * that the options cannot be hacked to change them, because then the token
 * would no longer match the saved hash in the database.
 *
 */

public enum RestoreOptions
{
    RECOVER_PASSWORD_OPTION ( "R", RestoreType.RECOVER_PASSWORD, false ),
    INVITE_WITH_USERNAME_CHOICE ( "IC", RestoreType.INVITE, true ),
    INVITE_WITH_DEFINED_USERNAME ( "ID", RestoreType.INVITE, false );

    /**
     * Prefix to be used on restore token, to represent this set of options.
     */
    private final String tokenPrefix;

    /**
     * The type of restore operation to perform (i.e. password recovery
     * or invite to create account.)
     */
    private final RestoreType restoreType;

    /**
     * Defines whether the user can choose a username at the time of restore.
     */
    private final boolean usernameChoice;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    private RestoreOptions( String tokenPrefix, RestoreType restoreType, boolean usernameChoice )
    {
        this.tokenPrefix = tokenPrefix;
        this.restoreType = restoreType;
        this.usernameChoice = usernameChoice;
    }

    // -------------------------------------------------------------------------
    // Get Restore Options from a token string
    // -------------------------------------------------------------------------

    static public RestoreOptions getRestoreOptions( String token )
    {
        for ( RestoreOptions ro : RestoreOptions.values() )
        {
            if ( token.startsWith( ro.getTokenPrefix() ) )
            {
                return ro;
            }
        }

        return null;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public String getTokenPrefix()
    {
        return tokenPrefix;
    }

    public RestoreType getRestoreType()
    {
        return restoreType;
    }

    public boolean isUsernameChoice()
    {
        return usernameChoice;
    }
}
