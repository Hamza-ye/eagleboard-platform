package com.mass3d.i18n;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.mass3d.i18n.locale.I18nLocale;

public interface I18nLocaleService
{
    /**
     * Returns available languages in a mapping between code and name.
     */
    Map<String, String> getAvailableLanguages();
    
    /**
     * Returns available countries in a mapping between code and name.
     */
    Map<String, String> getAvailableCountries();
    
    I18nLocale addI18nLocale(String language, String country);
    
    void saveI18nLocale(I18nLocale locale);
    
    I18nLocale getI18nLocale(int id);
    
    I18nLocale getI18nLocaleByUid(String uid);
        
    I18nLocale getI18nLocale(Locale locale);
    
    void deleteI18nLocale(I18nLocale locale);
    
    int getI18nLocaleCount();
        
    List<Locale> getAllLocales();

    List<I18nLocale> getAllI18nLocales();
}
