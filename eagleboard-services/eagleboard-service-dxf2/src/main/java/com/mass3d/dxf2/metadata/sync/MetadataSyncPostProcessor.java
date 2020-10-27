package com.mass3d.dxf2.metadata.sync;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.dxf2.metadata.feedback.ImportReport;
import com.mass3d.dxf2.metadata.jobs.MetadataRetryContext;
import com.mass3d.dxf2.metadata.jobs.MetadataSyncJob;
import com.mass3d.email.Email;
import com.mass3d.email.EmailService;
import com.mass3d.feedback.Stats;
import com.mass3d.feedback.Status;
import com.mass3d.feedback.TypeReport;
import com.mass3d.metadata.version.MetadataVersion;
import com.mass3d.metadata.version.VersionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Performs the tasks after metadata sync happens
 *
 */
@Slf4j
@Component( "metadataSyncPostProcessor" )
@Scope("prototype")
public class MetadataSyncPostProcessor
{
    @Autowired
    private EmailService emailService;

    public boolean handleSyncNotificationsAndAbortStatus( MetadataSyncSummary metadataSyncSummary, 
        MetadataRetryContext retryContext, MetadataVersion dataVersion )
    {
        ImportReport importReport = metadataSyncSummary.getImportReport();

        if ( importReport == null )
        {
            handleImportFailedContext( null, retryContext, dataVersion );

            return true;
        }

        Status syncStatus = importReport.getStatus();
        log.info( "Import completed. Import Status: " + syncStatus );

        if ( Status.OK.equals( syncStatus ) || ( Status.WARNING.equals( syncStatus ) && VersionType.BEST_EFFORT.equals( dataVersion.getType() ) ) )
        {
            sendSuccessMailToAdmin( metadataSyncSummary );
            return false;
        }

        if ( Status.ERROR.equals( syncStatus ) )
        {
            handleImportFailedContext( metadataSyncSummary, retryContext, dataVersion );
            return true;
        }

        return false;
    }

    public void handleVersionAlreadyExists ( MetadataRetryContext retryContext, MetadataVersion dataVersion )
    {
        retryContext.updateRetryContext( MetadataSyncJob.METADATA_SYNC, "Version already exists in system and hence stopping the sync", dataVersion, null );
        sendFailureMailToAdmin( retryContext );
        log.info( "Aborting Metadata sync. Version already exists in system and hence stopping the sync. Check mail and logs for more details." );
    }

    private void handleImportFailedContext( MetadataSyncSummary metadataSyncSummary, MetadataRetryContext retryContext, MetadataVersion dataVersion )
    {
        retryContext.updateRetryContext( MetadataSyncJob.METADATA_SYNC, "Import of metadata objects was unsuccessful", dataVersion, metadataSyncSummary );
        sendFailureMailToAdmin( retryContext );
        log.info( "Aborting Metadata sync Import Failure happened. Check mail and logs for more details." );
    }

    public void sendSuccessMailToAdmin( MetadataSyncSummary metadataSyncSummary )
    {
        ImportReport importReport = metadataSyncSummary.getImportReport();
        StringBuilder text = new StringBuilder( "Successful Import Report for the scheduler run for Metadata synchronization \n\n" )
            .append( "Imported Version Details \n " )
            .append( "Version Name: " + metadataSyncSummary.getMetadataVersion().getName() + "\n" )
            .append( "Version Type: " + metadataSyncSummary.getMetadataVersion().getType() + "\n" );

        Map<Class<?>, TypeReport> typeReportMap = importReport.getTypeReportMap();

        if ( typeReportMap != null && typeReportMap.entrySet().size() == 0 )
        {
            text.append( "New Version created. It does not have any metadata changes. \n" );
        }
        else if ( typeReportMap != null )
        {
            text.append( "Imported Object Details: \n" );

            for ( Map.Entry<Class<?>, TypeReport> typeReportEntry : typeReportMap.entrySet() )
            {
                if ( typeReportEntry != null )
                {
                    Class<?> key = typeReportEntry.getKey();
                    TypeReport value = typeReportEntry.getValue();
                    Stats stats = value.getStats();

                    text.append( "Metadata Object Type: " )
                        .append( key )
                        .append( "\n" )
                        .append( "Stats: \n" )
                        .append( "total: " + stats.getTotal() + "\n" );

                    if ( stats.getCreated() > 0 )
                    {
                        text.append( " created: " + stats.getCreated() + "\n" );
                    }

                    if ( stats.getUpdated() > 0 )
                    {
                        text.append( " updated: " + stats.getUpdated() + "\n" );
                    }

                    if ( stats.getIgnored() > 0 )
                    {
                        text.append( " ignored: " + stats.getIgnored() + "\n" );
                    }
                }

            }

            text.append( "\n\n" );

        }

        if ( text.length() > 0 )
        {
            log.info( "Success mail will be sent with the following message: " + text );
            emailService.sendSystemEmail( new Email( "Success Notification: Metadata Synchronization", text.toString() ) );
        }
    }

    public void sendFailureMailToAdmin( MetadataRetryContext retryContext )
    {
        StringBuilder text = new StringBuilder( "Following Exceptions were encountered while the scheduler run for metadata sync \n\n" );

        for ( String name : MetadataSyncJob.keys )
        {
            Object value = retryContext.getRetryContext().getAttribute( name );

            if ( value != null )
            {
                text.append( "ERROR_CATEGORY " )
                    .append( ": " ).append( name )
                    .append( "\n ERROR_VALUE : " ).append( value )
                    .append( "\n\n" );
            }
        }

        Object report = retryContext.getRetryContext().getAttribute( MetadataSyncJob.METADATA_SYNC_REPORT );

        if ( report != null )
        {
            String reportString = (String) report;
            
            text.append( MetadataSyncJob.METADATA_SYNC_REPORT )
                .append( "\n " )
                .append( reportString );
        }
        else
        {
            if ( retryContext.getRetryContext().getLastThrowable() != null )
            {
                text.append( retryContext.getRetryContext().getLastThrowable().getMessage() );
            }
        }

        if ( text.length() > 0 )
        {
            log.info( "Failure mail will be sent with the following message: " + text );
            emailService.sendSystemEmail( new Email( "Action Required: MetadataSync Failed Notification", text.toString() ) );
        }
    }
}
