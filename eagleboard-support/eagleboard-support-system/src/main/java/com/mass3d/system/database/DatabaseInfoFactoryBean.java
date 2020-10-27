package com.mass3d.system.database;


import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component( "databaseInfo" )
public class DatabaseInfoFactoryBean
    implements FactoryBean<DatabaseInfo>
{
    private final DatabaseInfoProvider databaseInfoProvider;

    public DatabaseInfoFactoryBean( DatabaseInfoProvider databaseInfoProvider )
    {
        checkNotNull( databaseInfoProvider );

        this.databaseInfoProvider = databaseInfoProvider;
    }

    @Override
    public DatabaseInfo getObject() {
        return databaseInfoProvider.getDatabaseInfo();
    }

    @Override
    public Class<?> getObjectType()
    {
        return DatabaseInfo.class;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }
}
