package com.mass3d.monitoring.prometheus.config;

import io.micrometer.core.instrument.Clock;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrometheusMonitoringConfig
{
    @Bean
    public Clock micrometerClock()
    {
        return Clock.SYSTEM;
    }

    @Bean
    public PrometheusProperties defaultProperties()
    {
        return new PrometheusProperties();
    }

    @Bean
    public PrometheusConfig prometheusConfig( PrometheusProperties prometheusProperties )
    {
        return new PrometheusPropertiesConfigAdapter( prometheusProperties );
    }

    @Bean
    public PrometheusMeterRegistry prometheusMeterRegistry( PrometheusConfig prometheusConfig,
        CollectorRegistry collectorRegistry, Clock clock )
    {
        return new PrometheusMeterRegistry( prometheusConfig, collectorRegistry, clock );
    }

    @Bean
    public CollectorRegistry collectorRegistry()
    {
        return new CollectorRegistry( true );
    }
}
