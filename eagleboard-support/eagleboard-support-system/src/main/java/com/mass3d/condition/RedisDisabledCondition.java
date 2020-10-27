package com.mass3d.condition;

import com.mass3d.external.conf.ConfigurationKey;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Condition that matches to true if redis.enabled property is absent in
 * dhis.conf or if it is explicitly set to anything other than true
 *
 *
 */
public class RedisDisabledCondition extends PropertiesAwareConfigurationCondition
{
    @Override
    public boolean matches( ConditionContext context, AnnotatedTypeMetadata metadata )
    {
        if ( !isTestRun( context ) )
        {
            return !getConfiguration().getProperty( ConfigurationKey.REDIS_ENABLED ).equalsIgnoreCase( "true" );
        }

        return true;
    }

    @Override
    public ConfigurationPhase getConfigurationPhase()
    {
        return ConfigurationPhase.REGISTER_BEAN;
    }
}
