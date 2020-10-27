package com.mass3d.condition;

import com.mass3d.external.conf.ConfigurationKey;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Condition that matches to true if redis.enabled property is set to true in
 * dhis.conf.
 *
 *
 */
public class RedisEnabledCondition extends PropertiesAwareConfigurationCondition
{
    @Override
    public boolean matches( ConditionContext context, AnnotatedTypeMetadata metadata )
    {
        if ( !isTestRun( context ) )
        {
            return getConfiguration().getProperty( ConfigurationKey.REDIS_ENABLED ).equalsIgnoreCase( "true" );
        }
        return false;
    }

    @Override
    public ConfigurationPhase getConfigurationPhase()
    {
        return ConfigurationPhase.REGISTER_BEAN;
    }
}
