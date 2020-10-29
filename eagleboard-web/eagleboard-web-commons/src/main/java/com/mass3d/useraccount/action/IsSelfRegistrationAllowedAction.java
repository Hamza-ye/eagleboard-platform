package com.mass3d.useraccount.action;

import com.mass3d.configuration.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Action;

public class IsSelfRegistrationAllowedAction
    implements Action
{
    @Autowired
    private ConfigurationService configurationService;
    
    @Override
    public String execute()
        throws Exception
    {
        boolean allowed = configurationService.getConfiguration().selfRegistrationAllowed();
        
        return allowed ? SUCCESS : ERROR;
    }
}
