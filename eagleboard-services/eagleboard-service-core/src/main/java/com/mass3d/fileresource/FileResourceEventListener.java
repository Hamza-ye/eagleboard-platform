package com.mass3d.fileresource;

import java.io.File;
import java.util.Map;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.fileresource.events.BinaryFileSavedEvent;
import com.mass3d.fileresource.events.FileDeletedEvent;
import com.mass3d.fileresource.events.FileSavedEvent;
import com.mass3d.fileresource.events.ImageFileSavedEvent;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component( "com.mass3d.fileresource.FileResourceEventListener" )
public class FileResourceEventListener
{
    private final FileResourceService fileResourceService;

    private final FileResourceContentStore fileResourceContentStore;

    public FileResourceEventListener(FileResourceService fileResourceService, FileResourceContentStore contentStore )
    {
        this.fileResourceService = fileResourceService;
        this.fileResourceContentStore = contentStore;
    }

    @TransactionalEventListener
    @Async
    public void save( FileSavedEvent fileSavedEvent )
    {
        DateTime startTime = DateTime.now();

        File file = fileSavedEvent.getFile();

        FileResource fileResource = fileResourceService.getFileResource( fileSavedEvent.getFileResource() );

        String storageId = fileResourceContentStore.saveFileResourceContent( fileResource, file );

        Period timeDiff = new Period( startTime, DateTime.now() );

        logMessage( storageId, fileResource, timeDiff );
    }

    @TransactionalEventListener
    @Async
    public void saveImageFile( ImageFileSavedEvent imageFileSavedEvent )
    {
        DateTime startTime = DateTime.now();

        Map<ImageFileDimension, File> imageFiles = imageFileSavedEvent.getImageFiles();

        FileResource fileResource = fileResourceService.getFileResource( imageFileSavedEvent.getFileResource() );

        String storageId = fileResourceContentStore.saveFileResourceContent( fileResource, imageFiles );

        if ( storageId != null )
        {
            fileResource.setHasMultipleStorageFiles( true );

            fileResourceService.updateFileResource( fileResource );
        }

        Period timeDiff = new Period( startTime, DateTime.now() );

        logMessage( storageId, fileResource, timeDiff );
    }

    @TransactionalEventListener
    @Async
    public void saveBinaryFile( BinaryFileSavedEvent binaryFileSavedEvent )
    {
        DateTime startTime = DateTime.now();

        byte[] bytes = binaryFileSavedEvent.getBytes();

        FileResource fileResource = fileResourceService.getFileResource( binaryFileSavedEvent.getFileResource() );

        String storageId = fileResourceContentStore.saveFileResourceContent( fileResource, bytes );

        Period timeDiff = new Period( startTime, DateTime.now() );

        logMessage( storageId, fileResource, timeDiff );
    }

    @TransactionalEventListener
    @Async
    public void deleteFile( FileDeletedEvent deleteFileEvent )
    {
        if ( !fileResourceContentStore.fileResourceContentExists( deleteFileEvent.getStorageKey() ) )
        {
            log.error( String.format( "No file exist for key: %s", deleteFileEvent.getStorageKey() ) );
            return;
        }

        if ( FileResource.IMAGE_CONTENT_TYPES.contains( deleteFileEvent.getContentType() ) && FileResourceDomain.getDomainForMultipleImages().contains( deleteFileEvent.getDomain() ) )
        {
            String storageKey = deleteFileEvent.getStorageKey();

            Stream.of( ImageFileDimension.values() ).forEach(d -> fileResourceContentStore.deleteFileResourceContent( StringUtils
                .join( storageKey, d.getDimension() ) ) );
        }
        else
        {
            fileResourceContentStore.deleteFileResourceContent( deleteFileEvent.getStorageKey() );
        }
    }

    private void logMessage( String storageId, FileResource fileResource, Period timeDiff )
    {
        if ( storageId == null )
        {
            log.error( String.format( "Saving content for file resource failed: %s", fileResource.getUid() ) );
            return;
        }

        log.info( String
            .format( "File stored with key: %s'. Upload finished in %s", storageId, timeDiff.toString( PeriodFormat
                .getDefault() ) ) );
    }
}
