package com.mass3d.dxf2.metadata.jobs;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.dxf2.metadata.MetadataImportParams;
import com.mass3d.dxf2.metadata.sync.*;
import com.mass3d.dxf2.metadata.sync.exception.DhisVersionMismatchException;
import com.mass3d.dxf2.metadata.sync.exception.MetadataSyncServiceException;
import com.mass3d.dxf2.sync.SynchronizationJob;
import com.mass3d.dxf2.synch.SynchronizationManager;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.metadata.version.MetadataVersion;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.scheduling.JobType;
import com.mass3d.scheduling.parameters.MetadataSyncJobParameters;
import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 * This is the runnable that takes care of the Metadata Synchronization.
 * Leverages Spring RetryTemplate to exhibit retries. The retries are configurable
 * through the dhis.conf.
 *
 */
@Slf4j
@Component( "metadataSyncJob" )
public class MetadataSyncJob extends SynchronizationJob
{
    public static final String VERSION_KEY = "version";
    public static final String DATA_PUSH_SUMMARY = "dataPushSummary";
    public static final String EVENT_PUSH_SUMMARY = "eventPushSummary";
    public static final String TRACKER_PUSH_SUMMARY = "trackerPushSummary";
    public static final String GET_METADATAVERSION = "getMetadataVersion";
    public static final String GET_METADATAVERSIONSLIST = "getMetadataVersionsList";
    public static final String METADATA_SYNC = "metadataSync";
    public static final String METADATA_SYNC_REPORT = "metadataSyncReport";
    public static final String[] keys = { DATA_PUSH_SUMMARY, EVENT_PUSH_SUMMARY, GET_METADATAVERSION, GET_METADATAVERSIONSLIST, METADATA_SYNC, VERSION_KEY };

    private final SystemSettingManager systemSettingManager;

    private final RetryTemplate retryTemplate;

    private final SynchronizationManager synchronizationManager;

    private final MetadataSyncPreProcessor metadataSyncPreProcessor;

    private final MetadataSyncPostProcessor metadataSyncPostProcessor;

    private final MetadataSyncService metadataSyncService;

    private final MetadataRetryContext metadataRetryContext;

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    public MetadataSyncJob( SystemSettingManager systemSettingManager, RetryTemplate retryTemplate,
        SynchronizationManager synchronizationManager, MetadataSyncPreProcessor metadataSyncPreProcessor,
        MetadataSyncPostProcessor metadataSyncPostProcessor, MetadataSyncService metadataSyncService,
        MetadataRetryContext metadataRetryContext )
    {

        checkNotNull( systemSettingManager );
        checkNotNull( retryTemplate );
        checkNotNull( synchronizationManager );
        checkNotNull( metadataSyncPreProcessor );
        checkNotNull( metadataSyncPostProcessor );
        checkNotNull( metadataSyncService );
        checkNotNull( metadataRetryContext );

        this.systemSettingManager = systemSettingManager;
        this.retryTemplate = retryTemplate;
        this.synchronizationManager = synchronizationManager;
        this.metadataSyncPreProcessor = metadataSyncPreProcessor;
        this.metadataSyncPostProcessor = metadataSyncPostProcessor;
        this.metadataSyncService = metadataSyncService;
        this.metadataRetryContext = metadataRetryContext;
    }

    @Override
    public JobType getJobType()
    {
        return JobType.META_DATA_SYNC;
    }

    @Override
    public void execute( JobConfiguration jobConfiguration )
    {
        log.info( "Metadata Sync cron Job started" );

        try
        {
            MetadataSyncJobParameters jobParameters = (MetadataSyncJobParameters) jobConfiguration.getJobParameters();
            retryTemplate.execute( retryContext ->
                {
                    metadataRetryContext.setRetryContext( retryContext );
                    clearFailedVersionSettings();
                    runSyncTask( metadataRetryContext, jobParameters );
                    return null;
                }
                , retryContext ->
                {
                    log.info( "Metadata Sync failed! Sending mail to Admin" );
                    updateMetadataVersionFailureDetails( metadataRetryContext );
                    metadataSyncPostProcessor.sendFailureMailToAdmin( metadataRetryContext );
                    return null;
                } );
        }
        catch ( Exception e )
        {
            String customMessage = "Exception occurred while executing metadata sync task." + e.getMessage();
            log.error( customMessage, e );
        }
    }

