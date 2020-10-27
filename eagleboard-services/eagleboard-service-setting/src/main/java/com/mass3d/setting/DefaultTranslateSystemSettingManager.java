package com.mass3d.setting;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Hashtable;
import java.util.Map;
import com.mass3d.util.ObjectUtils;
import org.springframework.stereotype.Service;

@Service( "com.mass3d.setting.TranslateSystemSettingManager" )
public class DefaultTranslateSystemSettingManager
    implements TranslateSystemSettingManager
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SystemSettingManager systemSettingManager;

    public DefaultTranslateSystemSettingManager( SystemSettingManager systemSettingManager )
    {
        checkNotNull( systemSettingManager );

        this.systemSettingManager = systemSettingManager;
    }

    // -------------------------------------------------------------------------
    // Method implementation
    // -------------------------------------------------------------------------

    @Override
    public Map<String, String> getTranslationSystemAppearanceSettings( String locale )
    {
        Map<String, String> translations = new Hashtable<>();

        translations.put( SettingKey.APPLICATION_TITLE.getName(), getSystemSettingWithFallbacks( SettingKey.APPLICATION_TITLE, locale, SettingKey.APPLICATION_TITLE.getDefaultValue().toString() ) );
        translations.put( SettingKey.APPLICATION_INTRO.getName(), getSystemSettingWithFallbacks( SettingKey.APPLICATION_INTRO, locale, EMPTY ) );
        translations.put( SettingKey.APPLICATION_NOTIFICATION.getName(), getSystemSettingWithFallbacks( SettingKey.APPLICATION_NOTIFICATION, locale, EMPTY ) );
        translations.put( SettingKey.APPLICATION_FOOTER.getName(), getSystemSettingWithFallbacks( SettingKey.APPLICATION_FOOTER ,locale, EMPTY ) );
        translations.put( SettingKey.APPLICATION_RIGHT_FOOTER.getName(), getSystemSettingWithFallbacks( SettingKey.APPLICATION_RIGHT_FOOTER, locale, EMPTY ) );

        return translations;
    }

    // -------------------------------------------------------------------------
    // Support Method implementation
    // -------------------------------------------------------------------------

    private String getSystemSettingWithFallbacks( SettingKey keyName, String localeStr, String defaultValue )
    {
        String settingValue = EMPTY;

        settingValue = (String) ObjectUtils.firstNonNull( systemSettingManager.getSystemSetting( keyName ), defaultValue );

        return settingValue;
    }
}
