package com.mass3d.useraccount.action;

import com.mass3d.security.RestoreOptions;
import com.mass3d.security.RestoreType;
import com.mass3d.security.SecurityService;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Action;

public class IsInviteTokenValidAction
    implements Action
{
    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String username;

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    private String token;

    public String getToken()
    {
        return token;
    }

    public void setToken( String token )
    {
        this.token = token;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private UserCredentials userCredentials;

    public UserCredentials getUserCredentials()
    {
        return userCredentials;
    }

    private final String accountAction = "invited";

    public String getAccountAction()
    {
        return accountAction;
    }

    private String usernameChoice;

    public String getUsernameChoice()
    {
        return usernameChoice;
    }

    private String email;

    public String getEmail()
    {
        return email;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        userCredentials = userService.getUserCredentialsByUsername( username );

        if ( userCredentials == null )
        {
            return ERROR;
        }

        email = userCredentials.getUserInfo().getEmail();

        RestoreOptions restoreOptions = securityService.getRestoreOptions( token );

        if ( restoreOptions != null )
        {
            usernameChoice = Boolean.toString( restoreOptions.isUsernameChoice() );
        }

        String errorMessage = securityService.verifyToken( userCredentials, token, RestoreType.INVITE );

        return errorMessage == null ? SUCCESS : ERROR;
    }
}
