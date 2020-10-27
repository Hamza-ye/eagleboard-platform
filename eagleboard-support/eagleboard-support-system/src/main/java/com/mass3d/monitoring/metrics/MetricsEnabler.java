package com.mass3d.monitoring.metrics;

import lombok.extern.slf4j.Slf4j;
import com.mass3d.condition.PropertiesAwareConfigurationCondition;
import com.mass3d.external.conf.ConfigurationKey;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Slf4j
public abstract class MetricsEnabler
    extends PropertiesAwareConfigurationCondition
{
    @Override
    public ConfigurationPhase getConfigurationPhase()
    {
        return ConfigurationPhase.REGISTER_BEAN;
    }

    @Override
    public boolean matches( ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata )
    {
        ConfigurationKey key = getConfigKey();

        boolean isEnabled = !isTestRun( conditionContext ) && getBooleanValue( getConfigKey() );
        log.info(
            String
                .format( "Monitoring metric for key %s is %s", key.getKey(), isEnabled ? "enabled" : "disabled" ) );
        return isEnabled;
    }

    protected abstract ConfigurationKey getConfigKey();
}
