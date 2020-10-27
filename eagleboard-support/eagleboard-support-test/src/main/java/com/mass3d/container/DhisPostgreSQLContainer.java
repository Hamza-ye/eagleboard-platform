package com.mass3d.container;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Custom {@link PostgreSQLContainer} that provides additional fluent API to
 * customize PostgreSQL configuration.
 *
 */
public class DhisPostgreSQLContainer<SELF extends DhisPostgreSQLContainer<SELF>> extends PostgreSQLContainer<SELF>
{
    private Set<String> customPostgresConfigs = new HashSet<>();

    public DhisPostgreSQLContainer( final String dockerImageName )
    {
        super( dockerImageName );
    }

    @Override
    protected void configure()
    {
        addExposedPort( POSTGRESQL_PORT );
        addEnv( "POSTGRES_DB", getDatabaseName() );
        addEnv( "POSTGRES_USER", getUsername() );
        addEnv( "POSTGRES_PASSWORD", getPassword() );
        setCommand( getPostgresCommandWithCustomConfigs() );
    }

    private String getPostgresCommandWithCustomConfigs()
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "postgres" );

        if ( !this.customPostgresConfigs.isEmpty() )
        {
            this.customPostgresConfigs.forEach( config -> {
                builder.append( " -c " );
                builder.append( config );
            } );
        }
        return builder.toString();
    }

    /**
     * Append custom postgres configuration to be customized when starting the
     * container. The configAndValue should be of the form
     * "configName=configValue". This method can be invoked multiple times to
     * add multiple custom commands.
     *
     * @param configAndValue The configuration and value of the form
     *        "configName=configValue"
     * @return the DhisPostgreSQLContainer
     */
    public SELF appendCustomPostgresConfig( String configAndValue )
    {
        if ( !StringUtils.isBlank( configAndValue ) )
        {
            this.customPostgresConfigs.add( configAndValue );
        }
        return self();
    }


}
