package com.mass3d.webapi.mvc.interceptor;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mass3d.common.UserContext;
import com.mass3d.dxf2.common.TranslateParams;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.user.UserSettingKey;
import com.mass3d.user.UserSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class TranslationInterceptor extends HandlerInterceptorAdapter
{
    private static String PARAM_TRANSLATE = "translate";

    private static String PARAM_LOCALE = "locale";

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private UserSettingService userSettingService;

    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception
    {
        boolean translate = !"false".equals( request.getParameter( PARAM_TRANSLATE ) );
        String locale = request.getParameter( PARAM_LOCALE );

        User user = currentUserService.getCurrentUser();
        setUserContext( user, new TranslateParams( translate, locale ) );

        return true;
    }

    private void setUserContext( User user, TranslateParams translateParams )
    {
        Locale dbLocale = getLocaleWithDefault( translateParams, user );
        UserContext.setUser( user );
        UserContext.setUserSetting( UserSettingKey.DB_LOCALE, dbLocale );
    }

    private Locale getLocaleWithDefault( TranslateParams translateParams, User user )
    {
        return translateParams.isTranslate() ?
            translateParams.getLocaleWithDefault( (Locale) userSettingService.getUserSetting( UserSettingKey.DB_LOCALE, user ) ) : null;
    }
}
