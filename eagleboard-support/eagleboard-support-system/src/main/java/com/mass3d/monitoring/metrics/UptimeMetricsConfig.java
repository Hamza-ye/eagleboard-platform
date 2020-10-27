package com.mass3d.monitoring.metrics;

import static com.mass3d.external.conf.ConfigurationKey.MONITORING_UPTIME_ENABLED;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import com.mass3d.external.conf.ConfigurationKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional( UptimeMetricsConfig.UptimeMetricsConfigEnabledCondition.class )
public class UptimeMetricsConfig
{
    @Bean
    public UptimeMetrics uptimeMetrics()
    {
        return new UptimeMetrics();
    }

    @Autowired
    public void bindToRegistry( MeterRegistry registry, UptimeMetrics uptimeMetrics )
    {
        uptimeMetrics.bindTo( registry );
    }

    static class UptimeMetricsConfigEnabledCondition
        extends
        MetricsEnabler
    {
        @Override
        protected ConfigurationKey getConfigKey()
        {
            return MONITORING_UPTIME_ENABLED;
        }
    }
}
