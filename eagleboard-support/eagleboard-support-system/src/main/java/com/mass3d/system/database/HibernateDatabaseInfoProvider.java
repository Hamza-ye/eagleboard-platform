package com.mass3d.system.database;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.commons.util.SystemUtils;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.DhisConfigurationProvider;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component( "databaseInfoProvider" )
public class HibernateDatabaseInfoProvider
    implements DatabaseInfoProvider
{
    private static final String POSTGIS_MISSING_ERROR = "Postgis extension is not installed. Execute \"CREATE EXTENSION postgis;\" as a superuser and start the application again.";
    private static final String POSTGRES_VERSION_REGEX = "^([a-zA-Z_-]+ \\d+\\.+\\d+)?[ ,].*$";
    private static final Pattern POSTGRES_VERSION_PATTERN = Pattern.compile( POSTGRES_VERSION_REGEX );

    private DatabaseInfo info;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final DhisConfigurationProvider config;
    private final JdbcOperations jdbcTemplate;
    private final Environment environment;

    public HibernateDatabaseInfoProvider( DhisConfigurationProvider config, JdbcOperations jdbcTemplate,
        Environment environment )
    {
        checkNotNull( config );
        checkNotNull( jdbcTemplate );
        checkNotNull( environment );

        this.config = config;
        this.jdbcTemplate = jdbcTemplate;
        this.environment = environment;
    }
    
    @PostConstruct
    public void init()
    {
        checkDatabaseConnectivity();

        boolean spatialSupport = false;

        // Check if postgis is installed, fail startup if not

        if ( !SystemUtils.isTestRun(environment.getActiveProfiles()) )
        {
            spatialSupport = isSpatialSupport();

            if ( !spatialSupport )
            {
                log.error( POSTGIS_MISSING_ERROR );
                throw new IllegalStateException( POSTGIS_MISSING_ERROR );
            }
        }

        String url = config.getProperty( ConfigurationKey.CONNECTION_URL );
        String user = config.getProperty( ConfigurationKey.CONNECTION_USERNAME );
        String password = config.getProperty( ConfigurationKey.CONNECTION_PASSWORD );
        InternalDatabaseInfo internalDatabaseInfo = getInternalDatabaseInfo();

        info = new DatabaseInfo();
        info.setName( internalDatabaseInfo.getDatabase() );
        info.setUser( StringUtils.defaultIfEmpty( internalDatabaseInfo.getUser(), user ) );
        info.setPassword( password );
        info.setUrl( url );
        info.setSpatialSupport( spatialSupport );
        info.setDatabaseVersion( internalDatabaseInfo.getVersion() );
    }

    // -------------------------------------------------------------------------
    // DatabaseInfoProvider implementation
    // -------------------------------------------------------------------------

    @Override
    public DatabaseInfo getDatabaseInfo()
    {
        // parts of returned object may be reset due to security reasons
        // (clone must be created to preserve original values)
        return info == null ? null : info.instance();
    }

    @Override
    public boolean isInMemory()
    {
        return info.getUrl() != null && info.getUrl().contains( ":mem:" );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    @Nonnull
    private InternalDatabaseInfo getInternalDatabaseInfo()
    {
        if ( SystemUtils.isH2( environment.getActiveProfiles() ) )
        {
            return new InternalDatabaseInfo();
        }
        else
        {
            try
            {
                final InternalDatabaseInfo internalDatabaseInfo = jdbcTemplate
                    .queryForObject( "select version(),current_catalog,current_user", ( rs, rowNum ) -> {
                        String version = rs.getString( 1 );
                        final Matcher versionMatcher = POSTGRES_VERSION_PATTERN.matcher( version );

                        if ( versionMatcher.find() )
                        {
                            version = versionMatcher.group( 1 );
                        }

                        return new InternalDatabaseInfo( version, rs.getString( 2 ), rs.getString( 3 ) );
                    } );

                return internalDatabaseInfo == null ? new InternalDatabaseInfo() : internalDatabaseInfo;
            }
            catch ( Exception ex )
            {
                log.error( "An error occurred when retrieving database info.", ex );

                return new InternalDatabaseInfo();
            }
        }
    }

    private void checkDatabaseConnectivity()
    {
        jdbcTemplate.queryForObject( "select 'checking db connection';", String.class );
    }

    /**
     * Attempts to create a spatial database extension. Checks if spatial operations
     * are supported.
     */
    private boolean isSpatialSupport()
    {
        try
        {
            jdbcTemplate.execute( "create extension postgis;" );
        }
        catch ( Exception ex )
        {
        }

        try
        {
            String version = jdbcTemplate.queryForObject( "select postgis_full_version();", String.class );

            return version != null;
        }
        catch ( Exception ex )
        {
            log.error( "Exception when checking postgis_full_version(), PostGIS not available" );
            log.debug( "Exception when checking postgis_full_version()", ex );
            return false;
        }
    }

    protected static class InternalDatabaseInfo
    {
        private final String version;

        private final String database;

        private final String user;

        public InternalDatabaseInfo()
        {
            this( StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY );
        }

        public InternalDatabaseInfo( String version, String database, String user )
        {
            this.version = version;
            this.database = database;
            this.user = user;
        }

        public String getVersion()
        {
            return version;
        }

        public String getDatabase()
        {
            return database;
        }

        public String getUser()
        {
            return user;
        }
    }
}
