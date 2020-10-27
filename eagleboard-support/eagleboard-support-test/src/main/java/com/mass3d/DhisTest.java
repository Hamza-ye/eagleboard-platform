package com.mass3d;

import java.lang.reflect.Method;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.mass3d.dbms.DbmsManager;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * TODO remove this class and its usage, too slow.
 *
 */
@RunWith( SpringRunner.class )
@ActiveProfiles( "test-h2" )
@ContextConfiguration( classes = { UnitTestConfiguration.class } )
public abstract class DhisTest
    extends DhisConvenienceTest implements ApplicationContextAware
{
    @Autowired
    protected DbmsManager dbmsManager;

    // -------------------------------------------------------------------------
    // ApplicationContextAware implementation
    // -------------------------------------------------------------------------

    private ApplicationContext context;

    @Override
    public void setApplicationContext( ApplicationContext context )
    {
        this.context = context;
    }

    @Before
    public final void before()
        throws Exception
    {
        bindSession();

        executeStartupRoutines();

        setUpTest();
    }

    @After
    public final void after()
        throws Exception
    {
        tearDownTest();

        unbindSession();

        if ( emptyDatabaseAfterTest() )
        {
            dbmsManager.emptyDatabase();
        }
    }

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

    /**
     * Method to override.
     */
    protected boolean emptyDatabaseAfterTest()
    {
        return false;
    }

    // -------------------------------------------------------------------------
    // Utility methods
    // -------------------------------------------------------------------------

    /**
     * Retrieves a bean from the application context.
     *
     * @param beanId the identifier of the bean.
     */
    protected final Object getBean( String beanId )
    {
        return context.getBean( beanId );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void executeStartupRoutines()
        throws Exception
    {
        String id = "com.mass3d.system.startup.StartupRoutineExecutor";

        if ( context.containsBean( id ) )
        {
            Object object = context.getBean( id );

            Method method = object.getClass().getMethod( "executeForTesting", new Class[0] );

            method.invoke( object, new Object[0] );
        }
    }

    /**
     * Binds a Hibernate Session to the current thread.
     */
    private void bindSession()
    {
        SessionFactory sessionFactory = (SessionFactory) getBean( "sessionFactory" );
        Session session = sessionFactory.openSession();

        TransactionSynchronizationManager.bindResource( sessionFactory, new SessionHolder( session ) );
    }

    /**
     * Unbinds and closes the bound Hibernate Session from the current thread.
     */
    private void unbindSession()
    {
        SessionFactory sessionFactory = (SessionFactory) getBean( "sessionFactory" );

        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource( sessionFactory );

        SessionFactoryUtils.closeSession( sessionHolder.getSession() );
    }
}
