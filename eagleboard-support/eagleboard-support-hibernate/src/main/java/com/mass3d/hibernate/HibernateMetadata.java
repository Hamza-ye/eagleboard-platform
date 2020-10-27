package com.mass3d.hibernate;

import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.boot.spi.SessionFactoryBuilderFactory;
import org.hibernate.boot.spi.SessionFactoryBuilderImplementor;

public class HibernateMetadata implements SessionFactoryBuilderFactory
{
    private static final ThreadLocal<MetadataImplementor> metadataImplementor = new ThreadLocal<>();

    @Override
    public SessionFactoryBuilder getSessionFactoryBuilder( MetadataImplementor metadataImplementor, SessionFactoryBuilderImplementor defaultBuilder )
    {
        HibernateMetadata.metadataImplementor.set( metadataImplementor );
        return defaultBuilder;
    }

    public static MetadataImplementor getMetadataImplementor()
    {
        return metadataImplementor.get();
    }
}
