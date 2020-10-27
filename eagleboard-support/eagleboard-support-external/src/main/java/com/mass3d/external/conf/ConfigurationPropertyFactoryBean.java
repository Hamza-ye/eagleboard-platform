package com.mass3d.external.conf;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Factory bean which allows for DHIS configuration property values to be 
 * injected into target beans. 
 * 
 * @param key must reflect a {@link ConfigurationKey}.
 * 
 * @author Lars Helge Overland
 */
public class ConfigurationPropertyFactoryBean
    implements FactoryBean<Object>
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    @Autowired
    private DhisConfigurationProvider configurationProvider;
    
    private ConfigurationKey key;
    
    public ConfigurationPropertyFactoryBean( ConfigurationKey key )
    {
        this.key = key;
    }
    
    // -------------------------------------------------------------------------
    // FactoryBean implementation
    // -------------------------------------------------------------------------

    @Override
    public Object getObject()
    {
        Assert.notNull( key, "Configuration key must be specified" );
        
        return configurationProvider.getProperty( key );
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
