package com.mass3d.monitoring.metrics;

import static com.mass3d.external.conf.ConfigurationKey.MONITORING_DBPOOL_ENABLED;

import com.google.common.collect.Lists;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.sql.DataSource;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.monitoring.metrics.jdbc.C3p0MetadataProvider;
import com.mass3d.monitoring.metrics.jdbc.DataSourcePoolMetadataProvider;
import com.mass3d.monitoring.metrics.jdbc.DataSourcePoolMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class DataSourcePoolMetricsConfig
{
    @Configuration
    @Conditional( DataSourcePoolMetricsEnabledCondition.class )
    static class DataSourcePoolMetadataMetricsConfiguration
    {

        private static final String DATASOURCE_SUFFIX = "dataSource";

        private final MeterRegistry registry;

        private final Collection<DataSourcePoolMetadataProvider> metadataProviders;

        DataSourcePoolMetadataMetricsConfiguration( MeterRegistry registry,
            Collection<DataSourcePoolMetadataProvider> metadataProviders )
        {
            this.registry = registry;
            this.metadataProviders = metadataProviders;
        }

        @Autowired
        public void bindDataSourcesToRegistry( Map<String, DataSource> dataSources )
        {
            dataSources.forEach( this::bindDataSourceToRegistry );
        }

        private void bindDataSourceToRegistry( String beanName, DataSource dataSource )
        {
            String dataSourceName = getDataSourceName( beanName );
            new DataSourcePoolMetrics( dataSource, this.metadataProviders, dataSourceName, Collections
                .emptyList() )
                .bindTo( this.registry );
        }

        /**
         * Get the name of a DataSource based on its {@code beanName}.
         *
         * @param beanName the name of the data source bean
         * @return a name for the given data source
         */
        private String getDataSourceName( String beanName )
        {
            if ( beanName.length() > DATASOURCE_SUFFIX.length()
                && StringUtils.endsWithIgnoreCase( beanName, DATASOURCE_SUFFIX ) )
            {
                return beanName.substring( 0, beanName.length() - DATASOURCE_SUFFIX.length() );
            }
            return beanName;
        }
    }

    @Bean
    public Collection<DataSourcePoolMetadataProvider> dataSourceMetadataProvider()
    {
        DataSourcePoolMetadataProvider provider = dataSource -> new C3p0MetadataProvider(
            (ComboPooledDataSource) dataSource );

        return Lists.newArrayList( provider );
    }

    static class DataSourcePoolMetricsEnabledCondition
        extends MetricsEnabler
    {
        @Override
        protected ConfigurationKey getConfigKey()
        {
            return MONITORING_DBPOOL_ENABLED;
        }
    }
}
