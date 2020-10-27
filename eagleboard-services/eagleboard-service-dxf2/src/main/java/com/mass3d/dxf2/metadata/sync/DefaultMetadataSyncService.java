package com.mass3d.dxf2.metadata.sync;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import com.mass3d.dxf2.metadata.AtomicMode;
import com.mass3d.dxf2.metadata.MetadataImportParams;
import com.mass3d.dxf2.metadata.sync.exception.DhisVersionMismatchException;
import com.mass3d.dxf2.metadata.sync.exception.MetadataSyncServiceException;
import com.mass3d.dxf2.metadata.sync.exception.RemoteServerUnavailableException;
import com.mass3d.dxf2.metadata.version.MetadataVersionDelegate;
import com.mass3d.dxf2.metadata.version.exception.MetadataVersionServiceException;
import com.mass3d.metadata.version.MetadataVersion;
import com.mass3d.metadata.version.MetadataVersionService;
import com.mass3d.metadata.version.VersionType;
import org.springframework.stereotype.Service;

/**
 * Performs the meta data sync related tasks in service layer.
 *
 */
@Slf4j
@Service( "com.mass3d.dxf2.metadata.sync.MetadataSyncService" )
public class DefaultMetadataSyncService
    implements MetadataSyncService
{
    private MetadataVersionDelegate metadataVersionDelegate;

    private MetadataVersionService metadataVersionService;

    private MetadataSyncDelegate metadataSyncDelegate;

    private MetadataSyncImportHandler metadataSyncImportHandler;

    public DefaultMetadataSyncService( MetadataVersionDelegate metadataVersionDelegate,
        MetadataVersionService metadataVersionService, MetadataSyncDelegate metadataSyncDelegate,
        MetadataSyncImportHandler metadataSyncImportHandler )
    {
        checkNotNull(metadataVersionDelegate);
        checkNotNull(metadataVersionService);
        checkNotNull(metadataSyncDelegate);
        checkNotNull(metadataSyncImportHandler);

        this.metadataVersionDelegate = metadataVersionDelegate;
        this.metadataVersionService = metadataVersionService;
        this.metadataSyncDelegate = metadataSyncDelegate;
        this.metadataSyncImportHandler = metadataSyncImportHandler;
    }

    @Override
    public MetadataSyncParams getParamsFromMap( Map<String, List<String>> parameters )
    {
        List<String> versionName = getVersionsFromParams( parameters );
        MetadataImportParams importParams = new MetadataImportParams();
        importParams.setMetadataSyncImport( true );
        MetadataSyncParams syncParams = new MetadataSyncParams();
        syncParams.setImportParams( importParams );
        String versionNameStr = versionName.get( 0 );

        if ( StringUtils.isNotEmpty( versionNameStr ) )
        {
            MetadataVersion version;

            try
            {
                version = metadataVersionDelegate.getRemoteMetadataVersion( versionNameStr );
            }
            catch ( MetadataVersionServiceException e )
            {
                throw new MetadataSyncServiceException( e.getMessage(), e );
            }

            if ( version == null )
            {
                throw new MetadataSyncServiceException(
                    "The MetadataVersion could not be fetched from the remote server for the versionName: " +
                        versionNameStr );
            }

            syncParams.setVersion( version );
        }

        syncParams.setParameters( parameters );

        return syncParams;
    }

    @Override
    public synchronized MetadataSyncSummary doMetadataSync( MetadataSyncParams syncParams )
        throws MetadataSyncServiceException, DhisVersionMismatchException
    {
        MetadataVersion version = getMetadataVersion( syncParams );

        setMetadataImportMode( syncParams, version );
        String metadataVersionSnapshot = getMetadataVersionSnapshot( version );

        if ( metadataSyncDelegate.shouldStopSync( metadataVersionSnapshot ) )
        {
            throw new DhisVersionMismatchException( "Metadata sync failed because your version of DHIS does not match the master version" );
        }

        saveMetadataVersionSnapshotLocally( version, metadataVersionSnapshot );
        MetadataSyncSummary metadataSyncSummary = metadataSyncImportHandler.importMetadata( syncParams, metadataVersionSnapshot );

        log.info( "Metadata Sync Summary: " + metadataSyncSummary );

        return metadataSyncSummary;
    }

    @Override
    public boolean isSyncRequired ( MetadataSyncParams syncParams )
    {
        MetadataVersion version = getMetadataVersion( syncParams );
        return ( metadataVersionService.getVersionByName( version.getName() ) == null );
    }

    private void saveMetadataVersionSnapshotLocally( MetadataVersion version, String metadataVersionSnapshot )
    {
        if ( getLocalVersionSnapshot( version ) == null )
        {
            metadataVersionService.createMetadataVersionInDataStore( version.getName(), metadataVersionSnapshot );
            log.info( "Downloaded the metadata snapshot from remote and saved in Data Store for the version: " + version );
        }
    }

    private String getMetadataVersionSnapshot( MetadataVersion version )
    {
        String metadataVersionSnapshot = getLocalVersionSnapshot( version );

        if ( metadataVersionSnapshot != null )
        {
            return metadataVersionSnapshot;
        }

        metadataVersionSnapshot = getMetadataVersionSnapshotFromRemote( version );

        if ( !(metadataVersionService.isMetadataPassingIntegrity( version, metadataVersionSnapshot ) ) )
        {
            throw new MetadataSyncServiceException( "Metadata snapshot is corrupted. Not saving it locally" );
        }

        return metadataVersionSnapshot;
    }

    private String getMetadataVersionSnapshotFromRemote( MetadataVersion version )
    {
        String metadataVersionSnapshot;

        try
        {
            metadataVersionSnapshot = metadataVersionDelegate.downloadMetadataVersionSnapshot( version );
        }
        catch ( MetadataVersionServiceException | RemoteServerUnavailableException e )
        {
            throw new MetadataSyncServiceException( e.getMessage(), e );
        }

        if ( metadataVersionSnapshot == null )
        {
            throw new MetadataSyncServiceException( "Metadata snapshot can't be null." );
        }

        return metadataVersionSnapshot;
    }

    private void setMetadataImportMode( MetadataSyncParams syncParams, MetadataVersion version )
    {
        if ( VersionType.BEST_EFFORT.equals( version.getType() ) )
        {
            syncParams.getImportParams().setAtomicMode( AtomicMode.NONE );
        }
    }

    //----------------------------------------------------------------------------------------
    // Private Methods
    //----------------------------------------------------------------------------------------

    private String getLocalVersionSnapshot( MetadataVersion version )
    {
        String metadataVersionSnapshot = metadataVersionService.getVersionData( version.getName() );

        if ( StringUtils.isNotEmpty( metadataVersionSnapshot ) )
        {
            log.info( "Rendering the MetadataVersion from local DataStore" );
            return metadataVersionSnapshot;
        }

        return null;
    }

    private List<String> getVersionsFromParams( Map<String, List<String>> parameters )
    {
        if ( parameters == null )
        {
            throw new MetadataSyncServiceException( "Missing required parameter: 'versionName'" );
        }

        List<String> versionName = parameters.get( "versionName" );

        if ( versionName == null || versionName.size() == 0 )
        {
            throw new MetadataSyncServiceException( "Missing required parameter: 'versionName'" );
        }

        return versionName;
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
            throw new MetadataSyncServiceException(
                "MetadataVersion for the Sync cant be null. The ClassListMap could not be constructed." );
        }

        return version;
    }
}
