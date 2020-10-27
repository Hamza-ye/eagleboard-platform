package com.mass3d.hibernate;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.springframework.core.io.Resource;

public interface HibernateConfigurationProvider
{
    String ID = HibernateConfigurationProvider.class.getName();

    Configuration getConfiguration()
        throws HibernateException;

    List<Resource> getJarResources();
    
    List<Resource> getDirectoryResources();
    
    List<String> getClusterHostnames();
}
