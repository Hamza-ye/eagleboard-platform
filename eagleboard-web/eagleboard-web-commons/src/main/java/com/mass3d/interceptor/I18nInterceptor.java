package com.mass3d.interceptor;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import ognl.NoSuchPropertyException;
import ognl.Ognl;
import com.mass3d.common.UserContext;
import com.mass3d.i18n.I18n;
import com.mass3d.i18n.I18nFormat;
import com.mass3d.i18n.I18nManager;
import com.mass3d.i18n.locale.LocaleManager;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.user.UserSettingKey;
import com.mass3d.user.UserSettingService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @version $Id: WebWorkI18nInterceptor.java 6335 2008-11-20 11:11:26Z larshelg $
 */
public class I18nInterceptor
    implements Interceptor
{
    private static final String KEY_I18N = "i18n";
    private static final String KEY_I18N_FORMAT = "format";
    private static final String KEY_LOCALE = "locale";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private I18nManager i18nManager;

    public void setI18nManager( I18nManager manager )
    {
        i18nManager = manager;
    }

    private LocaleManager localeManager;

    public void setLocaleManager( LocaleManager localeManager )
    {
        this.localeManager = localeManager;
    }

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private UserSettingService userSettingService;

    public void setUserSettingService( UserSettingService userSettingService )
    {
        this.userSettingService = userSettingService;
    }


    // -------------------------------------------------------------------------
    // AroundInterceptor implementation
    // -------------------------------------------------------------------------

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
        Action action = (Action) invocation.getAction();

        I18n i18n = i18nManager.getI18n( action.getClass() );
        I18nFormat i18nFormat = i18nManager.getI18nFormat();
        Locale locale = localeManager.getCurrentLocale();

        // ---------------------------------------------------------------------
        // Make the objects available for web templates
        // ---------------------------------------------------------------------

        Map<String, Object> i18nMap = new HashMap<>( 3 );
        i18nMap.put( KEY_I18N, i18n );
        i18nMap.put( KEY_I18N_FORMAT, i18nFormat );
        i18nMap.put( KEY_LOCALE, locale );

        invocation.getStack().push( i18nMap );

        // ---------------------------------------------------------------------
        // Set the objects in the action class if the properties exist
        // ---------------------------------------------------------------------

        Map<?, ?> contextMap = invocation.getInvocationContext().getContextMap();

        try
        {
            Ognl.setValue( KEY_I18N, contextMap, action, i18n );
        }
        catch ( NoSuchPropertyException ignored )
        {
        }

        try
        {
            Ognl.setValue( KEY_I18N_FORMAT, contextMap, action, i18nFormat );
        }
        catch ( NoSuchPropertyException ignored )
        {
        }

        try
        {
            Ognl.setValue( KEY_LOCALE, contextMap, action, locale );
        }
        catch ( NoSuchPropertyException ignored )
        {
        }

        // ---------------------------------------------------------------------
        // Set the current User DB Locale in UserContext
        // ---------------------------------------------------------------------

        User currentUser = currentUserService.getCurrentUser();
        Locale dbLocale = (Locale) userSettingService.getUserSetting( UserSettingKey.DB_LOCALE, currentUser );

        UserContext.setUser( currentUser );
        UserContext.setUserSetting( UserSettingKey.DB_LOCALE, dbLocale );

        return invocation.invoke();
    }
}
