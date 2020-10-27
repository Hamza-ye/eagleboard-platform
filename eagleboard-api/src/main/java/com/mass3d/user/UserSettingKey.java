package com.mass3d.user;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.LocaleUtils;
import com.mass3d.common.DisplayProperty;

public enum UserSettingKey
{
    STYLE( "keyStyle" ),
    MESSAGE_EMAIL_NOTIFICATION( "keyMessageEmailNotification", true, Boolean.class ),
    MESSAGE_SMS_NOTIFICATION( "keyMessageSmsNotification", true, Boolean.class ),
    UI_LOCALE( "keyUiLocale", Locale.class ),
    DB_LOCALE( "keyDbLocale", Locale.class ),
    ANALYSIS_DISPLAY_PROPERTY( "keyAnalysisDisplayProperty", DisplayProperty.class ),
    TRACKER_DASHBOARD_LAYOUT( "keyTrackerDashboardLayout" );

    private final String name;

    private final Serializable defaultValue;

    private final Class<?> clazz;

    private static Map<String, Serializable> DEFAULT_USER_SETTINGS_MAP = Stream.of( UserSettingKey.values() ).filter( k -> k.getDefaultValue() != null )
        .collect( Collectors.toMap( UserSettingKey::getName, UserSettingKey::getDefaultValue ) );

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    UserSettingKey( String name )
    {
        this.name = name;
        this.defaultValue = null;
        this.clazz = String.class;
    }

    UserSettingKey( String name, Class<?> clazz )
    {
        this.name = name;
        this.defaultValue = null;
        this.clazz = clazz;
    }

    UserSettingKey( String name, Serializable defaultValue, Class<?> clazz )
    {
        this.name = name;
        this.defaultValue = defaultValue;
        this.clazz = clazz;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public Serializable getDefaultValue()
    {
        return defaultValue;
    }

    public boolean hasDefaultValue()
    {
        return defaultValue != null;
    }

    public static Optional<UserSettingKey> getByName( String name )
    {
        for ( UserSettingKey setting : UserSettingKey.values() )
        {
            if ( setting.getName().equals( name ) )
            {
                return Optional.of( setting );
            }
        }

        return Optional.empty();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Serializable getAsRealClass( String name, String value )
    {
        Optional<UserSettingKey> setting = getByName( name );

        if ( setting.isPresent() )
        {
            Class<?> settingClazz = setting.get().getClazz();

            if ( Double.class.isAssignableFrom( settingClazz ) )
            {
                return Double.valueOf( value );
            }
            else if ( Integer.class.isAssignableFrom( settingClazz ) )
            {
                return Integer.valueOf( value );
            }
            else if ( Boolean.class.isAssignableFrom( settingClazz ) )
            {
                return Boolean.valueOf( value );
            }
            else if ( Locale.class.isAssignableFrom( settingClazz ) )
            {
                return LocaleUtils.toLocale( value );
            }
            else if ( Enum.class.isAssignableFrom( settingClazz ) )
            {
                return Enum.valueOf( (Class<? extends Enum>) settingClazz, value.toUpperCase() );
            }

            //TODO handle Dates
        }

        return value;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public String getName()
    {
        return name;
    }

    public Class<?> getClazz()
    {
        return clazz;
    }

    public static Map<String, Serializable> getDefaultUserSettingsMap()
    {
        return new HashMap<>( DEFAULT_USER_SETTINGS_MAP );
    }

    public static Set<UserSetting> getDefaultUserSettings( User user )
    {
        Set<UserSetting> defaultUserSettings = new HashSet<>();
        DEFAULT_USER_SETTINGS_MAP.forEach( ( key, value ) -> {
            UserSetting userSetting = new UserSetting();
            userSetting.setName( key );
            userSetting.setValue( value );
            userSetting.setUser( user );
            defaultUserSettings.add( userSetting );
        } );
        return defaultUserSettings;
    }
}
