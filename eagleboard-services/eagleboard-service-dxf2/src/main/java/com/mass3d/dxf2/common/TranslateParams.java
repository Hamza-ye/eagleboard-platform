package com.mass3d.dxf2.common;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

public class TranslateParams
{
    private boolean translate = true;

    private String locale;

    public TranslateParams()
    {
    }

    public TranslateParams( boolean translate )
    {
        this.translate = translate;
    }

    public TranslateParams( boolean translate, String locale )
    {
        this.translate = translate;
        this.locale = locale;
    }

    public boolean isTranslate()
    {
        return translate || !StringUtils.isEmpty( locale );
    }

    public void setTranslate( boolean translate )
    {
        this.translate = translate;
    }

    public Locale getLocaleWithDefault( Locale defaultLocale )
    {
        Locale locale = getLocale();
        return locale != null ? locale : defaultLocale;
    }

    public Locale getLocale()
    {
        try
        {
            return Locale.forLanguageTag( locale );
        }
        catch ( Exception ignored )
        {
        }

        return null;
    }

    public void setLocale( String locale )
    {
        this.locale = locale;
    }

    public boolean defaultLocale()
    {
        return StringUtils.isEmpty( locale );
    }
}
