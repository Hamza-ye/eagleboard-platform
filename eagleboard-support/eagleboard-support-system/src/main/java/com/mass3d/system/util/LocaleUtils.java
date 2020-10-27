package com.mass3d.system.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocaleUtils
{
    private static final String SEP = "_";
    
    /**
     * Creates a Locale object based on the input string.
     *
     * @param localeStr String to parse
     * @return A locale object or null if not valid
     */
    public static Locale getLocale( String localeStr )
    {
        if ( localeStr == null || localeStr.isEmpty() )
        {
            return null;
        }
        else
        {
            return org.apache.commons.lang3.LocaleUtils.toLocale( localeStr );
        }
    }
        
    /**
     * Createa a locale string based on the given language, country and variant.
     * 
     * @param language the language, cannot be null.
     * @param country the country, can be null.
     * @param variant the variant, can be null.
     * @return a locale string.
     */
    public static String getLocaleString( String language, String country, String variant )
    {
        if ( language == null )
        {
            return null;
        }
        
        String locale = language;
        
        if ( country != null )
        {
            locale += SEP + country;
        }
        
        if ( variant != null )
        {
            locale += SEP + variant;
        }
        
        return locale;
    }
    
    /**
     * Creates a list of locales of all possible specifities based on the given
     * Locale. As an example, for the given locale "en_UK", the locales "en" and
     * "en_UK" are returned.
     * 
     * @param locale the Locale.
     * @return a list of locale strings.
     */
    public static List<String> getLocaleFallbacks( Locale locale )
    {
        List<String> locales = new ArrayList<>();
        
        locales.add( locale.getLanguage() );
        
        if ( !locale.getCountry().isEmpty() )
        {
            locales.add( locale.getLanguage() + SEP + locale.getCountry() );
        }
        
        if ( !locale.getVariant().isEmpty() )
        {
            locales.add( locale.toString() );
        }
        
        return locales;
    }
}
