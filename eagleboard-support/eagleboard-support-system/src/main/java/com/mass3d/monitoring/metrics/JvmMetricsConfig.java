package com.mass3d.monitoring.metrics;

import static com.mass3d.external.conf.ConfigurationKey.MONITORING_JVM_ENABLED;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import com.mass3d.external.conf.ConfigurationKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional( JvmMetricsConfig.JvmMetricsEnabledCondition.class )
public class JvmMetricsConfig
{
    @Bean
    public JvmGcMetrics jvmGcMetrics()
    {
        return new JvmGcMetrics();
    }

    @Bean
    public JvmMemoryMetrics jvmMemoryMetrics()
    {
        return new JvmMemoryMetrics();
    }

    @Bean
    public JvmThreadMetrics jvmThreadMetrics()
    {
        return new JvmThreadMetrics();
    }

    @Bean
    public ClassLoaderMetrics classLoaderMetrics()
    {
        return new ClassLoaderMetrics();
    }

    @Autowired
    public void bindToRegistry( MeterRegistry registry, JvmGcMetrics jvmGcMetrics, JvmMemoryMetrics jvmMemoryMetrics,
        JvmThreadMetrics jvmThreadMetrics, ClassLoaderMetrics classLoaderMetrics )
    {
        jvmGcMetrics.bindTo( registry );
        jvmMemoryMetrics.bindTo( registry );
        jvmThreadMetrics.bindTo( registry );
        classLoaderMetrics.bindTo( registry );
    }

    static class JvmMetricsEnabledCondition
        extends
        MetricsEnabler
    {
        @Override
        protected ConfigurationKey getConfigKey()
        {
            return MONITORING_JVM_ENABLED;
        }
    }
}
