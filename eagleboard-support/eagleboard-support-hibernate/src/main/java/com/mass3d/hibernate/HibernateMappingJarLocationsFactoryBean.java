package com.mass3d.hibernate;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

/**
 * @version $Id$
 */
public class HibernateMappingJarLocationsFactoryBean
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
        return hibernateConfigurationProvider.getJarResources().toArray();
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
