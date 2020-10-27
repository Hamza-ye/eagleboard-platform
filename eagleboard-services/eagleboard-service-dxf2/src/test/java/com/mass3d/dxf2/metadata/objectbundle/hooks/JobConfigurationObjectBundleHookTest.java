package com.mass3d.dxf2.metadata.objectbundle.hooks;

import java.util.List;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.scheduling.Job;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.scheduling.JobConfigurationService;
import com.mass3d.scheduling.JobType;
import com.mass3d.scheduling.SchedulingManager;
import com.mass3d.scheduling.parameters.ContinuousAnalyticsJobParameters;
import com.mass3d.scheduling.parameters.DataSynchronizationJobParameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Unit tests for {@link JobConfigurationObjectBundleHook}.
 *
 */
public class JobConfigurationObjectBundleHookTest
{
    private static final String CRON_HOURLY = "0 0 * ? * *";

    @Mock
    private JobConfigurationService jobConfigurationService;

    @Mock
    private SchedulingManager schedulingManager;

    @Mock
    private Job job;

    @InjectMocks
    private JobConfigurationObjectBundleHook hook;

    private JobConfiguration analyticsTableJobConfig;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp()
    {
        analyticsTableJobConfig = new JobConfiguration();
        analyticsTableJobConfig.setJobType( JobType.ANALYTICSTABLE_UPDATE );
        analyticsTableJobConfig.setEnabled( true );
    }

    @Test
    public void validateInternalNonConfigurableChangeError()
    {
        Mockito.when( jobConfigurationService.getJobConfigurationByUid( Mockito.eq( "jsdhJSJHD" ) ) )
            .thenReturn( analyticsTableJobConfig );
        Mockito.when( schedulingManager.getJob( Mockito.eq( JobType.ANALYTICSTABLE_UPDATE ) ) )
            .thenReturn( job );

        JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setUid( "jsdhJSJHD" );
        jobConfiguration.setJobType( JobType.ANALYTICSTABLE_UPDATE );
        jobConfiguration.setCronExpression( CRON_HOURLY );
        jobConfiguration.setEnabled( false );

        List<ErrorReport> errorReports = hook.validateInternal( jobConfiguration );
        Assert.assertEquals( 1, errorReports.size() );
        Assert.assertEquals( ErrorCode.E7003, errorReports.get( 0 ).getErrorCode() );
    }

    @Test
    public void validateInternalNonConfigurableChange()
    {
        Mockito.when( jobConfigurationService.getJobConfigurationByUid( Mockito.eq( "jsdhJSJHD" ) ) )
            .thenReturn( analyticsTableJobConfig );
        Mockito.when( schedulingManager.getJob( Mockito.eq( JobType.ANALYTICSTABLE_UPDATE ) ) )
            .thenReturn( job );

        JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setUid( "jsdhJSJHD" );
        jobConfiguration.setJobType( JobType.ANALYTICSTABLE_UPDATE );
        jobConfiguration.setCronExpression( CRON_HOURLY );
        jobConfiguration.setEnabled( true );

        List<ErrorReport> errorReports = hook.validateInternal( jobConfiguration );
        Assert.assertEquals( 0, errorReports.size() );
    }

    @Test
    public void validateInternalNonConfigurableShownValidationErrorNonE7010()
    {
        Mockito.when( jobConfigurationService.getJobConfigurationByUid( Mockito.eq( "jsdhJSJHD" ) ) )
            .thenReturn( analyticsTableJobConfig );
        Mockito.when( schedulingManager.getJob( Mockito.eq( JobType.ANALYTICSTABLE_UPDATE ) ) )
            .thenReturn( job );
        Mockito.when( job.validate() ).thenReturn( new ErrorReport( Class.class, ErrorCode.E7000 ) );

        JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setUid( "jsdhJSJHD" );
        jobConfiguration.setJobType( JobType.ANALYTICSTABLE_UPDATE );
        jobConfiguration.setCronExpression( CRON_HOURLY );
        jobConfiguration.setEnabled( true );

        List<ErrorReport> errorReports = hook.validateInternal( jobConfiguration );
        Assert.assertEquals( 1, errorReports.size() );
        Assert.assertEquals( ErrorCode.E7000, errorReports.get( 0 ).getErrorCode() );
    }

    @Test
    public void validateInternalNonConfigurableShownValidationErrorE7010Configurable()
    {
        Mockito.when( jobConfigurationService.getJobConfigurationByUid( Mockito.eq( "jsdhJSJHD" ) ) )
            .thenReturn( analyticsTableJobConfig );
        Mockito.when( schedulingManager.getJob( Mockito.eq( JobType.DATA_SYNC ) ) )
            .thenReturn( job );
        Mockito.when( job.validate() ).thenReturn( new ErrorReport( Class.class, ErrorCode.E7010 ) );

        analyticsTableJobConfig.setJobType( JobType.DATA_SYNC );
        JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setUid( "jsdhJSJHD" );
        jobConfiguration.setJobType( JobType.DATA_SYNC );
        jobConfiguration.setCronExpression( CRON_HOURLY );
        jobConfiguration.setEnabled( true );

        DataSynchronizationJobParameters jobParameters = new DataSynchronizationJobParameters();
        jobParameters.setPageSize( 200 );
        jobConfiguration.setJobParameters( jobParameters );

        List<ErrorReport> errorReports = hook.validateInternal( jobConfiguration );
        Assert.assertEquals( 1, errorReports.size() );
        Assert.assertEquals( ErrorCode.E7010, errorReports.get( 0 ).getErrorCode() );
    }

