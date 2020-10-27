package com.mass3d.i18n.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Locale;
import java.util.ResourceBundle;
import com.mass3d.i18n.I18n;
import com.mass3d.i18n.I18nFormat;
import com.mass3d.i18n.I18nManager;
import com.mass3d.i18n.locale.LocaleManager;
import com.mass3d.i18n.ui.resourcebundle.ResourceBundleManager;
import com.mass3d.i18n.ui.resourcebundle.ResourceBundleManagerException;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.i18n.I18nManager" )
public class DefaultI18nManager
    implements I18nManager
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final ResourceBundleManager resourceBundleManager;

    private final LocaleManager localeManager;

    public DefaultI18nManager( ResourceBundleManager resourceBundleManager, LocaleManager localeManager )
    {
        checkNotNull( resourceBundleManager );
        checkNotNull( localeManager );

        this.resourceBundleManager = resourceBundleManager;
        this.localeManager = localeManager;
    }

    // -------------------------------------------------------------------------
    // I18nManager implementation
    // -------------------------------------------------------------------------

    @Override
    public I18n getI18n()
    {
        Locale locale = getCurrentLocale();
        
        return new I18n( getGlobalResourceBundle( locale ), null );
    }

    @Override
    public I18n getI18n( Locale locale )
    {
        return new I18n( getGlobalResourceBundle( locale ), null );
    }

    @Override
    public I18n getI18n( Class<?> clazz )
    {
        return new I18n( getGlobalResourceBundle(), getSpecificResourceBundle( clazz.getName() ) );
    }

    @Override
    public I18n getI18n( Class<?> clazz, Locale locale )
    {
        return new I18n( getGlobalResourceBundle( locale ), getSpecificResourceBundle( clazz.getName(), locale ) );
    }

    @Override
    public I18n getI18n( String clazzName )
    {
        return new I18n( getGlobalResourceBundle(), getSpecificResourceBundle( clazzName ) );
    }
    
    @Override
    public I18nFormat getI18nFormat()
    {
        I18nFormat formatter = new I18nFormat( getGlobalResourceBundle() );

        formatter.init();

        return formatter;
    }

    // -------------------------------------------------------------------------
    // Support methods
    // -------------------------------------------------------------------------

    private ResourceBundle getGlobalResourceBundle()
    {
        return getGlobalResourceBundle( getCurrentLocale() );
    }
    
    private ResourceBundle getGlobalResourceBundle( Locale locale )
    {
        try
        {
            return resourceBundleManager.getGlobalResourceBundle( locale );
        }
        catch ( ResourceBundleManagerException e )
        {
            throw new RuntimeException( "Failed to get global resource bundle", e );
        }
    }

    private ResourceBundle getSpecificResourceBundle( String clazzName )
    {
        return resourceBundleManager.getSpecificResourceBundle( clazzName, getCurrentLocale() );
    }
    
    private ResourceBundle getSpecificResourceBundle( String clazzName, Locale locale )
    {
        return resourceBundleManager.getSpecificResourceBundle( clazzName, locale );
    }

    private Locale getCurrentLocale()
    {
        return localeManager.getCurrentLocale();
    }
}
