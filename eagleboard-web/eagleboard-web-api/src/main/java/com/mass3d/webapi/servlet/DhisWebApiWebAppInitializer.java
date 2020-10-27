package com.mass3d.webapi.servlet;

import lombok.extern.slf4j.Slf4j;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.DefaultDhisConfigurationProvider;
import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.external.location.DefaultLocationManager;
import com.mass3d.system.startup.StartupListener;
import com.mass3d.webapi.security.config.WebMvcConfig;
import org.springframework.core.annotation.Order;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionTrackingMode;
import java.util.EnumSet;

@Order( 10 )
@Slf4j
public class DhisWebApiWebAppInitializer implements WebApplicationInitializer
{
    @Override
    public void onStartup( ServletContext servletContext )
    {
//        WebApplicationContext context = getContext();
//        servletContext.addListener(new ContextLoaderListener(context));

        boolean httpsOnly = getConfig().isEnabled( ConfigurationKey.SERVER_HTTPS );

        log.debug( String.format( "Configuring cookies, HTTPS only: %b", httpsOnly ) );

        if ( httpsOnly )
        {
            servletContext.getSessionCookieConfig().setSecure( true );
            servletContext.getSessionCookieConfig().setHttpOnly( true );

            log.info( "HTTPS only is enabled, cookies configured as secure" );
        }

        servletContext.setSessionTrackingModes( EnumSet.of( SessionTrackingMode.COOKIE ) );

        AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext = new AnnotationConfigWebApplicationContext();
        annotationConfigWebApplicationContext.register( WebMvcConfig.class );

        servletContext.addListener( new ContextLoaderListener( annotationConfigWebApplicationContext ) );

        ServletRegistration.Dynamic dispatcher = servletContext
            .addServlet( "dispatcher", new DispatcherServlet( annotationConfigWebApplicationContext ) );

        dispatcher.setAsyncSupported( true );
        dispatcher.setLoadOnStartup( 1 );
        dispatcher.addMapping( "/api/*" );
        dispatcher.addMapping( "/uaa/*" );

        FilterRegistration.Dynamic openSessionInViewFilter = servletContext.addFilter( "openSessionInViewFilter",
            OpenSessionInViewFilter.class );
        openSessionInViewFilter.setInitParameter( "sessionFactoryBeanName", "sessionFactory" );
        openSessionInViewFilter.addMappingForUrlPatterns( null, false, "/*" );
        openSessionInViewFilter.addMappingForServletNames( null, false, "dispatcher" );

        FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter( "characterEncodingFilter",
            CharacterEncodingFilter.class );
        characterEncodingFilter.setInitParameter( "encoding", "UTF-8" );
        characterEncodingFilter.setInitParameter( "forceEncoding", "true" );
        characterEncodingFilter.addMappingForUrlPatterns( null, false, "/*" );
        characterEncodingFilter.addMappingForServletNames( null, false, "dispatcher" );

        servletContext.addFilter( "RequestIdentifierFilter", new DelegatingFilterProxy( "requestIdentifierFilter" ) )
            .addMappingForUrlPatterns( null, true, "/*" );

        servletContext.addFilter( "AppOverrideFilter", new DelegatingFilterProxy( "appOverrideFilter" ) )
            .addMappingForUrlPatterns( null, true, "/*" );

        servletContext.addListener( new StartupListener() );
    }

    private DhisConfigurationProvider getConfig()
    {
        DefaultLocationManager locationManager = DefaultLocationManager.getDefault();
        locationManager.init();
        DefaultDhisConfigurationProvider configProvider = new DefaultDhisConfigurationProvider( locationManager );
        configProvider.init();

        return configProvider;
    }

    private AnnotationConfigWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("com.mass3d.webapi.security");
        return context;
    }
}