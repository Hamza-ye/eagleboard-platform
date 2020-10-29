package com.mass3d.commons.action;

import com.mass3d.user.User;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserService;

import com.opensymphony.xwork2.Action;

public class GetUserAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private UserService userService;

    public void setUserService( UserService userService )
    {
        this.userService = userService;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }
    
    private String username;

    public void setUsername( String username )
    {
        this.username = username;
    }

    private User user;

    public User getUser()
    {
        return user;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        if ( id != null )
        {
            user = userService.getUser( id );
        }
        else if ( username != null )
        {
            UserCredentials credentials = userService.getUserCredentialsByUsername( username );
            
            user = credentials != null ? credentials.getUserInfo() : null;
        }
        return SUCCESS;
    }
}
