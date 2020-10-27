package com.mass3d.system.log;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.external.location.LocationManager;
import org.springframework.stereotype.Component;

/**
 * This class adds new Logger(s) and RollingFileAppender(s) to the XML-based, default Log4J configuration.
 * The goal is to create a number of scoped log files, each for different areas of the application. The scope
 * is defined by package name.
 *
 * Additionally this class also attach a RollingFileAppender to the Root logger.
 *
 */
@Slf4j
@Component( "logInitializer" )
public class Log4JLogConfigInitializer
    implements LogConfigInitializer
{
    private PatternLayout PATTERN_LAYOUT = PatternLayout.newBuilder().withPattern( "* %-5p %d{ISO8601} %m (%F [%t])%n").build();

    private static final String LOG_DIR = "logs";
    private static final String ANALYTICS_TABLE_LOGGER_FILENAME = "dhis-analytics-table.log";
    private static final String DATA_EXCHANGE_LOGGER_FILENAME = "dhis-data-exchange.log";
    private static final String DATA_SYNC_LOGGER_FILENAME = "dhis-data-sync.log";
    private static final String METADATA_SYNC_LOGGER_FILENAME = "dhis-metadata-sync.log";
    private static final String GENERAL_LOGGER_FILENAME = "dhis.log";
    private static final String PUSH_ANALYSIS_LOGGER_FILENAME = "dhis-push-analysis.log";
    private static final String LOG4J_CONF_PROP = "log4j.configuration";

    private final LocationManager locationManager;

    private final DhisConfigurationProvider config;

    public Log4JLogConfigInitializer( LocationManager locationManager, DhisConfigurationProvider config )
    {
        checkNotNull( locationManager );
        checkNotNull( config );
        this.locationManager = locationManager;
        this.config = config;
    }

    @PostConstruct
    @Override
    public void initConfig()
    {
        if ( !locationManager.externalDirectorySet() )
        {
            log.warn( "Could not initialize additional log configuration, external home directory not set" );
            return;
        }

        if ( isNotBlank( System.getProperty( LOG4J_CONF_PROP ) ) )
        {
            log.info( "Aborting default log config, external config set through system prop " + LOG4J_CONF_PROP + ": " + System
                .getProperty( LOG4J_CONF_PROP ) );
            return;
        }

        log.info( String.format( "Initializing Log4j, max file size: '%s', max file archives: %s",
            config.getProperty( ConfigurationKey.LOGGING_FILE_MAX_SIZE ),
            config.getProperty( ConfigurationKey.LOGGING_FILE_MAX_ARCHIVES ) ) );

        locationManager.buildDirectory( LOG_DIR );

        configureLoggers( ANALYTICS_TABLE_LOGGER_FILENAME, Lists
            .newArrayList( "com.mass3d.resourcetable", "com.mass3d.analytics.table" ) );

        configureLoggers( DATA_EXCHANGE_LOGGER_FILENAME, Lists.newArrayList( "com.mass3d.dxf2" ) );

        configureLoggers( DATA_SYNC_LOGGER_FILENAME, Lists.newArrayList( "com.mass3d.dxf2.sync" ) );

        configureLoggers( METADATA_SYNC_LOGGER_FILENAME, Lists.newArrayList( "com.mass3d.dxf2.metadata" ) );

        configureLoggers( PUSH_ANALYSIS_LOGGER_FILENAME, Lists.newArrayList( "com.mass3d.pushanalysis" ) );

        configureRootLogger( GENERAL_LOGGER_FILENAME );

        final LoggerContext ctx = (LoggerContext) LogManager.getContext( false );
        ctx.updateLoggers();
    }

    /**
     * Configures rolling file loggers.
     *
     * @param filename the filename to output logging to.
     * @param loggers the logger names.
     */
    private void configureLoggers( String filename, List<String> loggers )
    {
        String file = getLogFile( filename );

        RollingFileAppender appender = getRollingFileAppender( file );

        getLogConfiguration().addAppender( appender );

        AppenderRef[] refs = createAppenderRef( "Ref_" + filename );

        for ( String loggerName : loggers )
        {
            LoggerConfig loggerConfig = LoggerConfig.createLogger( true, Level.INFO, loggerName, "true", refs, null,
                getLogConfiguration(), null );

            loggerConfig.addAppender(appender, null, null);

            getLogConfiguration().addLogger(loggerName, loggerConfig);

            log.info( "Added logger: " + loggerName + " using file: " + file );
        }
    }

    private AppenderRef[] createAppenderRef( String refName )
    {
        AppenderRef ref = AppenderRef.createAppenderRef( refName, Level.INFO, null );
        return new AppenderRef[] { ref };
    }

    private Configuration getLogConfiguration()
    {

        final LoggerContext ctx = (LoggerContext) LogManager.getContext( false );
        return ctx.getConfiguration();
    }

    /**
     * Configures a root file logger.
     *
     * @param filename the filename to output logging to.
     */
    private void configureRootLogger( String filename )
    {
        String file = getLogFile( filename );

        RollingFileAppender appender = getRollingFileAppender( file );

        getLogConfiguration().addAppender( appender );

        getLogConfiguration().getRootLogger().addAppender( getLogConfiguration().getAppender( appender.getName() ),
            Level.INFO, null );

        log.info( "Added root logger using file: " + file );
    }

    /**
     * Returns a rolling file appender.
     *
     * @param file the file to output to, including path and filename.
     */
    private RollingFileAppender getRollingFileAppender( String file )
    {
        RollingFileAppender appender = RollingFileAppender.newBuilder().withFileName( file )
            .setName("appender_" + file)
            .withFilePattern( file + "%i")
            .setLayout( PATTERN_LAYOUT )
            .withPolicy(
                SizeBasedTriggeringPolicy.createPolicy( config.getProperty( ConfigurationKey.LOGGING_FILE_MAX_SIZE ) ) )
            .withStrategy( DefaultRolloverStrategy.newBuilder()
                .withMax( config.getProperty( ConfigurationKey.LOGGING_FILE_MAX_ARCHIVES ) ).build() )
            .build();

        appender.start();
        return appender;
    }

    /**
     * Returns a file including path and filename.
     *
     * @param filename the filename to use for the file path.
     */
    private String getLogFile( String filename )
    {
        return locationManager.getExternalDirectoryPath() + File.separator + LOG_DIR + File.separator + filename;
    }
}
