package com.mass3d.jdbc.dialect;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;
import org.hibernate.cfg.Configuration;
import com.mass3d.hibernate.HibernateConfigurationProvider;
import org.hisp.quick.StatementDialect;
import org.springframework.beans.factory.FactoryBean;

public class StatementDialectFactoryBean
    implements FactoryBean<StatementDialect>
{
    private static final String KEY_DIALECT = "hibernate.dialect";
    
    private static Map<String, StatementDialect> dialectMap;
    
    static
    {
        dialectMap = new HashMap<>();
        dialectMap.put( "org.hibernate.dialect.MySQLDialect", StatementDialect.MYSQL );
        dialectMap.put( "org.hibernate.dialect.PostgreSQLDialect", StatementDialect.POSTGRESQL );
        dialectMap.put( "com.mass3d.hibernate.dialect.DhisPostgresDialect", StatementDialect.POSTGRESQL );
        dialectMap.put( "org.hibernate.dialect.HSQLDialect", StatementDialect.HSQL );
        dialectMap.put( "org.hibernate.dialect.H2Dialect", StatementDialect.H2 );
        dialectMap.put( "com.mass3d.hibernate.dialect.DhisH2Dialect", StatementDialect.H2 );
    }
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private final HibernateConfigurationProvider hibernateConfigurationProvider;

    public StatementDialectFactoryBean( HibernateConfigurationProvider hibernateConfigurationProvider )
    {
        checkNotNull( hibernateConfigurationProvider );
        this.hibernateConfigurationProvider = hibernateConfigurationProvider;
    }

    private StatementDialect statementDialect;
    
    // -------------------------------------------------------------------------
    // Initialisation
    // -------------------------------------------------------------------------
    
    public void init()
    {
        Configuration hibernateConfiguration = hibernateConfigurationProvider.getConfiguration();
        
        String dialect = hibernateConfiguration.getProperty( KEY_DIALECT );

        statementDialect = dialectMap.get( dialect );
        
        if ( statementDialect == null )
        {
            throw new RuntimeException( "Unsupported dialect: " + dialect );
        }
    }

    // -------------------------------------------------------------------------
    // FactoryBean implementation
    // -------------------------------------------------------------------------
        
    @Override
    public StatementDialect getObject() {
        return statementDialect;
    }

    @Override
    public Class<StatementDialect> getObjectType()
    {
        return StatementDialect.class;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }
}
