package com.mass3d.hibernate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.FactoryBean;

public class ConnectionPropertyFactoryBean
    implements FactoryBean<String>
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private HibernateConfigurationProvider hibernateConfigurationProvider;
    
    public void setHibernateConfigurationProvider( HibernateConfigurationProvider hibernateConfigurationProvider )
    {
        this.hibernateConfigurationProvider = hibernateConfigurationProvider;
    }

    private String hibernateProperty;

    public void setHibernateProperty( String hibernateProperty )
    {
        this.hibernateProperty = hibernateProperty;
    }
    
    private String defaultValue;

    public void setDefaultValue( String defaultValue )
    {
        this.defaultValue = defaultValue;
    }

    // -------------------------------------------------------------------------
    // FactoryBean implementation
    // -------------------------------------------------------------------------

    @Override
    public String getObject()
    {
        String value = hibernateConfigurationProvider.getConfiguration().getProperty( hibernateProperty );
        
        return StringUtils.defaultIfEmpty( value, defaultValue );
    }

    @Override
    public Class<String> getObjectType()
    {
        return String.class;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }
}
