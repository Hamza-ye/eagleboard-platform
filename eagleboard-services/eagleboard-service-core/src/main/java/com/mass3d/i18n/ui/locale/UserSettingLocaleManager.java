package com.mass3d.i18n.ui.locale;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.mass3d.i18n.locale.LocaleManager;
import com.mass3d.i18n.ui.resourcebundle.ResourceBundleManager;
import com.mass3d.i18n.ui.resourcebundle.ResourceBundleManagerException;
import com.mass3d.user.UserSettingKey;
import com.mass3d.user.UserSettingService;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.i18n.locale.LocaleManager" )
public class UserSettingLocaleManager
    implements LocaleManager
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final UserSettingService userSettingService;

    private final ResourceBundleManager resourceBundleManager;

    public UserSettingLocaleManager( UserSettingService userSettingService,
        ResourceBundleManager resourceBundleManager )
    {
        checkNotNull( userSettingService );
        checkNotNull( resourceBundleManager );

        this.userSettingService = userSettingService;
        this.resourceBundleManager = resourceBundleManager;
    }

    // -------------------------------------------------------------------------
    // LocaleManager implementation
    // -------------------------------------------------------------------------

    @Override
    public Locale getCurrentLocale()
    {
        Locale locale = getUserSelectedLocale();

        if ( locale != null )
        {
            return locale;
        }

        return DEFAULT_LOCALE;
    }

    @Override
    public void setCurrentLocale( Locale locale )
    {
        userSettingService.saveUserSetting( UserSettingKey.UI_LOCALE, locale );
    }

    @Override
    public List<Locale> getLocalesOrderedByPriority()
    {
        List<Locale> locales = new ArrayList<>();

        Locale userLocale = getUserSelectedLocale();

        if ( userLocale != null )
        {
            locales.add( userLocale );
        }

        locales.add( DEFAULT_LOCALE );

        return locales;
    }

    private Locale getUserSelectedLocale()
    {
        return (Locale) userSettingService.getUserSetting( UserSettingKey.UI_LOCALE );
    }

    @Override
    public Locale getFallbackLocale()
    {
        return DEFAULT_LOCALE;
    }
    
    @Override
    public List<Locale> getAvailableLocales()
    {
        try
        {
            return resourceBundleManager.getAvailableLocales();
        }
        catch ( ResourceBundleManagerException ex )
        {
            throw new RuntimeException( ex );
        }
    }
}
