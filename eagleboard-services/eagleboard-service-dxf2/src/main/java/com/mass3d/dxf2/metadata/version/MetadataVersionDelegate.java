package com.mass3d.dxf2.metadata.version;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.dxf2.metadata.sync.exception.RemoteServerUnavailableException;
import com.mass3d.dxf2.metadata.systemsettings.DefaultMetadataSystemSettingService;
import com.mass3d.dxf2.metadata.version.exception.MetadataVersionServiceException;
import com.mass3d.dxf2.synch.AvailabilityStatus;
import com.mass3d.dxf2.synch.SynchronizationManager;
import com.mass3d.metadata.version.MetadataVersion;
import com.mass3d.metadata.version.MetadataVersionService;
import com.mass3d.render.RenderFormat;
import com.mass3d.render.RenderService;
import com.mass3d.system.util.DhisHttpResponse;
import com.mass3d.system.util.HttpUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Handling remote calls for metadata version.
 *
 */
@Slf4j
@Component( "com.mass3d.dxf2.metadata.version.MetadataVersionDelegate" )
@Scope("prototype")
public class MetadataVersionDelegate
{
    private DefaultMetadataSystemSettingService metadataSystemSettingService;

    private SynchronizationManager synchronizationManager;

    private RenderService renderService;

    private MetadataVersionService metadataVersionService;

    public MetadataVersionDelegate( DefaultMetadataSystemSettingService metadataSystemSettingService,
        SynchronizationManager synchronizationManager, RenderService renderService,
        MetadataVersionService metadataVersionService )
    {
        checkNotNull(metadataSystemSettingService);
        checkNotNull(synchronizationManager);
        checkNotNull(renderService);
        checkNotNull(metadataVersionService);

        this.metadataSystemSettingService = metadataSystemSettingService;
        this.synchronizationManager = synchronizationManager;
        this.renderService = renderService;
        this.metadataVersionService = metadataVersionService;
    }

    private int VERSION_TIMEOUT = 120000;

    private int DOWNLOAD_TIMEOUT = 300000;

    public MetadataVersion getRemoteMetadataVersion( String versionName )
    {
        String versionDetailsUrl = metadataSystemSettingService.getVersionDetailsUrl( versionName );
        DhisHttpResponse dhisHttpResponse = getDhisHttpResponse( versionDetailsUrl, VERSION_TIMEOUT );
        MetadataVersion dataVersion = null;

        if ( isValidDhisHttpResponse( dhisHttpResponse ) )
        {
            try
            {
                dataVersion = renderService.fromJson( dhisHttpResponse.getResponse(), MetadataVersion.class );
            }
            catch ( Exception e )
            {
                String message = "Exception occurred while trying to do JSON conversion for metadata version";
                log.error( message, e );
                throw new MetadataVersionServiceException( message, e );
            }
        }

        return dataVersion;
    }

    public List<MetadataVersion> getMetaDataDifference( MetadataVersion metadataVersion )
    {
        String url;
        List<MetadataVersion> metadataVersions = new ArrayList<>();
        
        if ( metadataVersion == null )
        {
            url = metadataSystemSettingService.getEntireVersionHistory();
        }
        else
        {
            url = metadataSystemSettingService.getMetaDataDifferenceURL( metadataVersion.getName() );
        }

        DhisHttpResponse dhisHttpResponse = getDhisHttpResponse( url, VERSION_TIMEOUT );

        if ( isValidDhisHttpResponse( dhisHttpResponse ) )
        {
            try
            {
                metadataVersions = renderService
                    .fromMetadataVersion( new ByteArrayInputStream( dhisHttpResponse.getResponse().getBytes() ),
                        RenderFormat.JSON );
                return metadataVersions;
            }
            catch ( IOException io )
            {
                String message =
                    "Exception occurred while trying to do JSON conversion. Caused by:  " + io.getMessage();
                log.error( message, io );
                throw new MetadataVersionServiceException( message, io );
            }
        }

        log.warn( "Returning empty for the metadata versions difference" );
        return metadataVersions;
    }

    public String downloadMetadataVersionSnapshot(MetadataVersion version )
        throws MetadataVersionServiceException
    {
        String downloadVersionSnapshotURL = metadataSystemSettingService.getDownloadVersionSnapshotURL( version.getName() );
        DhisHttpResponse dhisHttpResponse = getDhisHttpResponse( downloadVersionSnapshotURL, DOWNLOAD_TIMEOUT );

        if ( isValidDhisHttpResponse( dhisHttpResponse ) )
        {
            return dhisHttpResponse.getResponse();
        }

        return null;
    }

    public synchronized void addNewMetadataVersion( MetadataVersion version )
    {
        version.setImportDate( new Date() );

        try
        {
            metadataVersionService.addVersion( version );
        }
        catch ( Exception e )
        {
            throw new MetadataVersionServiceException( "Exception occurred while trying to add metadata version" + version, e );
        }
    }

    //----------------------------------------------------------------------------------------
    // Private Methods
    //----------------------------------------------------------------------------------------

    private DhisHttpResponse getDhisHttpResponse( String url, int timeout )
    {
        AvailabilityStatus remoteServerAvailable = synchronizationManager.isRemoteServerAvailable();

        if ( !( remoteServerAvailable.isAvailable() ) )
        {
            String message = remoteServerAvailable.getMessage();
            log.error( message );
            throw new RemoteServerUnavailableException( message );
        }

        String username = metadataSystemSettingService.getRemoteInstanceUserName();
        String password = metadataSystemSettingService.getRemoteInstancePassword();

        log.info( "Remote server metadata version  URL: " + url + ", username: " + username );
        DhisHttpResponse dhisHttpResponse;

        try
        {
            dhisHttpResponse = HttpUtils.httpGET( url, true, username, password, null, timeout, true );
        }
        catch ( Exception e )
        {
            String message = "Exception occurred while trying to make the GET call to URL: " + url;
            log.error( message, e );
            throw new MetadataVersionServiceException( message, e );
        }

        return dhisHttpResponse;
    }

    private boolean isValidDhisHttpResponse( DhisHttpResponse dhisHttpResponse )
    {
        if ( dhisHttpResponse == null || dhisHttpResponse.getResponse().isEmpty() )
        {
            log.warn( "Dhis http response is null" );
            return false;
        }

        if ( HttpStatus.valueOf( dhisHttpResponse.getStatusCode() ).is2xxSuccessful() )
        {
            return true;
        }

        if ( HttpStatus.valueOf( dhisHttpResponse.getStatusCode() ).is4xxClientError() )
        {
            StringBuilder clientErrorMessage = buildErrorMessage( "Client Error. ", dhisHttpResponse );
            log.warn( clientErrorMessage.toString() );
            throw new MetadataVersionServiceException( clientErrorMessage.toString() );
        }

        if ( HttpStatus.valueOf( dhisHttpResponse.getStatusCode() ).is5xxServerError() )
        {
            StringBuilder serverErrorMessage = buildErrorMessage( "Server Error. ", dhisHttpResponse );
            log.warn( serverErrorMessage.toString() );
            throw new MetadataVersionServiceException( serverErrorMessage.toString() );
        }

        return false;
    }

    private StringBuilder buildErrorMessage( String errorType, DhisHttpResponse dhisHttpResponse )
    {
        StringBuilder message = new StringBuilder();
        message.append( errorType ).append( "Http call failed with status code: " )
            .append( dhisHttpResponse.getStatusCode() ).append( " Caused by: " )
            .append( dhisHttpResponse.getResponse() );
        return message;
    }
}
