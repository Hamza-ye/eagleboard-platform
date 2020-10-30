package com.mass3d.webportal.menu.action;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.webapi.utils.ContextUtils;
import com.mass3d.webportal.module.Module;
import com.mass3d.webportal.module.ModuleManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Action;

public class GetModulesAction
    implements Action
{
    @Autowired
    private ModuleManager manager;

    @Autowired
    private CurrentUserService currentUserService;
    
    private List<Module> modules;
    
    public List<Module> getModules()
    {
        return modules;
    }

    @Override
    public String execute()
        throws Exception
    {
        String contextPath = ContextUtils.getContextPath( ServletActionContext.getRequest() );
        
        modules = manager.getAccessibleMenuModulesAndApps( contextPath );

        User user = currentUserService.getCurrentUser();
        
        if ( user != null && user.getApps() != null && !user.getApps().isEmpty() )
        {
            final List<String> userApps = new ArrayList<>( user.getApps() );
            
            Collections.sort( modules, new Comparator<Module>()
            {
                @Override
                public int compare( Module m1, Module m2 )
                {
                    int i1 = userApps.indexOf( m1.getName() );
                    int i2 = userApps.indexOf( m2.getName() );

                    i1 = i1 == -1 ? 9999 : i1;
                    i2 = i2 == -1 ? 9999 : i2;
                    
                    return Integer.valueOf( i1 ).compareTo( Integer.valueOf( i2 ) );
                }
            } );
        }
        
        return SUCCESS;
    }
}
