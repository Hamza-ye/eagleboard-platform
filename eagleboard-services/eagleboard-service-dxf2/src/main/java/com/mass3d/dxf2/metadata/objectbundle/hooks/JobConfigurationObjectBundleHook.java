package com.mass3d.dxf2.metadata.objectbundle.hooks;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.mass3d.scheduling.JobStatus.DISABLED;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.commons.util.DebugUtils;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.scheduling.*;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobConfigurationObjectBundleHook
    extends AbstractObjectBundleHook
{
    private final JobConfigurationService jobConfigurationService;

    private final SchedulingManager schedulingManager;

    public JobConfigurationObjectBundleHook( JobConfigurationService jobConfigurationService,
        SchedulingManager schedulingManager )
    {
        checkNotNull( jobConfigurationService );
        checkNotNull( schedulingManager );

        this.jobConfigurationService = jobConfigurationService;
        this.schedulingManager = schedulingManager;
    }

    @Override
    public <T extends IdentifiableObject> List<ErrorReport> validate( T object, ObjectBundle bundle )
    {
        if ( !JobConfiguration.class.isInstance( object ) )
        {
            return new ArrayList<>();
        }

        JobConfiguration jobConfiguration = (JobConfiguration) object;
        List<ErrorReport> errorReports = new ArrayList<>( validateInternal( jobConfiguration ) );

        if ( errorReports.size() == 0 )
        {
            jobConfiguration.setNextExecutionTime( null );

            log.info( "Validation of '" + jobConfiguration.getName() + "' succeeded" );
        }
        else
        {
            log.info( "Validation of '" + jobConfiguration.getName() + "' failed." );
            log.info( errorReports.toString() );
        }

        return errorReports;
    }

    @Override
    public <T extends IdentifiableObject> void preCreate( T object, ObjectBundle bundle )
    {
        if ( !( object instanceof JobConfiguration ) )
        {
            return;
        }

        JobConfiguration jobConfiguration = (JobConfiguration) object;
        ensureDefaultJobParametersAreUsedIfNoOtherArePresent( jobConfiguration );
    }

    @Override
    public void preUpdate( IdentifiableObject object, IdentifiableObject persistedObject, ObjectBundle bundle )
    {
        if ( !JobConfiguration.class.isInstance( object ) )
        {
            return;
        }

        JobConfiguration newObject = (JobConfiguration) object;
        JobConfiguration persObject = (JobConfiguration) persistedObject;

        newObject.setLastExecuted( persObject.getLastExecuted() );
        newObject.setLastExecutedStatus( persObject.getLastExecutedStatus() );
        newObject.setLastRuntimeExecution( persObject.getLastRuntimeExecution() );

        ensureDefaultJobParametersAreUsedIfNoOtherArePresent( newObject );

        schedulingManager.stopJob( (JobConfiguration) persistedObject );
    }

    @Override
    public <T extends IdentifiableObject> void preDelete( T persistedObject, ObjectBundle bundle )
    {
        if ( !JobConfiguration.class.isInstance( persistedObject ) )
        {
            return;
        }

        schedulingManager.stopJob( (JobConfiguration) persistedObject );
        sessionFactory.getCurrentSession().delete( persistedObject );
    }

    @Override
    public <T extends IdentifiableObject> void postCreate( T persistedObject, ObjectBundle bundle )
    {
        if ( !JobConfiguration.class.isInstance( persistedObject ) )
        {
            return;
        }

        JobConfiguration jobConfiguration = (JobConfiguration) persistedObject;

        if ( jobConfiguration.getJobStatus() != DISABLED )
        {
            schedulingManager.scheduleJob( jobConfiguration );
        }
    }

    @Override
    public <T extends IdentifiableObject> void postUpdate( T persistedObject, ObjectBundle bundle )
    {
        if ( !JobConfiguration.class.isInstance( persistedObject ) )
        {
            return;
        }

        JobConfiguration jobConfiguration = (JobConfiguration) persistedObject;

        if ( jobConfiguration.getJobStatus() != DISABLED )
        {
            schedulingManager.scheduleJob( jobConfiguration );
        }
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /*
     *  Validates that there are no other jobs of the same job type which are scheduled with the same cron expression.
     */
    private void validateCronExpressionWithinJobType( List<ErrorReport> errorReports, JobConfiguration jobConfiguration )
    {
        Set<JobConfiguration> jobConfigs = jobConfigurationService.getAllJobConfigurations().stream()
            .filter( jobConfig -> jobConfig.getJobType().equals( jobConfiguration.getJobType() ) && !Objects
                .equals( jobConfig.getUid(), jobConfiguration.getUid() ) )
            .collect( Collectors.toSet() );

        for ( JobConfiguration jobConfig : jobConfigs )
        {
            if ( jobConfig.hasCronExpression() && jobConfig.getCronExpression().equals( jobConfiguration.getCronExpression() ) )
            {
                errorReports.add( new ErrorReport( JobConfiguration.class, ErrorCode.E7000, jobConfig.getCronExpression() ) );
            }
        }
    }

    List<ErrorReport> validateInternal( final JobConfiguration jobConfiguration )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        // Check whether jobConfiguration already exists in the system and if so validate it

        JobConfiguration persistedJobConfiguration = jobConfigurationService.getJobConfigurationByUid( jobConfiguration.getUid() );

        final JobConfiguration tempJobConfiguration = validatePersistedAndPrepareTempJobConfiguration( errorReports,
            jobConfiguration, persistedJobConfiguration );

        validateJobConfigurationCronOrFixedDelay( errorReports, tempJobConfiguration );
        validateCronExpressionWithinJobType( errorReports, tempJobConfiguration );

        // Validate parameters

        if ( tempJobConfiguration.getJobParameters() != null )
        {
            tempJobConfiguration.getJobParameters().validate().ifPresent( errorReports::add );
        }
        else
        {
            // Report error if JobType requires JobParameters, but it does not exist in JobConfiguration

            if ( tempJobConfiguration.getJobType().hasJobParameters() )
            {
                errorReports.add( new ErrorReport( this.getClass(), ErrorCode.E4029, tempJobConfiguration.getJobType() ) );
            }
        }

        validateJob( errorReports, tempJobConfiguration, persistedJobConfiguration );

        return errorReports;
    }

    private JobConfiguration validatePersistedAndPrepareTempJobConfiguration( List<ErrorReport> errorReports,
        JobConfiguration jobConfiguration, JobConfiguration persistedJobConfiguration )
    {
        if ( persistedJobConfiguration != null && !persistedJobConfiguration.isConfigurable() )
        {
            if ( persistedJobConfiguration.hasNonConfigurableJobChanges( jobConfiguration ) )
            {
                errorReports.add( new ErrorReport( JobConfiguration.class, ErrorCode.E7003, jobConfiguration.getJobType() ) );
            }
            else
            {
                persistedJobConfiguration.setCronExpression( jobConfiguration.getCronExpression() );
                return persistedJobConfiguration;
            }
        }

        return jobConfiguration;
    }

    private void validateJobConfigurationCronOrFixedDelay( List<ErrorReport> errorReports,
        JobConfiguration jobConfiguration )
    {
        if ( jobConfiguration.getJobType().isCronSchedulingType() )
        {
            if ( jobConfiguration.getCronExpression() == null )
            {
                errorReports.add( new ErrorReport( JobConfiguration.class, ErrorCode.E7004, jobConfiguration.getUid() ) );
            }
            else if ( !CronSequenceGenerator.isValidExpression( jobConfiguration.getCronExpression() ) )
            {
                errorReports.add( new ErrorReport( JobConfiguration.class, ErrorCode.E7005 ) );
            }
        }

        if ( jobConfiguration.getJobType().isFixedDelaySchedulingType() && jobConfiguration.getDelay() == null )
        {
            errorReports.add( new ErrorReport( JobConfiguration.class, ErrorCode.E7007, jobConfiguration.getUid() ) );
        }
    }

    private void validateJob( List<ErrorReport> errorReports, JobConfiguration jobConfiguration,
        JobConfiguration persistedJobConfiguration )
    {
        Job job = schedulingManager.getJob( jobConfiguration.getJobType() );
        ErrorReport jobValidation = job.validate();

        if ( jobValidation != null && ( jobValidation.getErrorCode() != ErrorCode.E7010 || persistedJobConfiguration == null || jobConfiguration.isConfigurable() ))
        {
            // If the error is caused by the environment and the job is a non-configurable job that already exists,
            // then the error can be ignored as the job has the issue with and without updating it.

            errorReports.add( jobValidation );
        }
    }

    private void ensureDefaultJobParametersAreUsedIfNoOtherArePresent( JobConfiguration jobConfiguration )
    {
        if ( !jobConfiguration.isInMemoryJob() )
        {
            if ( jobConfiguration.getJobParameters() == null )
            {
                jobConfiguration.setJobParameters( getDefaultJobParameters( jobConfiguration ) );
            }
        }
    }

    private JobParameters getDefaultJobParameters( JobConfiguration jobConfiguration )
    {
        if ( jobConfiguration.getJobType().getJobParameters() == null )
        {
            return null;
        }

        try
        {
            return jobConfiguration.getJobType().getJobParameters().newInstance();
        }
        catch ( InstantiationException | IllegalAccessException ex )
        {
            log.error( DebugUtils.getStackTrace( ex ) );
        }

        return null;
    }
}