    @Override
    public ErrorReport validate()
    {
        Optional<ErrorReport> errorReport = validateRemoteServerAvailability( synchronizationManager,
            MetadataSyncJob.class );

        return errorReport.orElse( super.validate() );
    }

    synchronized void runSyncTask( MetadataRetryContext context, MetadataSyncJobParameters jobParameters )
        throws MetadataSyncServiceException, DhisVersionMismatchException
    {
        metadataSyncPreProcessor.setUp( context );

//        metadataSyncPreProcessor.handleDataValuePush( context, jobParameters );

//        metadataSyncPreProcessor.handleEventProgramsDataPush( context, jobParameters );
//        metadataSyncPreProcessor.handleCompleteDataSetRegistrationDataPush( context );
//        metadataSyncPreProcessor.handleTrackerProgramsDataPush( context, jobParameters );

        MetadataVersion metadataVersion = metadataSyncPreProcessor.handleCurrentMetadataVersion( context );

        List<MetadataVersion> metadataVersionList = metadataSyncPreProcessor.handleMetadataVersionsList( context, metadataVersion );

        if ( metadataVersionList != null )
        {
            for ( MetadataVersion dataVersion : metadataVersionList )
            {
                MetadataSyncParams syncParams = new MetadataSyncParams( new MetadataImportParams(), dataVersion );
                boolean isSyncRequired = metadataSyncService.isSyncRequired( syncParams );
                MetadataSyncSummary metadataSyncSummary = null;

                if ( isSyncRequired )
                {
                    metadataSyncSummary = handleMetadataSync( context, dataVersion );
                }
                else
                {
                    metadataSyncPostProcessor.handleVersionAlreadyExists( context, dataVersion );
                    break;
                }

                boolean abortStatus = metadataSyncPostProcessor.handleSyncNotificationsAndAbortStatus( metadataSyncSummary, context, dataVersion );

                if ( abortStatus )
                {
                    break;
                }

                clearFailedVersionSettings();
            }
        }

        log.info( "Metadata sync cron job ended " );
    }

    //----------------------------------------------------------------------------------------
    // Private Methods
    //----------------------------------------------------------------------------------------

    private MetadataSyncSummary handleMetadataSync( MetadataRetryContext context, MetadataVersion dataVersion ) throws DhisVersionMismatchException
    {

        MetadataSyncParams syncParams = new MetadataSyncParams( new MetadataImportParams(), dataVersion );
        MetadataSyncSummary metadataSyncSummary = null;

        try
        {
            metadataSyncSummary = metadataSyncService.doMetadataSync( syncParams );
        }
        catch ( MetadataSyncServiceException e )
        {
            log.error( "Exception happened  while trying to do metadata sync  " + e.getMessage(), e );
            context.updateRetryContext( METADATA_SYNC, e.getMessage(), dataVersion );
            throw e;
        }
        catch ( DhisVersionMismatchException e )
        {
            context.updateRetryContext( METADATA_SYNC, e.getMessage(), dataVersion );
            throw e;
        }
        return metadataSyncSummary;

    }

    private void updateMetadataVersionFailureDetails( MetadataRetryContext retryContext )
    {
        Object version = retryContext.getRetryContext().getAttribute( VERSION_KEY );

        if ( version != null )
        {
            MetadataVersion metadataVersion = (MetadataVersion) version;
            systemSettingManager.saveSystemSetting( SettingKey.METADATA_FAILED_VERSION, metadataVersion.getName() );
            systemSettingManager.saveSystemSetting( SettingKey.METADATA_LAST_FAILED_TIME, new Date() );
        }
    }

    private void clearFailedVersionSettings()
    {
        systemSettingManager.deleteSystemSetting( SettingKey.METADATA_FAILED_VERSION );
        systemSettingManager.deleteSystemSetting( SettingKey.METADATA_LAST_FAILED_TIME );
    }
}
