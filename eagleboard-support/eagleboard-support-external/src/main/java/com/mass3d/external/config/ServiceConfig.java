package com.mass3d.external.config;

import static com.mass3d.external.conf.ConfigurationKey.*;
import com.mass3d.external.conf.ConfigurationPropertyFactoryBean;
import com.mass3d.external.location.DefaultLocationManager;
import com.mass3d.external.location.LocationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration( "externalServiceConfig" )
@EnableAsync
@EnableScheduling
public class ServiceConfig
{
    @Bean
    public LocationManager locationManager()
    {
        return DefaultLocationManager.getDefault();
    }

    @Bean( "maxAttempts" )
    public ConfigurationPropertyFactoryBean maxAttempts()
    {
        return new ConfigurationPropertyFactoryBean( META_DATA_SYNC_RETRY );
    }

    @Bean( "initialInterval" )
    public ConfigurationPropertyFactoryBean initialInterval()
    {
        return new ConfigurationPropertyFactoryBean( META_DATA_SYNC_RETRY_TIME_FREQUENCY_MILLISEC );
    }

    @Bean( "sessionTimeout" )
    public ConfigurationPropertyFactoryBean sessionTimeout()
    {
        return new ConfigurationPropertyFactoryBean( SYSTEM_SESSION_TIMEOUT );
    }

}
