package com.mass3d;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.mass3d.dbms.DbmsManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = { IntegrationTestConfig.class } )
@Category( IntegrationTest.class )
@ActiveProfiles( profiles = {"test-postgres"} )
public abstract class IntegrationTestBase
        extends DhisConvenienceTest
        implements ApplicationContextAware
{
    @Autowired
    protected DbmsManager dbmsManager;

    private static JdbcTemplate jdbcTemplate;

    /*
    "Special" setter to allow setting JdbcTemplate as static field
     */
    @Autowired
    public void setJdbcTemplate( JdbcTemplate jdbcTemplate )
    {
        IntegrationTestBase.jdbcTemplate = jdbcTemplate;
    }

    /*
        Flag that determines if the IntegrationTestData annotation has
        been running the database init script. We only want to run
        the init script once per unit test
     */
    public static boolean dataInit = false;

    protected ApplicationContext webApplicationContext;

    @Before
    public void before() throws Exception
    {
        bindSession();
        executeStartupRoutines();

        IntegrationTestData annotation = this.getClass().getAnnotation( IntegrationTestData.class );

        if ( annotation != null && !dataInit )
        {
            ScriptUtils.executeSqlScript( jdbcTemplate.getDataSource().getConnection(),
                new EncodedResource( new ClassPathResource( annotation.path() ), StandardCharsets.UTF_8 ) );
            // only executes once per Unit Test
            dataInit = true;
        }

        // method that can be overridden by subclasses
        setUpTest();
    }

    @AfterClass
    public static void afterClass() {

        if ( dataInit ) // only truncate tables if IntegrationTestData is used
        {
            // truncate all tables
            String truncateAll = "DO $$ DECLARE\n" +
                    "  r RECORD;\n" +
                    "BEGIN\n" +
                    "  FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP\n" +
                    "    EXECUTE 'TRUNCATE TABLE ' || quote_ident(r.tablename) || ' CASCADE';\n" +
                    "  END LOOP;\n" +
                    "END $$;";

            jdbcTemplate.execute( truncateAll );
        }
        // reset data init state
        dataInit = false;
    }

    @After
    public void after() throws Exception
    {
        tearDownTest();
        unbindSession();

        if ( emptyDatabaseAfterTest() )
        {
            dbmsManager.emptyDatabase();
        }
    }

    private void executeStartupRoutines() throws Exception
    {
        String id = "com.mass3d.system.startup.StartupRoutineExecutor";

        if ( webApplicationContext.containsBean( id ) )
        {
            Object object = webApplicationContext.getBean( id );
            Method method = object.getClass().getMethod( "executeForTesting", new Class[0] );
            method.invoke( object, new Object[0] );
        }
    }

    @Override
    public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException
    {
        this.webApplicationContext = applicationContext;
    }

    private void bindSession()
    {
        SessionFactory sessionFactory = (SessionFactory) webApplicationContext.getBean( "sessionFactory" );
        Session session = sessionFactory.openSession();
        session.setHibernateFlushMode(FlushMode.ALWAYS);
        TransactionSynchronizationManager.bindResource( sessionFactory, new SessionHolder( session ) );
    }

    private void unbindSession()
    {
        SessionFactory sessionFactory = (SessionFactory) webApplicationContext.getBean( "sessionFactory" );

        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager
            .unbindResource( sessionFactory );

        SessionFactoryUtils.closeSession( sessionHolder.getSession() );
    }

    public abstract boolean emptyDatabaseAfterTest();

    /**
     * Method to override.
     */
    protected void setUpTest()
        throws Exception
    {
    }

    /**
     * Method to override.
     */
    protected void tearDownTest()
        throws Exception
    {
    }
}
