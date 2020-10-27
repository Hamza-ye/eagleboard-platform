package com.mass3d.i18n;

import java.util.Locale;

/**
 * @version $Id: I18nManager.java 6335 2008-11-20 11:11:26Z larshelg $
 */
public interface I18nManager
{
    String ID = I18nManager.class.getName();

    I18n getI18n();
    
    I18n getI18n(Locale locale);

    I18n getI18n(Class<?> clazz);

    I18n getI18n(Class<?> clazz, Locale locale);

    I18n getI18n(String clazzName);

    I18nFormat getI18nFormat();
}
