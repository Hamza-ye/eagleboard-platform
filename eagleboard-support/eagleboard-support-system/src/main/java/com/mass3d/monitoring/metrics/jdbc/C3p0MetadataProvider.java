package com.mass3d.monitoring.metrics.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class C3p0MetadataProvider
    extends
    AbstractDataSourcePoolMetadata<ComboPooledDataSource>
{
    /**
     * Create an instance with the data source to use.
     *
     * @param dataSource the data source
     */
    public C3p0MetadataProvider( ComboPooledDataSource dataSource )
    {
        super( dataSource );
    }

    @Override
    public Integer getActive()
    {
        try
        {
            return getDataSource().getNumBusyConnections();
        }
        catch ( SQLException e )
        {
            log.error( "An error occurred while fetching number of busy connection from the DataSource", e );
            return 0;
        }
    }

    @Override
    public Integer getMax()
    {
        return getDataSource().getMaxPoolSize();
    }

    @Override
    public Integer getMin()
    {
        return getDataSource().getMinPoolSize();
    }

    @Override
    public String getValidationQuery()
    {
        return "";
    }

    @Override
    public Boolean getDefaultAutoCommit()
    {
        return getDataSource().isAutoCommitOnClose();
    }

    @Override
    public Integer getIdle()
    {
        try
        {
            return getDataSource().getNumIdleConnections();
        }
        catch ( SQLException e )
        {
            log.error( "An error occurred while fetching number of idle connection from the DataSource", e );
            return 0;
        }
    }
}
