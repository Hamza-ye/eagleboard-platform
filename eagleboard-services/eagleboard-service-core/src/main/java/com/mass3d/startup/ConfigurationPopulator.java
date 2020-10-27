package com.mass3d.startup;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.configuration.Configuration;
import com.mass3d.configuration.ConfigurationService;
import com.mass3d.encryption.EncryptionStatus;
import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.system.startup.TransactionContextStartupRoutine;

@Slf4j
public class ConfigurationPopulator
    extends TransactionContextStartupRoutine
{
    private final ConfigurationService configurationService;

    private final DhisConfigurationProvider dhisConfigurationProvider;

    public ConfigurationPopulator( ConfigurationService configurationService,
        DhisConfigurationProvider dhisConfigurationProvider )
    {
        checkNotNull( configurationService );
        checkNotNull( dhisConfigurationProvider );

        this.configurationService = configurationService;
        this.dhisConfigurationProvider = dhisConfigurationProvider;
    }

    @Override
    public void executeInTransaction()
    {
        checkSecurityConfiguration();

        Configuration config = configurationService.getConfiguration();

        if ( config != null && config.getSystemId() == null )
        {
            config.setSystemId( UUID.randomUUID().toString() );
            configurationService.setConfiguration( config );
        }
    }

    private void checkSecurityConfiguration()
    {
        EncryptionStatus status = dhisConfigurationProvider.getEncryptionStatus();

        if ( !status.isOk() )
        {
            log.warn( "Encryption not configured: " + status.getKey() );
        }
        else
        {
            log.info( "Encryption is available" );
        }
    }
}
