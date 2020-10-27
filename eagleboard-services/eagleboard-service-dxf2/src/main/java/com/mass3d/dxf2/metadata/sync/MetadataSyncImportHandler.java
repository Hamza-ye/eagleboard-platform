package com.mass3d.dxf2.metadata.sync;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.MetadataImportParams;
import com.mass3d.dxf2.metadata.MetadataImportService;
import com.mass3d.dxf2.metadata.feedback.ImportReport;
import com.mass3d.dxf2.metadata.sync.exception.MetadataSyncImportException;
import com.mass3d.dxf2.metadata.sync.exception.MetadataSyncServiceException;
import com.mass3d.dxf2.metadata.version.MetadataVersionDelegate;
import com.mass3d.dxf2.metadata.version.exception.MetadataVersionServiceException;
import com.mass3d.feedback.Status;
import com.mass3d.metadata.version.MetadataVersion;
import com.mass3d.metadata.version.VersionType;
import com.mass3d.render.RenderFormat;
import com.mass3d.render.RenderService;
import com.mass3d.scheduling.JobConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Import handler for metadata sync service
 *
 */
@Slf4j
@Component( "com.mass3d.dxf2.metadata.sync.MetadataImportHandler" )
@Scope("prototype")
public class MetadataSyncImportHandler
{
    @Autowired
    private MetadataVersionDelegate metadataVersionDelegate;

    @Autowired
    private RenderService renderService;

    @Autowired
    private MetadataImportService metadataImportService;

    public MetadataSyncSummary importMetadata( MetadataSyncParams syncParams, String versionSnapShot )
    {
        MetadataVersion version = getMetadataVersion( syncParams );
        MetadataImportParams importParams = syncParams.getImportParams();
        MetadataSyncSummary metadataSyncSummary = new MetadataSyncSummary();

        if ( importParams == null )
        {
            throw new MetadataSyncServiceException( "MetadataImportParams for the Sync cant be null." );
        }

        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> classListMap = parseClassListMap(
            versionSnapShot );

        if ( classListMap == null )
        {
            throw new MetadataSyncServiceException( "ClassListMap can't be null" );
        }

        // Job configurations should not be imported from any source
        // (neither by normal metadata import nor by sync).
        classListMap.remove( JobConfiguration.class );
        importParams.setObjects( classListMap );

        ImportReport importReport = null;

        try
        {
            importReport = metadataImportService.importMetadata( importParams );

        }
        catch ( Exception e )
        {
            String message = "Exception occurred while trying to import the metadata. " + e.getMessage();
            log.error( message,e );
            throw new MetadataSyncImportException( message,e );
        }

        boolean addNewVersion = handleImportReport( importReport, version );

        if ( addNewVersion )
        {
            try
            {
                metadataVersionDelegate.addNewMetadataVersion( version );
            }
            catch ( MetadataVersionServiceException e )
            {
                throw new MetadataSyncServiceException( e.getMessage(), e );
            }

        }

        metadataSyncSummary.setImportReport( importReport );
        metadataSyncSummary.setMetadataVersion( version );

        return metadataSyncSummary;
    }

    //----------------------------------------------------------------------------------------
    // Private Methods
    //----------------------------------------------------------------------------------------

    private boolean handleImportReport( ImportReport importReport, MetadataVersion version )
    {
        if ( importReport == null )
        {
            return false;
        }

        Status importStatus = importReport.getStatus();
        return importStatus.equals( Status.OK ) || isBestEffort( version, importStatus );
    }

    private boolean isBestEffort( MetadataVersion version, Status importStatus )
    {
        return importStatus.equals( Status.WARNING ) && VersionType.BEST_EFFORT.equals( version.getType() );
    }

    private Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> parseClassListMap(
        String metadataVersionSnapshot )
    {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
            metadataVersionSnapshot.getBytes( StandardCharsets.UTF_8 ) );

        try
        {
            return renderService.fromMetadata( byteArrayInputStream, RenderFormat.JSON );
        }
        catch ( IOException ex )
        {
            String message = "Exception occurred while trying to do JSON conversion while parsing class list map";
            log.error( message );
            throw new MetadataSyncServiceException( message, ex );
        }
        catch ( Exception ex )
        {
            throw new MetadataSyncServiceException( ex.getMessage(), ex );
        }
    }

    private MetadataVersion getMetadataVersion( MetadataSyncParams syncParams )
    {
        if ( syncParams == null )
        {
            throw new MetadataSyncServiceException( "MetadataSyncParams cant be null" );
        }

        MetadataVersion version = syncParams.getVersion();

        if ( version == null )
        {
            throw new MetadataSyncServiceException( "MetadataVersion for the Sync cant be null." );
        }

        return version;
    }
}
