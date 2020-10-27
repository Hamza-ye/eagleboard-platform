package com.mass3d.hibernate;

import java.util.Properties;
import org.springframework.beans.factory.FactoryBean;

/**
 * @version $Id$
 */
public class HibernatePropertiesFactoryBean
    implements FactoryBean<Properties>
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
    public Properties getObject()
        throws Exception
    {
        return hibernateConfigurationProvider.getConfiguration().getProperties();
    }

    @Override
    public Class<Properties> getObjectType()
    {
        return Properties.class;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }
}