    @Test
    public void validateInternalNonConfigurableShownValidationErrorE7010NoPrevious()
    {
        Mockito.when( jobConfigurationService.getJobConfigurationByUid( Mockito.eq( "jsdhJSJHD" ) ) )
            .thenReturn( null );
        Mockito.when( schedulingManager.getJob( Mockito.eq( JobType.ANALYTICSTABLE_UPDATE ) ) )
            .thenReturn( job );
        Mockito.when( job.validate() ).thenReturn( new ErrorReport( Class.class, ErrorCode.E7010 ) );

        analyticsTableJobConfig.setJobType( JobType.ANALYTICSTABLE_UPDATE );
        JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setUid( "jsdhJSJHD" );
        jobConfiguration.setJobType( JobType.ANALYTICSTABLE_UPDATE );
        jobConfiguration.setCronExpression( CRON_HOURLY );
        jobConfiguration.setEnabled( true );

        List<ErrorReport> errorReports = hook.validateInternal( jobConfiguration );
        Assert.assertEquals( 1, errorReports.size() );
        Assert.assertEquals( ErrorCode.E7010, errorReports.get( 0 ).getErrorCode() );
    }

    @Test
    public void validateInternalNonConfigurableIgnoredValidationErrorE7010()
    {
        Mockito.when( jobConfigurationService.getJobConfigurationByUid( Mockito.eq( "jsdhJSJHD" ) ) )
            .thenReturn( analyticsTableJobConfig );
        Mockito.when( schedulingManager.getJob( Mockito.eq( JobType.ANALYTICSTABLE_UPDATE ) ) )
            .thenReturn( job );
        Mockito.when( job.validate() ).thenReturn( new ErrorReport( Class.class, ErrorCode.E7010 ) );

        JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setUid( "jsdhJSJHD" );
        jobConfiguration.setJobType( JobType.ANALYTICSTABLE_UPDATE );
        jobConfiguration.setCronExpression( CRON_HOURLY );
        jobConfiguration.setEnabled( true );

        List<ErrorReport> errorReports = hook.validateInternal( jobConfiguration );
        Assert.assertEquals( 0, errorReports.size() );
    }

    @Test
    public void validateCronExpressionForCronTypeJobs()
    {
        String jobConfigUid = "jsdhJSJHD";
        Mockito.when( jobConfigurationService.getJobConfigurationByUid( Mockito.eq( jobConfigUid ) ) )
            .thenReturn( analyticsTableJobConfig );
        Mockito.when( schedulingManager.getJob( Mockito.eq( JobType.ANALYTICSTABLE_UPDATE ) ) )
            .thenReturn( job );

        JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setUid( jobConfigUid );
        jobConfiguration.setJobType( JobType.ANALYTICSTABLE_UPDATE );
        jobConfiguration.setEnabled( true );

        List<ErrorReport> errorReports = hook.validateInternal( jobConfiguration );
        Assert.assertEquals( 1, errorReports.size() );
        Assert.assertEquals( ErrorCode.E7004, errorReports.get( 0 ).getErrorCode() );
    }

    @Test
    public void validateDelayForFixedIntervalTypeJobs()
    {
        String jobConfigUid = "o8kG3Qk3nG3";
        JobConfiguration contAnalyticsTableJobConfig = new JobConfiguration();
        contAnalyticsTableJobConfig.setUid( jobConfigUid );
        contAnalyticsTableJobConfig.setJobType( JobType.CONTINUOUS_ANALYTICS_TABLE );

        Mockito.when( jobConfigurationService.getJobConfigurationByUid( Mockito.eq( jobConfigUid ) ) )
            .thenReturn( contAnalyticsTableJobConfig );
        Mockito.when( schedulingManager.getJob( Mockito.eq( JobType.CONTINUOUS_ANALYTICS_TABLE ) ) )
            .thenReturn( job );

        JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setUid( jobConfigUid );
        jobConfiguration.setJobType( JobType.CONTINUOUS_ANALYTICS_TABLE );
        jobConfiguration.setJobParameters( new ContinuousAnalyticsJobParameters( 1, null, null ) );

        List<ErrorReport> errorReports = hook.validateInternal( jobConfiguration );
        Assert.assertEquals( 1, errorReports.size() );
        Assert.assertEquals( ErrorCode.E7007, errorReports.get( 0 ).getErrorCode() );
    }
}
