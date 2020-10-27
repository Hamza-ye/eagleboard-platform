package com.mass3d.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.apache.struts2.ServletActionContext;
import com.mass3d.system.database.DatabaseInfoProvider;
import com.mass3d.commons.util.TextUtils;

import java.util.HashMap;
import java.util.Map;

import static com.mass3d.util.ContextUtils.getCookieValue;

public class ContextInterceptor
    implements Interceptor
{
    private static final String KEY_IN_MEMORY_DATABASE = "inMemoryDatabase";
    private static final String KEY_TEXT_UTILS = "dhisTextUtils";
    private static final String KEY_CURRENT_PAGE = "keyCurrentPage";
    private static final String KEY_CURRENT_KEY = "keyCurrentKey";

    private DatabaseInfoProvider databaseInfoProvider;

    public void setDatabaseInfoProvider( DatabaseInfoProvider databaseInfoProvider )
    {
        this.databaseInfoProvider = databaseInfoProvider;
    }

    @Override
    public void destroy()
    {
    }

    @Override
    public void init()
    {
    }

    @Override
    public String intercept( ActionInvocation invocation )
        throws Exception
    {
        Map<String, Object> map = new HashMap<>();

        map.put( KEY_IN_MEMORY_DATABASE, databaseInfoProvider.isInMemory() );
        map.put( KEY_TEXT_UTILS, TextUtils.INSTANCE );
        map.put( KEY_CURRENT_PAGE, getCookieValue( ServletActionContext.getRequest(), "currentPage" ) );
        map.put( KEY_CURRENT_KEY, getCookieValue( ServletActionContext.getRequest(), "currentKey" ) );

        invocation.getStack().push( map );

        return invocation.invoke();
    }
}
