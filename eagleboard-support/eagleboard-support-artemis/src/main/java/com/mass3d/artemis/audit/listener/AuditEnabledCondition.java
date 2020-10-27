package com.mass3d.artemis.audit.listener;

import com.mass3d.condition.PropertiesAwareConfigurationCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Condition that "switches off" the Auditing producer from Hibernate Listener
 * when the system is running in a Test run
 *
 */
public class AuditEnabledCondition
    extends PropertiesAwareConfigurationCondition
{
    @Override
    public boolean matches( ConditionContext context, AnnotatedTypeMetadata metadata )
    {
        return !isTestRun( context ) || isAuditTest( context );
    }

    @Override
    public ConfigurationPhase getConfigurationPhase()
    {
        return ConfigurationPhase.REGISTER_BEAN;
    }
}
