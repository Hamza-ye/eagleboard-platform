package com.mass3d.useraccount.action;

import com.mass3d.setting.SystemSettingManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Action;

public class IsAccountRecoveryAllowedAction
    implements Action
{
    @Autowired
    private SystemSettingManager systemSettingManager;
    
    @Override
    public String execute()
    {
        boolean enabled = systemSettingManager.accountRecoveryEnabled();
        
        return enabled ? SUCCESS : ERROR;
    }
}
