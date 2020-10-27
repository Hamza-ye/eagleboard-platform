package com.mass3d.external.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * This class maintains a static list of logged statements and check if the
 * statement was already logged before logging it again. It is useful to remove
 * duplicated log entries from code that runs multiple times (e.g. init
 * routines)
 *
 */
public abstract class LogOnceLogger
{
    private static Set<String> logged = ConcurrentHashMap.newKeySet();

    /**
     * Creates a log entry with a specific Log level. The entry will be logged only
     * once
     *
     * @param log The SLF4J log to use
     * @param level The SLF4J log level
     * @param logString The string to log
     */
    protected void log( Logger log, Level level, String logString )
    {
        if ( !logged.contains( logString ) )
        {
            switch ( level )
            {
            case ERROR:
                log.error( logString );
                break;
            case WARN:
                log.warn( logString );
                break;
            case INFO:
                log.info( logString );
                break;
            case DEBUG:
                log.debug( logString );
                break;
            case TRACE:
                log.trace( logString );
                break;
            }
            logged.add( logString );
        }
    }

    /**
     * Creates a log entry with WARN level. The entry will be logged only once
     *
     * @param log The SLF4J log to use
     * @param logString The string to log
     */
    protected void warn( Logger log, String logString, Exception exception )
    {
        if ( !logged.contains( logString ) )
        {
            log.warn( logString, exception );
            logged.add( logString );
        }
    }
}
