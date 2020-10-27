package com.mass3d.monitoring.metrics.jdbc;

import javax.sql.DataSource;

@FunctionalInterface
public interface DataSourcePoolMetadataProvider
{
    /**
     * Return the {@link DataSourcePoolMetadata} instance able to manage the specified
     * {@link DataSource} or {@code null} if the given data source could not be handled.
     *
     * @param dataSource the data source.
     * @return the data source pool metadata.
     */
    DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource dataSource);

}