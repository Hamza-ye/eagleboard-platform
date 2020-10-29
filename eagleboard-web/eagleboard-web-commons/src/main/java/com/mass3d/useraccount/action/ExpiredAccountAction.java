package com.mass3d.useraccount.action;

import com.opensymphony.xwork2.Action;
import org.apache.struts2.ServletActionContext;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class ExpiredAccountAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private UserService userService;

    @Autowired
    public void setUserService( UserService userService )
    {
        this.userService = userService;
    }

    // -------------------------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------------------------

    private String username;

    public String getUsername()
    {
        return username;
    }

    // -------------------------------------------------------------------------
    // Action Impl
    // -------------------------------------------------------------------------

    @Override
    public String execute() throws Exception
    {
        username = (String) ServletActionContext.getRequest().getSession().getAttribute( "username" );

        UserCredentials credentials = userService.getUserCredentialsByUsername( username );

        // check that the user is actually expired
        if ( credentials != null && !userService.credentialsNonExpired( credentials ) )
        {
            return SUCCESS;
        }

        return ERROR;
    }
}
