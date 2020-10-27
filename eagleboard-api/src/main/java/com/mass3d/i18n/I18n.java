package com.mass3d.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class I18n
{    
    private ResourceBundle globalResourceBundle;

    private ResourceBundle specificResourceBundle;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public I18n( ResourceBundle globalResourceBundle, ResourceBundle specificResourceBundle )
    {
        this.globalResourceBundle = globalResourceBundle;
        this.specificResourceBundle = specificResourceBundle;
    }

    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------

    /**
     * Get a translated String for a given key for the currently selected locale
     *
     * @param key the key for a given translation
     * @return a translated String for a given key, or the key if no translation
     *          is found.
     */
    public String getString( String key )
    {
        String translation = key;

        if ( specificResourceBundle != null )
        {
            try
            {
                translation = specificResourceBundle.getString( key );
            }
            catch ( MissingResourceException ignored )
            {
            }
        }

        if ( translation.equals( key ) && globalResourceBundle != null )
        {
            try
            {
                translation = globalResourceBundle.getString( key );
            }
            catch ( MissingResourceException ignored )
            {
            }
        }

        return translation;
    }
}
