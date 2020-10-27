package com.mass3d.system.startup;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.commons.util.DebugUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Implementation of {@link ServletContextListener} which hooks
 * into the context initialization and executes the configured
 * {@link StartupRoutineExecutor}.
 *
 */
@Slf4j
public class StartupListener
    implements ServletContextListener
{
    // -------------------------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------------------------

    @Override
    public void contextInitialized( ServletContextEvent event )
    {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext( event
            .getServletContext() );

        StartupRoutineExecutor startupRoutineExecutor = (StartupRoutineExecutor) applicationContext
            .getBean( StartupRoutineExecutor.ID );

        try
        {
            startupRoutineExecutor.execute();
        }
        catch ( Exception ex )
        {
            log.error( DebugUtils.getStackTrace( ex ) );

            throw new RuntimeException( "Failed to run startup routines: " + ex.getMessage(), ex );
        }
    }

    @Override
    public void contextDestroyed( ServletContextEvent event )
    {
        Enumeration<Driver> drivers = DriverManager.getDrivers();

        while ( drivers.hasMoreElements() )
        {
            Driver driver = drivers.nextElement();
            try
            {
                DriverManager.deregisterDriver( driver );
                log.info( "De-registering jdbc driver: " + driver );
            }
            catch ( SQLException e )
            {
                log.info( "Error de-registering driver " + driver + " :" + e.getMessage() );
            }
        }
    }
}
