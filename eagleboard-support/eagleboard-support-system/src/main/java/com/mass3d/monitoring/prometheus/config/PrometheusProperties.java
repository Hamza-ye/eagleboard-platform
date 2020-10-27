package com.mass3d.monitoring.prometheus.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration Properties for configuring metrics export to Prometheus.
 *
 */
public class PrometheusProperties
{

    /**
     * Whether to enable publishing descriptions as part of the scrape payload to
     * Prometheus. Turn this off to minimize the amount of data sent on each scrape.
     */
    private boolean descriptions = true;

    /**
     * Configuration options for using Prometheus Pushgateway, allowing metrics to
     * be pushed when they cannot be scraped.
     */
    private Pushgateway pushgateway = new Pushgateway();

    /**
     * Step size (i.e. reporting frequency) to use.
     */
    private Duration step = Duration.ofMinutes( 1 );

    public boolean isDescriptions()
    {
        return this.descriptions;
    }

    public void setDescriptions( boolean descriptions )
    {
        this.descriptions = descriptions;
    }

    public Duration getStep()
    {
        return this.step;
    }

    public void setStep( Duration step )
    {
        this.step = step;
    }

    public Pushgateway getPushgateway()
    {
        return this.pushgateway;
    }

    public void setPushgateway( Pushgateway pushgateway )
    {
        this.pushgateway = pushgateway;
    }

    /**
     * Configuration options for push-based interaction with Prometheus.
     */
    public static class Pushgateway
    {

        /**
         * Enable publishing via a Prometheus Pushgateway.
         */
        private Boolean enabled = false;

        /**
         * Base URL for the Pushgateway.
         */
        private String baseUrl = "http://localhost:9091";

        /**
         * Frequency with which to push metrics.
         */
        private Duration pushRate = Duration.ofMinutes( 1 );

        /**
         * Job identifier for this application instance.
         */
        private String job;

        /**
         * Grouping key for the pushed metrics.
         */
        private Map<String, String> groupingKey = new HashMap<>();

        public Boolean getEnabled()
        {
            return this.enabled;
        }

        public void setEnabled( Boolean enabled )
        {
            this.enabled = enabled;
        }

        public String getBaseUrl()
        {
            return this.baseUrl;
        }

        public void setBaseUrl( String baseUrl )
        {
            this.baseUrl = baseUrl;
        }

        public Duration getPushRate()
        {
            return this.pushRate;
        }

        public void setPushRate( Duration pushRate )
        {
            this.pushRate = pushRate;
        }

        public String getJob()
        {
            return this.job;
        }

        public void setJob( String job )
        {
            this.job = job;
        }

        public Map<String, String> getGroupingKey()
        {
            return this.groupingKey;
        }

        public void setGroupingKey( Map<String, String> groupingKey )
        {
            this.groupingKey = groupingKey;
        }

    }

}