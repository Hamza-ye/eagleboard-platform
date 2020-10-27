package com.mass3d.hibernate;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

public class HibernateMappingDirectoryLocationsFactoryBean 
    implements FactoryBean<Object[]>
{    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private HibernateConfigurationProvider hibernateConfigurationProvider;

    public void setHibernateConfigurationProvider( HibernateConfigurationProvider hibernateConfigurationProvider )
    {
        this.hibernateConfigurationProvider = hibernateConfigurationProvider;
    }

    // -------------------------------------------------------------------------
    // FactoryBean implementation
    // -------------------------------------------------------------------------

    @Override
    public Object[] getObject()
        throws Exception
    {
        return hibernateConfigurationProvider.getDirectoryResources().toArray();
    }

    @Override
    public Class<Resource> getObjectType()
    {
        return Resource.class;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }
}
