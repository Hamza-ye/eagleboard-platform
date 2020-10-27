package com.mass3d;

import java.lang.reflect.Method;
import com.mass3d.dbms.DbmsManager;
import org.junit.After;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = { IntegrationTestConfig.class } )
@Category( IntegrationTest.class )
@ActiveProfiles( profiles = {"test-postgres"} )
@Transactional
public abstract class TransactionalIntegrationTestBase
    extends DhisConvenienceTest
    implements ApplicationContextAware
{
    @Autowired
    protected DbmsManager dbmsManager;

    protected ApplicationContext webApplicationContext;

    @Before
    public void before()
        throws Exception
    {
        executeStartupRoutines();
        setUpTest();
    }

    @After
    public void after()
        throws Exception
    {
        tearDownTest();

        if ( emptyDatabaseAfterTest() )
        {
            dbmsManager.emptyDatabase();
        }
    }

    private void executeStartupRoutines()
        throws Exception
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
    public void setApplicationContext( ApplicationContext applicationContext )
        throws BeansException
    {
        this.webApplicationContext = applicationContext;
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
