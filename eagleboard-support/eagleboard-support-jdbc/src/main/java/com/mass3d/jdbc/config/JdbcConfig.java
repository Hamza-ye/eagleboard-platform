package com.mass3d.jdbc.config;

import com.google.common.collect.Lists;
import com.mass3d.hibernate.HibernateConfigurationProvider;
import com.mass3d.jdbc.dialect.StatementDialectFactoryBean;
import com.mass3d.jdbc.statementbuilder.StatementBuilderFactoryBean;
import org.hisp.quick.StatementInterceptor;
import org.hisp.quick.configuration.JdbcConfigurationFactoryBean;
import org.hisp.quick.factory.DefaultBatchHandlerFactory;
import org.hisp.quick.statement.JdbcStatementManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbcConfig
{
    @Autowired
    private HibernateConfigurationProvider hibernateConfigurationProvider;

    @Bean
    public JdbcStatementManager statementManager()
        throws Exception
    {
        JdbcStatementManager jdbcStatementManager = new JdbcStatementManager();
        jdbcStatementManager.setJdbcConfiguration( jdbcConfiguration().getObject() );
        return jdbcStatementManager;
    }

    @Bean( initMethod = "init" )
    public StatementDialectFactoryBean statementDialect()
    {
        return new StatementDialectFactoryBean( this.hibernateConfigurationProvider );
    }

    @Bean( initMethod = "init" )
    public JdbcConfigurationFactoryBean jdbcConfiguration()
    {
        JdbcConfigurationFactoryBean jdbcConf = new JdbcConfigurationFactoryBean();
        jdbcConf.setDialect( statementDialect().getObject() );
        jdbcConf.setDriverClass( (String) getConnectionProperty( "hibernate.connection.driver_class" ) );
        jdbcConf.setConnectionUrl( (String) getConnectionProperty( "hibernate.connection.url" ) );
        jdbcConf.setUsername( (String) getConnectionProperty( "hibernate.connection.username" ) );
        jdbcConf.setPassword( (String) getConnectionProperty( "hibernate.connection.password" ) );
        return jdbcConf;
    }

    @Bean( initMethod = "init" )
    public StatementBuilderFactoryBean statementBuilder()
    {
        return new StatementBuilderFactoryBean( statementDialect().getObject() );
    }

    @Bean
    public DefaultBatchHandlerFactory batchHandlerFactory()
        throws Exception
    {
        DefaultBatchHandlerFactory defaultBatchHandlerFactory = new DefaultBatchHandlerFactory();
        defaultBatchHandlerFactory.setJdbcConfiguration( jdbcConfiguration().getObject() );
        return defaultBatchHandlerFactory;
    }

    @Bean
    public StatementInterceptor statementInterceptor()
        throws Exception
    {
        StatementInterceptor statementInterceptor = new StatementInterceptor();
        statementInterceptor.setStatementManagers( Lists.newArrayList( statementManager() ) );
        return statementInterceptor;
    }

    private Object getConnectionProperty( String key )
    {
        return hibernateConfigurationProvider.getConfiguration().getProperty( key );
    }

}
