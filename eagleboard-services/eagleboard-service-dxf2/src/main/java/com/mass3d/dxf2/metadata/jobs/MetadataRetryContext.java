package com.mass3d.dxf2.metadata.jobs;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.dxf2.metadata.feedback.ImportReport;
import com.mass3d.dxf2.metadata.sync.MetadataSyncSummary;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.feedback.Status;
import com.mass3d.metadata.version.MetadataVersion;
import org.springframework.context.annotation.Scope;
import org.springframework.retry.RetryContext;
import org.springframework.stereotype.Component;

/**
 * Defines retry mechanism for metadata sync scheduling
 *
 * @author aamerm
 */
@Slf4j
@Component( "metadataRetryContext" )
@Scope( "prototype" )
public class MetadataRetryContext
{
    private RetryContext retryContext;

    public RetryContext getRetryContext()
    {
        return retryContext;
    }

    public void setRetryContext( RetryContext retryContext )
    {
        this.retryContext = retryContext;
        log.info( "Now trying. Current count: " + (retryContext.getRetryCount() + 1) );
    }

    public void updateRetryContext( String stepKey,
        String message, MetadataVersion version )
    {
        retryContext.setAttribute( stepKey, message );

        if ( version != null )
        {
            retryContext.setAttribute( MetadataSyncJob.VERSION_KEY, version );
        }
    }

    public void updateRetryContext( String stepKey, String message, MetadataVersion version, MetadataSyncSummary summary )
    {
        updateRetryContext( stepKey, message, version );

        if ( summary != null )
        {
            setupImportReport( summary.getImportReport() );
        }
    }

    //----------------------------------------------------------------------------------------
    // Private Methods
    //----------------------------------------------------------------------------------------

    private void setupImportReport( ImportReport importReport )
    {
        Status status = importReport.getStatus();

        if ( Status.ERROR.equals( status ) )
        {
            StringBuilder report = new StringBuilder();
            List<ErrorReport> errorReports = importReport.getErrorReports();

            for ( ErrorReport errorReport : errorReports )
            {

                if ( errorReport != null )
                {
                    report.append( errorReport.toString() + "\n" );
                }

            }

            retryContext.setAttribute( MetadataSyncJob.METADATA_SYNC_REPORT, report.toString() );
        }
    }
}
