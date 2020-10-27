package com.mass3d.datasource;

import javax.sql.DataSource;

public interface DataSourceManager
{
    /**
     * Returns a data source which should be used for read only queries only. If
     * read only replicas have been explicitly defined in the configuration, the
     * data source implementation will be routing to potentially multiple 
     * underlying data sources. If not, the data source will point to the main 
     * data source.
     * 
     * @return a DataSource instance.
     */
    DataSource getReadOnlyDataSource();
    
    /**
     * Returns the number of explicitly defined read only database instances.
     * 
     * @return the number of explicitly defined read only database instances.
     */
    int getReadReplicaCount();
}
