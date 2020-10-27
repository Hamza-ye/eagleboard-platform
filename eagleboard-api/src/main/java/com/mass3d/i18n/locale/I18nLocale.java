package com.mass3d.i18n.locale;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Locale;
import com.mass3d.common.BaseIdentifiableObject;

/**
 * Wrapper for java.util.Locale for persistence purposes.
 * 
 */
public class I18nLocale
    extends BaseIdentifiableObject
{    
    private String locale;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public I18nLocale()
    {        
        this.name = "English (United Kingdom)";
        this.locale = "en_GB";
    }

    public I18nLocale( Locale locale )
    {
        this.name = locale.getDisplayName();
        this.locale = locale.toString();
    }
    
    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    public String getLocale()
    {
        return locale;
    }
    
    public void setLocale( String locale )
    {
        this.locale = locale;
    }
}
