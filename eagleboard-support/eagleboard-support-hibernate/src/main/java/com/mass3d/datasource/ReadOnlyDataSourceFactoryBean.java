package com.mass3d.datasource;

import javax.sql.DataSource;
import org.springframework.beans.factory.FactoryBean;

/**
 * Factory bean which provides a {@link CircularRoutingDataSource} containing a
 * list of data sources connecting to read replica database instances.
 * 
 */
public class ReadOnlyDataSourceFactoryBean
    implements FactoryBean<DataSource>
{
    private DataSourceManager dataSourceManager;

    public void setDataSourceManager( DataSourceManager dataSourceManager )
    {
        this.dataSourceManager = dataSourceManager;
    }

    // -------------------------------------------------------------------------
    // FactoryBean implementation
    // -------------------------------------------------------------------------

    @Override
    public DataSource getObject()
        throws Exception
    {
        return dataSourceManager.getReadOnlyDataSource();
    }

    @Override
    public Class<?> getObjectType()
    {
        return DataSource.class;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }
}
