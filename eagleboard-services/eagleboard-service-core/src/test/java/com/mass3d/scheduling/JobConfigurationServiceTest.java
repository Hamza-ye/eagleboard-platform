package com.mass3d.scheduling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import com.mass3d.DhisSpringTest;
import com.mass3d.scheduling.parameters.MockJobParameters;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JobConfigurationServiceTest
    extends DhisSpringTest
{
    private static final String CRON_EVERY_MIN = "0 * * ? * *";

    @Autowired
    private JobConfigurationService jobConfigurationService;

    private JobConfiguration jobA;

    private JobConfiguration jobB;

    @Override
    protected void setUpTest()
        throws Exception
    {
        jobA = new JobConfiguration( "jobA", JobType.MOCK, CRON_EVERY_MIN, new MockJobParameters( "test" ) );
        jobB = new JobConfiguration( "jobB", JobType.DATA_INTEGRITY, CRON_EVERY_MIN, null );

        jobConfigurationService.addJobConfiguration( jobA );
        jobConfigurationService.addJobConfiguration( jobB );
    }

    @Test
    public void testGetJobTypeInfo()
    {
        List<JobTypeInfo> jobTypes = jobConfigurationService.getJobTypeInfo();

        assertNotNull( jobTypes );
        assertFalse( jobTypes.isEmpty() );

        JobTypeInfo jobType = jobTypes.stream()
            .filter( j -> j.getJobType() == JobType.CONTINUOUS_ANALYTICS_TABLE )
            .findFirst().get();

        assertNotNull( jobType );
        assertEquals( JobType.CONTINUOUS_ANALYTICS_TABLE.getSchedulingType(), jobType.getSchedulingType() );
        assertEquals( JobType.CONTINUOUS_ANALYTICS_TABLE.getKey(), jobType.getKey() );
    }

    @Test
    public void testGetJob()
    {
        List<JobConfiguration> jobConfigurationList = jobConfigurationService.getAllJobConfigurations();
        assertEquals( "The number of job configurations does not match", 2, jobConfigurationList.size() );

        assertEquals( JobType.MOCK, jobConfigurationService.getJobConfigurationByUid( jobA.getUid() ).getJobType() );
        MockJobParameters jobParameters = (MockJobParameters) jobConfigurationService
            .getJobConfigurationByUid( jobA.getUid() ).getJobParameters();

        assertNotNull( jobParameters );
        assertEquals( "test", jobParameters.getMessage() );

        assertEquals( JobType.DATA_INTEGRITY,
            jobConfigurationService.getJobConfigurationByUid( jobB.getUid() ).getJobType() );
        assertNull( jobConfigurationService.getJobConfigurationByUid( jobB.getUid() ).getJobParameters() );
    }

    @Test
    public void testUpdateJob()
    {
        JobConfiguration test = jobConfigurationService.getJobConfigurationByUid( jobA.getUid() );
        test.setName( "testUpdate" );
        jobConfigurationService.updateJobConfiguration( test );

        assertEquals( "testUpdate", jobConfigurationService.getJobConfigurationByUid( jobA.getUid() ).getName() );
    }

    @Test
    public void testDeleteJob()
    {
        jobConfigurationService.deleteJobConfiguration( jobA );

        assertNull( jobConfigurationService.getJobConfigurationByUid( jobA.getUid() ) );
    }
}
