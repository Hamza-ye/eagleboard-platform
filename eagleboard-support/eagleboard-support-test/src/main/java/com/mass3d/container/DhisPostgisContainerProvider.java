package com.mass3d.container;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgisContainerProvider;

/**
 * Custom PostgisContainerProvider to create
 * {@link DhisPostgreSQLContainer}
 *
 *
 */
@SuppressWarnings( "rawtypes" )
public class DhisPostgisContainerProvider
    extends PostgisContainerProvider
{
    private static final String DEFAULT_TAG = "10";
    private static final String DEFAULT_IMAGE = "mdillon/postgis";

    @Override
    public JdbcDatabaseContainer newInstance()
    {
        return newInstance( DEFAULT_TAG );
    }

    @Override
    public JdbcDatabaseContainer newInstance( String tag )
    {
        return new DhisPostgreSQLContainer( DEFAULT_IMAGE + ":" + tag );
    }

}
