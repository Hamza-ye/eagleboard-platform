package com.mass3d.monitoring.prometheus.config;

import io.micrometer.prometheus.PrometheusConfig;
import java.time.Duration;

public class PrometheusPropertiesConfigAdapter
    extends PropertiesConfigAdapter<PrometheusProperties>
    implements PrometheusConfig
{
    /**
     * Create a new {@link PropertiesConfigAdapter} instance.
     *
     * @param properties the source properties
     */
    public PrometheusPropertiesConfigAdapter( PrometheusProperties properties )
    {
        super( properties );
    }

    @Override
    public String get( String key )
    {
        return null;
    }

    @Override
    public boolean descriptions()
    {
        return get( PrometheusProperties::isDescriptions, PrometheusConfig.super::descriptions );
    }

    @Override
    public Duration step()
    {
        return get( PrometheusProperties::getStep, PrometheusConfig.super::step );
    }
}
