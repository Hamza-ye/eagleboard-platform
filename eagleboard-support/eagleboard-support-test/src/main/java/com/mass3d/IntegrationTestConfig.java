package com.mass3d;

import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.container.DhisPostgisContainerProvider;
import com.mass3d.container.DhisPostgreSQLContainer;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.DhisConfigurationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.JdbcDatabaseContainer;

@Slf4j
@Configuration
@Profile("test")
@ComponentScan( "com.mass3d" )
public class IntegrationTestConfig
{
    private static final String POSTGRES_DATABASE_NAME = "eagleboard";

    private static final String POSTGRES_CREDENTIALS = "eagleboard";

    @Bean( name = "dhisConfigurationProvider" )
    public DhisConfigurationProvider dhisConfigurationProvider()
    {
        PostgresDhisConfigurationProvider dhisConfigurationProvider = new PostgresDhisConfigurationProvider();
        JdbcDatabaseContainer<?> postgreSQLContainer = initContainer();

        Properties properties = new Properties();

        properties.setProperty( "connection.url", postgreSQLContainer.getJdbcUrl() );
        properties.setProperty( "connection.dialect", "com.mass3d.hibernate.dialect.DhisPostgresDialect" );
        properties.setProperty( "connection.driver_class", "org.postgresql.Driver" );
        properties.setProperty( "connection.username", postgreSQLContainer.getUsername() );
        properties.setProperty( "connection.password", postgreSQLContainer.getPassword() );
        properties.setProperty( ConfigurationKey.AUDIT_USE_INMEMORY_QUEUE_ENABLED.getKey(), "off" );
        properties.setProperty( "metadata.audit.persist", "on");
        properties.setProperty( "tracker.audit.persist", "on");
        properties.setProperty( "aggregate.audit.persist", "on");
        properties.setProperty( "audit.metadata", "CREATE;UPDATE;DELETE");
        properties.setProperty( "audit.tracker", "CREATE;UPDATE;DELETE");
        properties.setProperty( "audit.aggregate", "CREATE;UPDATE;DELETE");

        dhisConfigurationProvider.addProperties( properties );

        return dhisConfigurationProvider;
    }

    private JdbcDatabaseContainer<?> initContainer()
    {
        DhisPostgreSQLContainer<?> postgisContainer = ((DhisPostgreSQLContainer<?>) new DhisPostgisContainerProvider().newInstance())
            .appendCustomPostgresConfig( "max_locks_per_transaction=100" )
            .withDatabaseName( POSTGRES_DATABASE_NAME )
            .withUsername( POSTGRES_CREDENTIALS )
            .withPassword( POSTGRES_CREDENTIALS );

        postgisContainer.start();

        log.info( String.format( "PostGIS container initialized: %s", postgisContainer.getJdbcUrl() ) );

        return postgisContainer;
    }
}
