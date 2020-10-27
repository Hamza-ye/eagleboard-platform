package com.mass3d.monitoring.metrics;

import static com.mass3d.external.conf.ConfigurationKey.MONITORING_CPU_ENABLED;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import com.mass3d.external.conf.ConfigurationKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional( ProcessorMetricsConfig.ProcessorMetricsConfigEnabledCondition.class )
public class ProcessorMetricsConfig
{
    @Bean
    public ProcessorMetrics processorMetrics()
    {
        return new ProcessorMetrics();
    }

    @Autowired
    public void bindToRegistry( MeterRegistry registry, ProcessorMetrics processorMetrics )
    {
        processorMetrics.bindTo( registry );
    }

    static class ProcessorMetricsConfigEnabledCondition
        extends
        MetricsEnabler
    {
        @Override
        protected ConfigurationKey getConfigKey()
        {
            return MONITORING_CPU_ENABLED;
        }
    }
}
