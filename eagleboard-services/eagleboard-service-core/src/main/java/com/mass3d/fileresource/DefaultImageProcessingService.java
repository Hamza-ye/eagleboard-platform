package com.mass3d.fileresource;

import com.google.common.collect.ImmutableMap;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.commons.util.DebugUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

@Slf4j
@Service( "com.mass3d.fileresource.ImageProcessingService" )
public class DefaultImageProcessingService implements ImageProcessingService
{
    private static final ImmutableMap<ImageFileDimension, ImageSize> IMAGE_FILE_SIZES = ImmutableMap
        .of(
        ImageFileDimension.SMALL, new ImageSize( 256, 256 ),
        ImageFileDimension.MEDIUM, new ImageSize( 512, 512 ),
        ImageFileDimension.LARGE, new ImageSize( 1024, 1024 ) );

    @Override
    public Map<ImageFileDimension, File> createImages( FileResource fileResource, File file )
    {
        if ( !isInputValid( fileResource, file ) )
        {
            return new HashMap<>();
        }

        Map<ImageFileDimension, File> images = new HashMap<>();

        try
        {
            BufferedImage image = ImageIO.read( file );

            for ( ImageFileDimension dimension : ImageFileDimension.values() )
            {
                if ( ImageFileDimension.ORIGINAL == dimension )
                {
                    images.put( dimension, file );
                    continue;
                }

                ImageSize size = IMAGE_FILE_SIZES.get( dimension );

                BufferedImage resizedImage = resize( image, size );

                File tempFile = new File( file.getPath() + dimension.getDimension() );

                ImageIO.write( resizedImage, fileResource.getFormat(), tempFile );

                images.put( dimension, tempFile );
            }
        }
        catch ( IOException e )
        {
            log.error( "Image file resource cannot be processed" );
            DebugUtils.getStackTrace( e );
            return new HashMap<>();
        }

        return images;
    }

    private BufferedImage resize( BufferedImage image, ImageSize dimensions )
    {
        return Scalr.resize( image, Scalr.Method.BALANCED, Scalr.Mode.FIT_TO_WIDTH, dimensions.width, dimensions.height );
    }

    private boolean isInputValid( FileResource fileResource, File file )
    {
        if ( fileResource == null || file == null )
        {
            log.error( "FileResource and associated File must not be null" );
            return false;
        }

        if ( file.exists() )
        {
            try ( InputStream is = new BufferedInputStream( new FileInputStream( file ) ))
            {
                String mimeType = URLConnection.guessContentTypeFromStream( is );
                return FileResource.IMAGE_CONTENT_TYPES.contains( mimeType );
            }
            catch ( IOException e )
            {
                DebugUtils.getStackTrace( e );
                log.error( "Error while getting content type", e );
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    private static class ImageSize
    {
        int width;
        int height;

        ImageSize( int width, int height )
        {
            this.width = width;
            this.height = height;
        }
    }
}
