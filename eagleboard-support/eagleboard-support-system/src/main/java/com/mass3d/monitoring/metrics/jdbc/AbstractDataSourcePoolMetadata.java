package com.mass3d.monitoring.metrics.jdbc;

import javax.sql.DataSource;

/**
 * A base {@link DataSourcePoolMetadata} implementation.
 *
 * @param <T> the data source type
 * @since 2.0.0
 */
public abstract class AbstractDataSourcePoolMetadata<T extends DataSource>
        implements DataSourcePoolMetadata {

    private final T dataSource;

    /**
     * Create an instance with the data source to use.
     * @param dataSource the data source
     */
    protected AbstractDataSourcePoolMetadata(T dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Float getUsage() {
        Integer maxSize = getMax();
        Integer currentSize = getActive();
        if (maxSize == null || currentSize == null) {
            return null;
        }
        if (maxSize < 0) {
            return -1F;
        }
        if (currentSize == 0) {
            return 0F;
        }
        return (float) currentSize / (float) maxSize;
    }

    protected final T getDataSource() {
        return this.dataSource;
    }

}