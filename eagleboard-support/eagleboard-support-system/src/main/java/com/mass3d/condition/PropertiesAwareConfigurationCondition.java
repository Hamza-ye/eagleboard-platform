package com.mass3d.condition;

import com.mass3d.commons.util.SystemUtils;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.DefaultDhisConfigurationProvider;
import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.external.config.ServiceConfig;
import com.mass3d.external.location.DefaultLocationManager;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;

/**
 * Loads the DHIS2 configuration provider within the context of a Spring
 * Configuration condition. This is required, since the
 * {@link DefaultDhisConfigurationProvider} is not available as Spring Bean when
 * the condition is evaluated.
 *
 */
public abstract class PropertiesAwareConfigurationCondition
    implements ConfigurationCondition
{
    protected DhisConfigurationProvider getConfiguration()
    {
        DefaultLocationManager locationManager = (DefaultLocationManager) new ServiceConfig().locationManager();
        locationManager.init();
        DefaultDhisConfigurationProvider dhisConfigurationProvider =
            new DefaultDhisConfigurationProvider( locationManager );
        dhisConfigurationProvider.init();

        return dhisConfigurationProvider;
    }

    protected boolean isTestRun( ConditionContext context )
    {
        return SystemUtils.isTestRun( context.getEnvironment().getActiveProfiles() );
    }

    protected boolean isAuditTest( ConditionContext context )
    {
        return SystemUtils.isAuditTest( context.getEnvironment().getActiveProfiles() );
    }

    protected boolean getBooleanValue( ConfigurationKey key )
    {
        return getConfiguration().isEnabled(key);
    }
}
