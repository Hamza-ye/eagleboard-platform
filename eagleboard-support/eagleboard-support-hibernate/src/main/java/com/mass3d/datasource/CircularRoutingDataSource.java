package com.mass3d.datasource;

import com.google.common.collect.Iterators;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.AbstractDataSource;

/**
 * Data source implementation which routes to the configured target data sources
 * in a circular fashion.
 * 
 */
public class CircularRoutingDataSource
    extends AbstractDataSource
{
    private Iterator<DataSource> dataSourceIterator;

    public CircularRoutingDataSource()
    {
    }
    
    public CircularRoutingDataSource( List<DataSource> targetDataSources )
    {
        this.dataSourceIterator = Iterators.cycle( Collections.synchronizedList( targetDataSources ) );
    }
        
    // -------------------------------------------------------------------------
    // AbstractDataSource implementation
    // -------------------------------------------------------------------------

    @Override
    public Connection getConnection()
        throws SQLException
    {
        return getDataSource().getConnection();
    }

    @Override
    public Connection getConnection( String username, String password )
        throws SQLException
    {
        return getDataSource().getConnection( username, password );
    }

    // -------------------------------------------------------------------------
    // Private methods
    // -------------------------------------------------------------------------

    private synchronized DataSource getDataSource()
    {
        return dataSourceIterator.next();
    }
}
