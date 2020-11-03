package com.mass3d.sms.config;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.common.CodeGenerator;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service( "com.mass3d.sms.config.GatewayAdministrationService" )
public class DefaultGatewayAdministrationService
    implements GatewayAdministrationService
{
    private Map<String, SmsGatewayConfig> gatewayConfigurationMap = new HashMap<>();

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final SmsConfigurationManager smsConfigurationManager;

    public DefaultGatewayAdministrationService( SmsConfigurationManager smsConfigurationManager )
    {
        checkNotNull( smsConfigurationManager );

        this.smsConfigurationManager = smsConfigurationManager;
    }

    // -------------------------------------------------------------------------
    // GatewayAdministrationService implementation
    // -------------------------------------------------------------------------

    @EventListener
    public void handleContextRefresh( ContextRefreshedEvent contextRefreshedEvent )
    {
        initializeSmsConfig();
    }

    @Override
    public void setDefaultGateway( String uid )
    {
        setDefaultGateway( getByUid( uid ) );
    }

    @Override
    public void setDefaultGateway( SmsGatewayConfig config )
    {
        SmsConfiguration configuration = getSmsConfiguration();

        List<SmsGatewayConfig> persistedConfigs = configuration.getGateways();

        List<SmsGatewayConfig> updatedConfigs = new ArrayList<>();

        for ( SmsGatewayConfig persisted : persistedConfigs )
        {
            if ( persisted.equals( config ) )
            {
                persisted.setDefault( true );
            }
            else
            {
                persisted.setDefault( false );
            }

            updatedConfigs.add( persisted );
        }

        configuration.setGateways( updatedConfigs );

        smsConfigurationManager.updateSmsConfiguration( configuration );
        initializeSmsConfig();
    }

    @Override
    public boolean addOrUpdateGateway( SmsGatewayConfig payLoad, Class<?> klass )
    {
        SmsConfiguration smsConfiguration = getSmsConfiguration();

        if ( smsConfiguration != null )
        {
            SmsGatewayConfig gatewayConfig = smsConfigurationManager.checkInstanceOfGateway( klass );

            int index = -1;

            if ( gatewayConfig != null )
            {
                index = smsConfiguration.getGateways().indexOf( gatewayConfig );
            }

            payLoad.setUid( CodeGenerator.generateCode( 10 ) );
            gatewayConfig = payLoad;

            if ( smsConfiguration.getGateways() == null || smsConfiguration.getGateways().isEmpty() )
            {
                gatewayConfig.setDefault( true );
            }

            if ( index >= 0 )
            {
                smsConfiguration.getGateways().set( index, gatewayConfig );

                gatewayConfigurationMap.put( gatewayConfig.getName(), gatewayConfig );
            }
            else
            {
                smsConfiguration.getGateways().add( gatewayConfig );

                gatewayConfigurationMap.put( gatewayConfig.getName(), gatewayConfig );
            }

            smsConfigurationManager.updateSmsConfiguration( smsConfiguration );
            initializeSmsConfig();

            return true;
        }

        return false;
    }

    @Override
    public boolean addGateway( SmsGatewayConfig config )
    {
        if ( config != null )
        {
            config.setUid( CodeGenerator.generateCode( 10 )  );

            SmsConfiguration smsConfiguration = getSmsConfiguration();

            if ( smsConfiguration.getGateways().isEmpty() )
            {
                config.setDefault( true );
            }
            else
            {
                config.setDefault( false );
            }

            smsConfiguration.getGateways().add( config );

            smsConfigurationManager.updateSmsConfiguration( smsConfiguration );
            initializeSmsConfig();

            return true;
        }

        return false;
    }

    @Override
    public void updateGateway( SmsGatewayConfig persisted, SmsGatewayConfig updatedConfig )
    {
        if ( persisted == null || updatedConfig == null )
        {
            log.warn( "Gateway configurations cannot be null" );
            return;
        }

        updatedConfig.setUid( persisted.getUid() );
        updatedConfig.setDefault( persisted.isDefault() );

        SmsConfiguration configuration = getSmsConfiguration();

        configuration.getGateways().remove( persisted );

        configuration.getGateways().add( updatedConfig );

        smsConfigurationManager.updateSmsConfiguration( configuration );
    }

    @Override
    public boolean removeGatewayByUid( String uid )
    {
        SmsConfiguration smsConfiguration = getSmsConfiguration();

        for ( SmsGatewayConfig gateway : smsConfiguration.getGateways() )
        {
            if ( gateway.getUid().equals( uid ) )
            {
                smsConfiguration.getGateways().remove( gateway );

                if( gateway.isDefault() )
                {
                    if (  !smsConfiguration.getGateways().isEmpty() )
                    {
                        smsConfiguration.getGateways().get( 0 ).setDefault( true );
                    }
                }

                smsConfigurationManager.updateSmsConfiguration( smsConfiguration );
                initializeSmsConfig();

                return true;
            }
        }

        return false;
    }

    @Override
    public SmsGatewayConfig getByUid( String uid )
    {
        List<SmsGatewayConfig> list = getSmsConfiguration().getGateways();

        if ( !list.isEmpty() )
        {
            for ( SmsGatewayConfig gw : list )
            {
                if ( gw.getUid().equals( uid ) )
                {
                    return gw;
                }
            }
        }

        return null;
    }

    @Override
    public SmsGatewayConfig getDefaultGateway()
    {
        List<SmsGatewayConfig> list = getSmsConfiguration().getGateways();

        if (  !list.isEmpty() )
        {
            for ( SmsGatewayConfig gw : list )
            {
                if ( gw.isDefault() )
                {
                    return gw;
                }
            }
        }

        return null;
    }

    @Override
    public boolean hasDefaultGateway()
    {
        return getDefaultGateway() != null;
    }

    @Override
    public boolean loadGatewayConfigurationMap( SmsConfiguration smsConfiguration )
    {
        gatewayConfigurationMap.clear();

        List<SmsGatewayConfig> gatewayList = smsConfiguration.getGateways();

        if ( !gatewayList.isEmpty() )
        {
            for ( SmsGatewayConfig smsGatewayConfig : gatewayList )
            {
                gatewayConfigurationMap.put( smsGatewayConfig.getName(), smsGatewayConfig );
            }

            return true;
        }

        return false;
    }

    @Override
    public Class<? extends SmsGatewayConfig> getGatewayType( SmsGatewayConfig config )
    {
        if ( config == null )
        {
            return null;
        }

        SmsConfiguration configuration = getSmsConfiguration();

        for ( SmsGatewayConfig gatewayConfig : configuration.getGateways() )
        {
            if ( gatewayConfig.getUid().equals( config.getUid() ) )
            {
                return gatewayConfig.getClass();
            }
        }

        return null;
    }

    @Override
    public Map<String, SmsGatewayConfig> getGatewayConfigurationMap()
    {
        return gatewayConfigurationMap;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private SmsConfiguration getSmsConfiguration()
    {
        SmsConfiguration smsConfiguration = smsConfigurationManager.getSmsConfiguration();

        if ( smsConfiguration != null )
        {
            return smsConfiguration;
        }

        return new SmsConfiguration();
    }

    private void initializeSmsConfig()
    {
        SmsConfiguration smsConfiguration = getSmsConfiguration();

        if ( smsConfiguration == null )
        {
            log.info( "SMS configuration not found" );
            return;
        }

        List<SmsGatewayConfig> gatewayList = smsConfiguration.getGateways();

        if ( gatewayList == null || gatewayList.isEmpty() )
        {
            log.info( "No Gateway configuration not found" );

            loadGatewayConfigurationMap( smsConfiguration );
            return;
        }

        log.info( "Gateway configuration found: " + gatewayList );

        loadGatewayConfigurationMap( smsConfiguration );
    }
}
