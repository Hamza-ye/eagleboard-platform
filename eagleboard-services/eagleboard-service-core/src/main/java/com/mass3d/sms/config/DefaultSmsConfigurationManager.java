package com.mass3d.sms.config;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import org.springframework.stereotype.Component;

/**
 * Manages the {@link SmsConfiguration} for the instance.
 */
@Component( "com.mass3d.sms.config.SmsConfigurationManager" )
public class DefaultSmsConfigurationManager
    implements SmsConfigurationManager
{
    private final SystemSettingManager systemSettingManager;

    public DefaultSmsConfigurationManager( SystemSettingManager systemSettingManager )
    {
        checkNotNull( systemSettingManager );

        this.systemSettingManager = systemSettingManager;
    }

    @Override
    public SmsConfiguration getSmsConfiguration()
    {
        return (SmsConfiguration) systemSettingManager.getSystemSetting( SettingKey.SMS_CONFIG );
    }

    @Override
    public void updateSmsConfiguration( SmsConfiguration config )
    {
        systemSettingManager.saveSystemSetting( SettingKey.SMS_CONFIG, config );
    }

    @Override
    public SmsGatewayConfig checkInstanceOfGateway( Class<?> clazz )
    {
        if ( getSmsConfiguration() == null )
        {
            SmsConfiguration smsConfig = new SmsConfiguration( true );
            updateSmsConfiguration( smsConfig );
        }

        for ( SmsGatewayConfig gateway : getSmsConfiguration().getGateways() )
        {
            if ( gateway.getClass().equals( clazz ) )
            {
                return gateway;
            }
        }

        return null;
    }
}
