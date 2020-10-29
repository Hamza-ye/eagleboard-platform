package com.mass3d.useraccount.action;

import com.mass3d.security.RestoreType;
import com.mass3d.security.SecurityService;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Action;

public class IsRestoreTokenValidAction
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
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        UserCredentials credentials = userService.getUserCredentialsByUsername( username );
        
        if ( credentials == null )
        {
            return ERROR;
        }

        String errorMessage = securityService.verifyToken( credentials, token, RestoreType.RECOVER_PASSWORD );

        return errorMessage == null ? SUCCESS : ERROR;
    }
}
