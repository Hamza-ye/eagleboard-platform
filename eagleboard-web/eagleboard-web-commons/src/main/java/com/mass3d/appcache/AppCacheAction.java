package com.mass3d.appcache;

import com.opensymphony.xwork2.Action;
import com.mass3d.system.SystemInfo;
import com.mass3d.system.SystemService;
import com.mass3d.user.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;

public class AppCacheAction implements Action
{
    private CurrentUserService currentUserService;

    @Autowired
    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private SystemService systemService;

    @Autowired
    public void setSystemService( SystemService systemService )
    {
        this.systemService = systemService;
    }

    private String username;

    public String getUsername()
    {
        return username;
    }

    private SystemInfo systemInfo;

    public SystemInfo getSystemInfo()
    {
        return systemInfo;
    }

    @Override
    public String execute() throws Exception
    {
        username = currentUserService.getCurrentUsername();

        systemInfo = systemService.getSystemInfo();

        return SUCCESS;
    }
}
