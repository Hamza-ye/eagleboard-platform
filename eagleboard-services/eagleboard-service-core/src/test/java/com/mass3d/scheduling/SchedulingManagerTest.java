package com.mass3d.scheduling;

import static org.junit.Assert.assertEquals;

import com.mass3d.DhisSpringTest;
import com.mass3d.scheduling.parameters.MockJobParameters;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SchedulingManagerTest
    extends DhisSpringTest
{
    private String CRON_EVERY_MIN = "0 * * ? * *";

    private String CRON_EVERY_SEC = "* * * ? * *";

    private JobConfiguration jobA;

    private JobConfiguration jobB;

    @Autowired
    private SchedulingManager schedulingManager;

    @Autowired
    private JobConfigurationService jobConfigurationService;

    private void verifyScheduledJobs( int expectedFutureJobs )
    {
        assertEquals( expectedFutureJobs, schedulingManager.getAllFutureJobs().size() );
    }

    private void createAndSchedule()
    {
        MockJobParameters jobConfigurationParametersA = new MockJobParameters();
        jobConfigurationParametersA.setMessage( "parameters A" );

        jobA = new JobConfiguration( "jobA", JobType.MOCK, CRON_EVERY_MIN, jobConfigurationParametersA );

        MockJobParameters jobConfigurationParametersB = new MockJobParameters();
        jobConfigurationParametersB.setMessage( "parameters B" );

        jobB = new JobConfiguration( "jobB", JobType.MOCK, CRON_EVERY_SEC, jobConfigurationParametersB );

        jobConfigurationService.addJobConfiguration( jobA );
        jobConfigurationService.addJobConfiguration( jobB );

        schedulingManager.scheduleJob( jobA );
        schedulingManager.scheduleJob( jobB );
    }

    /**
     * No assertions in this test. Tester has to verify by looking at the output in the terminal. JobA should fire at the first minute and every minute after that.
     * jobB should fire every second. (Unless sleep in actual job - or the job uses longer time than the expected delay to next execution time)
     */
    @Test
    @Ignore
    public void testScheduleJobs()
    {
        createAndSchedule();

        try
        {
            Thread.sleep( 1000000 );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void testScheduleJobsWithUpdate()
    {
        createAndSchedule();

        verifyScheduledJobs( 2 );

        // Wait 10 seconds. jobB should fire
        try
        {
            Thread.sleep( 10000 );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }

        schedulingManager.stopJob( jobB );

        verifyScheduledJobs( 1 );

        // Wait 1 minute. Job b should stop and jobA should fire
        try
        {
            Thread.sleep( 60000 );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void testExecuteJobs()
    {
        createAndSchedule();

        verifyScheduledJobs( 2 );

        // Wait 5 seconds and fire off jobC
        try
        {
            Thread.sleep( 5000 );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }

        JobConfiguration jobC = new JobConfiguration( "jobC", JobType.MOCK, "", new MockJobParameters() );
        schedulingManager.executeJob( jobC );

        verifyScheduledJobs( 2 );

        try
        {
            Thread.sleep( 5000 );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
    }
}
