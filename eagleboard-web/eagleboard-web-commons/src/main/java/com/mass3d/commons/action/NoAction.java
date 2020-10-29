package com.mass3d.commons.action;

import com.opensymphony.xwork2.Action;

public class NoAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        return SUCCESS;
    }

}
