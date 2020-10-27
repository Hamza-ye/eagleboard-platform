package com.mass3d.i18n.locale;

import java.util.List;
import java.util.Locale;

public interface LocaleManager
{
    String ID = LocaleManager.class.getName();

    Locale DEFAULT_LOCALE = Locale.ENGLISH;

    Locale getCurrentLocale();

    void setCurrentLocale(Locale locale);

    List<Locale> getLocalesOrderedByPriority();

    Locale getFallbackLocale();
    
    List<Locale> getAvailableLocales();
}
