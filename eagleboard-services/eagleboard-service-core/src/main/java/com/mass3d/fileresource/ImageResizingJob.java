package com.mass3d.fileresource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.commons.util.DebugUtils;
import com.mass3d.scheduling.AbstractJob;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.scheduling.JobType;
import org.springframework.stereotype.Component;

/**
 * Job will fetch all the image FileResources with flag hasMultiple set to false. It will process those image FileResources create three images files for each of them.
 * Once created, images will be stored at EWS and flag hasMultiple is set to true.
 *
 */
@Slf4j
@Component( "imageResizingJob" )
public class ImageResizingJob extends AbstractJob
{
    private final FileResourceContentStore fileResourceContentStore;

    private final FileResourceService fileResourceService;

    private final ImageProcessingService imageProcessingService;

    public ImageResizingJob(FileResourceContentStore fileResourceContentStore, FileResourceService fileResourceService,
        ImageProcessingService imageProcessingService )
    {
        this.fileResourceContentStore = fileResourceContentStore;
        this.fileResourceService = fileResourceService;
        this.imageProcessingService = imageProcessingService;
    }

    @Override
    public JobType getJobType()
    {
        return JobType.IMAGE_PROCESSING;
    }

    @Override
    public void execute( JobConfiguration jobConfiguration )
    {
        List<FileResource> fileResources = fileResourceService.getAllUnProcessedImagesFiles();

        File tmpFile = null;

        String storageKey;

        int count = 0;

        for ( FileResource fileResource : fileResources )
        {
            String key = fileResource.getStorageKey();

            tmpFile = new File( UUID.randomUUID().toString() );

	    if ( !fileResourceContentStore.fileResourceContentExists( key ) )
            {
                log.error( "The referenced file could not be found for FileResource: " + fileResource.getUid() );
                continue;
            }

            try ( FileOutputStream fileOutputStream = new FileOutputStream( tmpFile ) )
            {

                fileResourceContentStore.copyContent( key, fileOutputStream );

                Map<ImageFileDimension, File> imageFiles = imageProcessingService.createImages( fileResource, tmpFile );

                storageKey = fileResourceContentStore.saveFileResourceContent( fileResource, imageFiles );

                if ( storageKey != null )
                {
                    fileResource.setHasMultipleStorageFiles( true );
                    fileResourceService.updateFileResource( fileResource );
                    count++;
                }
                else
                {
                    log.error( "File upload failed" );
                }
            }
            catch ( Exception e )
            {
                DebugUtils.getStackTrace( e );
            }
            finally
            {
                try
                {
                    if ( tmpFile != null )
                    {
                        Files.deleteIfExists( tmpFile.toPath() );
                    }
                }
                catch ( IOException ioe )
                {
                    log.warn( String.format( "Temporary file '%s' could not be deleted.", tmpFile.toPath() ), ioe );
                }
            }
        }

        log.info( String.format( "Number of FileResources processed: %d", count ) );
    }
}
