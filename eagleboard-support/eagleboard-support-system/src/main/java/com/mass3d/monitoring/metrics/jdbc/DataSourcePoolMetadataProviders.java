package com.mass3d.monitoring.metrics.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;

/**
 * A {@link DataSourcePoolMetadataProvider} implementation that returns the first
 * {@link DataSourcePoolMetadata} that is found by one of its delegate.
 *
 * @since 1.2.0
 */
public class DataSourcePoolMetadataProviders implements DataSourcePoolMetadataProvider {

    private final List<DataSourcePoolMetadataProvider> providers;

    /**
     * Create a {@link DataSourcePoolMetadataProviders} instance with an initial
     * collection of delegates to use.
     * @param providers the data source pool metadata providers
     */
    public DataSourcePoolMetadataProviders(
            Collection<? extends DataSourcePoolMetadataProvider> providers) {
        this.providers = (providers == null
                ? Collections.emptyList()
                : new ArrayList<>(providers));
    }

    @Override
    public DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource dataSource) {
        for (DataSourcePoolMetadataProvider provider : this.providers) {
            DataSourcePoolMetadata metadata = provider
                    .getDataSourcePoolMetadata(dataSource);
            if (metadata != null) {
                return metadata;
            }
        }
        return null;
    }

}