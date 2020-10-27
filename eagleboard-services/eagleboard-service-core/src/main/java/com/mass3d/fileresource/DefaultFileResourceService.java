package com.mass3d.fileresource;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.hibernate.SessionFactory;
import com.mass3d.common.IllegalQueryException;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.fileresource.events.BinaryFileSavedEvent;
import com.mass3d.fileresource.events.FileDeletedEvent;
import com.mass3d.fileresource.events.FileSavedEvent;
import com.mass3d.fileresource.events.ImageFileSavedEvent;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.fileresource.FileResourceService" )
public class DefaultFileResourceService
    implements FileResourceService
{
    private static final Duration IS_ORPHAN_TIME_DELTA = Hours.TWO.toStandardDuration();

    public static final Predicate<FileResource> IS_ORPHAN_PREDICATE = (fr -> !fr.isAssigned());

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final FileResourceStore fileResourceStore;

    private final SessionFactory sessionFactory;

    private final FileResourceContentStore fileResourceContentStore;

    private final ImageProcessingService imageProcessingService;

    private final ApplicationEventPublisher fileEventPublisher;

    public DefaultFileResourceService( FileResourceStore fileResourceStore, SessionFactory sessionFactory,
        FileResourceContentStore fileResourceContentStore, ImageProcessingService imageProcessingService,
        ApplicationEventPublisher fileEventPublisher )
    {
        checkNotNull( fileResourceStore );
        checkNotNull( sessionFactory );
        checkNotNull( fileResourceContentStore );
        checkNotNull( imageProcessingService );
        checkNotNull( fileEventPublisher );

        this.fileResourceStore = fileResourceStore;
        this.sessionFactory = sessionFactory;
        this.fileResourceContentStore = fileResourceContentStore;
        this.imageProcessingService = imageProcessingService;
        this.fileEventPublisher = fileEventPublisher;
    }

    // -------------------------------------------------------------------------
    // FileResourceService implementation
    // -------------------------------------------------------------------------

    @Override
    @Transactional( readOnly = true )
    public FileResource getFileResource( String uid )
    {
        return checkStorageStatus( fileResourceStore.getByUid( uid ) );
    }

    @Override
    @Transactional( readOnly = true )
    public List<FileResource> getFileResources( List<String> uids )
    {
        return fileResourceStore.getByUid( uids ).stream()
            .map( this::checkStorageStatus )
            .collect( Collectors.toList() );
    }

    @Override
    @Transactional( readOnly = true )
    public List<FileResource> getOrphanedFileResources()
    {
        return fileResourceStore.getAllLeCreated( new DateTime().minus( IS_ORPHAN_TIME_DELTA ).toDate() ).stream()
            .filter( IS_ORPHAN_PREDICATE )
            .collect( Collectors.toList() );
    }

    @Override
    @Transactional
    public void saveFileResource( FileResource fileResource, File file )
    {
        validateFileResource( fileResource );

        fileResource.setStorageStatus( FileResourceStorageStatus.PENDING );
        fileResourceStore.save( fileResource );
        sessionFactory.getCurrentSession().flush();

        if ( FileResource.IMAGE_CONTENT_TYPES.contains( fileResource.getContentType() )
            && FileResourceDomain.getDomainForMultipleImages().contains( fileResource.getDomain() ) )
        {
            Map<ImageFileDimension, File> imageFiles = imageProcessingService.createImages( fileResource, file );

            fileEventPublisher.publishEvent( new ImageFileSavedEvent( fileResource.getUid(), imageFiles ) );
            return;
        }

        fileEventPublisher.publishEvent( new FileSavedEvent( fileResource.getUid(), file ) );
    }

    @Override
    @Transactional
    public String saveFileResource( FileResource fileResource, byte[] bytes )
    {
        fileResource.setStorageStatus( FileResourceStorageStatus.PENDING );
        fileResourceStore.save( fileResource );
        sessionFactory.getCurrentSession().flush();

        final String uid = fileResource.getUid();

        fileEventPublisher.publishEvent( new BinaryFileSavedEvent( fileResource.getUid(), bytes ) );

        return uid;
    }

    @Override
    @Transactional
    public void deleteFileResource( String uid )
    {
        if ( uid == null )
        {
            return;
        }

        FileResource fileResource = fileResourceStore.getByUid( uid );

        deleteFileResource( fileResource );
    }

    @Override
    @Transactional
    public void deleteFileResource( FileResource fileResource )
    {
        if ( fileResource == null || fileResourceStore.get( fileResource.getId() ) == null )
        {
            return;
        }

        FileDeletedEvent deleteFileEvent = new FileDeletedEvent( fileResource.getStorageKey(),
            fileResource.getContentType(), fileResource.getDomain() );

        fileResourceStore.delete( fileResource );

        fileEventPublisher.publishEvent( deleteFileEvent );
    }

    @Override
    @Transactional( readOnly = true )
    public InputStream getFileResourceContent( FileResource fileResource )
    {
        return fileResourceContentStore.getFileResourceContent( fileResource.getStorageKey() );
    }

    @Override
    @Transactional( readOnly = true )
    public long getFileResourceContentLength( FileResource fileResource )
    {
        return fileResourceContentStore.getFileResourceContentLength( fileResource.getStorageKey() );
    }

    @Override
    @Transactional( readOnly = true )
    public void copyFileResourceContent( FileResource fileResource, OutputStream outputStream )
        throws IOException, NoSuchElementException
    {
        fileResourceContentStore.copyContent( fileResource.getStorageKey(), outputStream );
    }

    @Override
    @Transactional
    public boolean fileResourceExists( String uid )
    {
        return fileResourceStore.getByUid( uid ) != null;
    }

    @Override
    @Transactional
    public void updateFileResource( FileResource fileResource )
    {
        fileResourceStore.update( fileResource );
    }

    @Override
    @Transactional( readOnly = true )
    public URI getSignedGetFileResourceContentUri( String uid )
    {
        FileResource fileResource = getFileResource( uid );

        if ( fileResource == null )
        {
            return null;
        }

        return fileResourceContentStore.getSignedGetContentUri( fileResource.getStorageKey() );
    }

    @Override
    @Transactional( readOnly = true )
    public URI getSignedGetFileResourceContentUri( FileResource fileResource )
    {
        if ( fileResource == null )
        {
            return null;
        }

        return fileResourceContentStore.getSignedGetContentUri( fileResource.getStorageKey() );
    }

    @Override
    @Transactional( readOnly = true )
    public List<FileResource> getExpiredFileResources( FileResourceRetentionStrategy retentionStrategy )
    {
        DateTime expires = DateTime.now().minus( retentionStrategy.getRetentionTime() );
        return fileResourceStore.getExpiredFileResources( expires );
    }

    @Override
    @Transactional( readOnly = true )
    public List<FileResource> getAllUnProcessedImagesFiles()
    {
        return fileResourceStore.getAllUnProcessedImages();
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Validates the given {@link FileResource}. Throws an exception if not.
     *
     * @param fileResource the file resource.
     * @throws IllegalQueryException if the given file resource is invalid.
     */
    private void validateFileResource( FileResource fileResource )
        throws IllegalQueryException
    {
        if ( fileResource.getName() == null )
        {
            throw new IllegalQueryException( ErrorCode.E6100 );
        }

        if ( !FileResourceBlocklist.isValid( fileResource ) )
        {
            throw new IllegalQueryException( ErrorCode.E6101 );
        }
    }

    private FileResource checkStorageStatus( FileResource fileResource )
    {
        if ( fileResource != null )
        {
            boolean exists = fileResourceContentStore.fileResourceContentExists( fileResource.getStorageKey() );

            if ( exists )
            {
                fileResource.setStorageStatus( FileResourceStorageStatus.STORED );
            }
            else
            {
                fileResource.setStorageStatus( FileResourceStorageStatus.PENDING );
            }
        }

        return fileResource;
    }
}
