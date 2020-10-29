package com.mass3d.useraccount.action;

import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.user.UserCredentials;

import com.opensymphony.xwork2.Action;

public class GetCurrentUserAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CurrentUserService currentUserService;

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private UserCredentials userCredentials;

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    public UserCredentials getUserCredentials()
    {
        return userCredentials;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        User user = currentUserService.getCurrentUser();

        userCredentials = user.getUserCredentials();

        return SUCCESS;
    }
}
