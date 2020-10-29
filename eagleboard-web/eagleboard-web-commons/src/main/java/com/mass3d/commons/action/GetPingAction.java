package com.mass3d.commons.action;

import com.opensymphony.xwork2.Action;

public class GetPingAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private Boolean loggedIn = true;

    public Boolean isLoggedIn()
    {
        return loggedIn;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        loggedIn = true;

        return SUCCESS;
    }
}
