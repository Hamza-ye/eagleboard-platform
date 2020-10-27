package com.mass3d.fileresource;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import com.mass3d.common.DeleteNotAllowedException;
import com.mass3d.scheduling.AbstractJob;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.scheduling.JobType;
import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Deletes any orphaned FileResources. Queries for non-assigned or failed-upload
 * FileResources and deletes them from the database and/or file store.
 *
 */
@Slf4j
@Component( "fileResourceCleanUpJob" )
public class FileResourceCleanUpJob
    extends AbstractJob
{
    @Autowired
    private FileResourceService fileResourceService;

    @Autowired
    private SystemSettingManager systemSettingManager;

    @Autowired
    private FileResourceContentStore fileResourceContentStore;

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public JobType getJobType()
    {
        return JobType.FILE_RESOURCE_CLEANUP;
    }

    @Override
    public void execute( JobConfiguration jobConfiguration )
    {
        FileResourceRetentionStrategy retentionStrategy = (FileResourceRetentionStrategy) systemSettingManager
            .getSystemSetting( SettingKey.FILE_RESOURCE_RETENTION_STRATEGY );

        List<Pair<String, String>> deletedOrphans = new ArrayList<>();

        List<Pair<String, String>> deletedAuditFiles = new ArrayList<>();

        // Delete expired FRs
        if ( !FileResourceRetentionStrategy.FOREVER.equals( retentionStrategy ) )
        {
            List<FileResource> expired = fileResourceService.getExpiredFileResources( retentionStrategy );
            expired.forEach( this::safeDelete );
            expired.forEach( fr -> deletedAuditFiles.add( ImmutablePair.of( fr.getName(), fr.getUid() ) ) );
        }

        // Delete failed uploads
        fileResourceService.getOrphanedFileResources().stream()
            .filter( fr -> !isFileStored( fr ) )
            .filter( this::safeDelete )
            .forEach( fr -> deletedOrphans.add( ImmutablePair.of( fr.getName(), fr.getUid() ) ) );

        if ( !deletedOrphans.isEmpty() )
        {
            log.info( String.format( "Deleted %d orphaned FileResources: %s", deletedOrphans.size(),
                prettyPrint( deletedOrphans ) ) );
        }

        if ( !deletedAuditFiles.isEmpty() )
        {
            log.info( String.format( "Deleted %d expired FileResource audits: %s", deletedAuditFiles.size(),
                prettyPrint( deletedAuditFiles ) ) );
        }
    }

    private String prettyPrint( List<Pair<String, String>> list )
    {
        if ( list.isEmpty() )
        {
            return "";
        }

        StringBuilder sb = new StringBuilder( "[ " );

        list.forEach(
            pair -> sb.append( pair.getLeft() ).append( " , uid: " ).append( pair.getRight() ).append( ", " ) );

        sb.deleteCharAt( sb.lastIndexOf( "," ) ).append( "]" );

        return sb.toString();
    }

    private boolean isFileStored( FileResource fileResource )
    {
        return fileResourceContentStore.fileResourceContentExists( fileResource.getStorageKey() );
    }

    /**
     * Attempts to delete a fileresource. Fixes the isAssigned status if it turns out to be referenced by something else
     *
     * @param fileResource the fileresource to delete
     * @return true if the delete was successful
     */
    private boolean safeDelete( FileResource fileResource )
    {
        try
        {
            fileResourceService.deleteFileResource( fileResource );
            return true;
        }
        catch ( DeleteNotAllowedException e )
        {
            fileResource.setAssigned( true );
            fileResourceService.updateFileResource( fileResource );
        }

        return false;
    }

}
