package com.mass3d.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.util.List;
import javax.sql.DataSource;
import com.mass3d.cache.DefaultHibernateCacheManager;
import com.mass3d.datasource.DataSourceManager;
import com.mass3d.datasource.DefaultDataSourceManager;
import com.mass3d.dbms.DbmsManager;
import com.mass3d.dbms.HibernateDbmsManager;
import com.mass3d.deletedobject.DeletedObject;
import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.hibernate.DefaultHibernateConfigurationProvider;
import com.mass3d.hibernate.HibernateConfigurationProvider;
import com.mass3d.hibernate.HibernatePropertiesFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EnableTransactionManagement
public class HibernateConfig
{
    @Autowired
    private DhisConfigurationProvider dhisConfigurationProvider;

    @Bean
    public HibernateConfigurationProvider hibernateConfigurationProvider()
    {
        DefaultHibernateConfigurationProvider hibernateConfigurationProvider = new DefaultHibernateConfigurationProvider();
        hibernateConfigurationProvider.setConfigurationProvider( dhisConfigurationProvider );
        return hibernateConfigurationProvider;
    }

    private HibernatePropertiesFactoryBean hibernateProperties()
    {
        HibernatePropertiesFactoryBean hibernatePropertiesFactoryBean = new HibernatePropertiesFactoryBean();
        hibernatePropertiesFactoryBean.setHibernateConfigurationProvider( hibernateConfigurationProvider() );
        return hibernatePropertiesFactoryBean;
    }

    @Bean
//    @DependsOn("flyway")
    public LocalSessionFactoryBean sessionFactory()
        throws Exception
    {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource( dataSource() );

        sessionFactory.setHibernateProperties( hibernateProperties().getObject() );

        List<Resource> jarResources = hibernateConfigurationProvider().getJarResources();
        sessionFactory.setMappingJarLocations( jarResources.toArray( new Resource[jarResources.size()] ) );

        List<Resource> directoryResources = hibernateConfigurationProvider().getDirectoryResources();
        sessionFactory
            .setMappingDirectoryLocations( directoryResources.toArray( new Resource[directoryResources.size()] ) );

        sessionFactory.setAnnotatedClasses( DeletedObject.class );

        sessionFactory.setHibernateProperties( hibernateProperties().getObject() );

        return sessionFactory;
    }

    @Bean
    public DataSource dataSource()
        throws PropertyVetoException
    {
        // FIXME LUCIANO destroyMethod ? destroy-method="close"
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass( (String) getConnectionProperty( "hibernate.connection.driver_class" ) );
        dataSource.setJdbcUrl( (String) getConnectionProperty( "hibernate.connection.url" ) );
        dataSource.setUser( (String) getConnectionProperty( "hibernate.connection.username" ) );
        dataSource.setPassword( (String) getConnectionProperty( "hibernate.connection.password" ) );
        dataSource.setMinPoolSize( 5 );
        dataSource.setMaxPoolSize( Integer.parseInt( (String) getConnectionProperty( "hibernate.c3p0.max_size" ) ) );
        dataSource.setInitialPoolSize( 5 );
        dataSource.setAcquireIncrement( 5 );
        dataSource.setMaxIdleTime( 7200 );

        return dataSource;
    }

    @Bean
    public DataSourceManager dataSourceManager() throws PropertyVetoException
    {
        DefaultDataSourceManager defaultDataSourceManager = new DefaultDataSourceManager();
        defaultDataSourceManager.setConfig( dhisConfigurationProvider );
        defaultDataSourceManager.setMainDataSource( dataSource() );

        return defaultDataSourceManager;
    }

    @Bean
    public DataSource readOnlyDataSource() throws PropertyVetoException
    {
        // FIXME Luciano why do we need this? Can't we use @Transactional readonly?

        return dataSourceManager().getReadOnlyDataSource();
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager()
        throws Exception
    {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory( sessionFactory().getObject() );
        transactionManager.setDataSource( dataSource() );
        return transactionManager;
    }

    @Bean
    public TransactionTemplate transactionTemplate()
        throws Exception
    {
        return new TransactionTemplate( hibernateTransactionManager() );
    }

    @Bean
    public DefaultHibernateCacheManager cacheManager()
        throws Exception
    {
        DefaultHibernateCacheManager cacheManager = new DefaultHibernateCacheManager();
        cacheManager.setSessionFactory(sessionFactory().getObject());
        return cacheManager;
    }

    @Bean
    public DbmsManager dbmsManager()
        throws Exception
    {
        HibernateDbmsManager hibernateDbmsManager = new HibernateDbmsManager();
        hibernateDbmsManager.setCacheManager( cacheManager() );
        hibernateDbmsManager.setSessionFactory(sessionFactory().getObject());
        hibernateDbmsManager.setJdbcTemplate( jdbcTemplate() );
        return hibernateDbmsManager;
    }

    @Bean( "jdbcTemplate" )
    @Primary
    public JdbcTemplate jdbcTemplate()
        throws PropertyVetoException
    {
        JdbcTemplate jdbcTemplate = new JdbcTemplate( dataSource() );
        jdbcTemplate.setFetchSize( 1000 );
        return jdbcTemplate;
    }

    @Bean( "readOnlyJdbcTemplate" )
    public JdbcTemplate readOnlyJdbcTemplate()
        throws PropertyVetoException
    {
        JdbcTemplate jdbcTemplate = new JdbcTemplate( readOnlyDataSource() );
        jdbcTemplate.setFetchSize( 1000 );
        return jdbcTemplate;
    }

    private Object getConnectionProperty( String key )
    {
        return hibernateConfigurationProvider().getConfiguration().getProperty( key );
    }
}
