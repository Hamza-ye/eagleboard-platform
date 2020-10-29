package com.mass3d.about.action;

import com.opensymphony.xwork2.Action;

import org.apache.struts2.ServletActionContext;
import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import com.mass3d.webapi.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RedirectAction
    implements Action
{
    @Autowired
    private SystemSettingManager systemSettingManager;

//    @Autowired
//    private AppManager appManager;

    private String redirectUrl;

    public String getRedirectUrl()
    {
        return redirectUrl;
    }

    @Override
    public String execute()
        throws Exception
    {
        String startModule = (String) systemSettingManager.getSystemSetting( SettingKey.START_MODULE );

        String contextPath = (String) ContextUtils.getContextPath( ServletActionContext.getRequest() );

        if ( startModule != null && !startModule.trim().isEmpty() )
        {
            if ( startModule.startsWith( "app:" ) )
            {
//                List<App> apps = appManager.getApps( contextPath );
//
//                for ( App app : apps )
//                {
//                    if ( app.getShortName().equals( startModule.substring( "app:".length() ) ) )
//                    {
//                        redirectUrl = app.getLaunchUrl();
//                        return SUCCESS;
//                    }
//                }
            }
            else
            {
                redirectUrl = "../" + startModule + "/";
                return SUCCESS;
            }
        }

        redirectUrl = "../dhis-web-dashboard/";
        return SUCCESS;
    }
}
