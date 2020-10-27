package com.mass3d.fileresource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import javax.imageio.ImageIO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.core.io.ClassPathResource;

public class ImageProcessingServiceTest
{
    private static final int SMALL_IMAGE_WIDTH = 256;
    private static final int MEDIUM_IMAGE_WIDTH = 512;
    private static final int LARGE_IMAGE_WIDTH = 1024;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private ImageProcessingService subject;

    @Before
    public void setUp()
    {
        subject = new DefaultImageProcessingService();
    }

    @Test
    public void test_create_images_with_null_values()
    {
        Map<ImageFileDimension, File> images = subject.createImages( new FileResource(), null );

        assertTrue( images.isEmpty() );
    }

    @Test
    public void test_create_images_with_wrong_file_content_type() throws IOException
    {
        FileResource fileResource = new FileResource();
        fileResource.setName( "test" );
        fileResource.setContentType( "image/png" );

        File file = new File( "complex.pdf" );

        Map<ImageFileDimension, File> images = subject.createImages( fileResource, file );

        assertTrue( images.isEmpty() );

        Files.deleteIfExists( file.toPath() );
    }

    @Test
    public void test_create_image() throws IOException
    {
        FileResource fileResource = new FileResource();
        fileResource.setName( "test" );
        fileResource.setContentType( "image/png" );

        File file = new ClassPathResource( "images/dhis2.png" ).getFile();

        Map<ImageFileDimension, File> images = subject.createImages( fileResource, file );

        assertNotNull( images );
        assertEquals( 4, images.size() );

        File smallImage = images.get( ImageFileDimension.SMALL );
        File mediumImage = images.get( ImageFileDimension.MEDIUM );
        File largeImage = images.get( ImageFileDimension.LARGE );

        assertEquals( SMALL_IMAGE_WIDTH, ImageIO.read( smallImage ).getWidth() );

        assertEquals( MEDIUM_IMAGE_WIDTH, ImageIO.read( mediumImage ).getWidth() );

        assertEquals( LARGE_IMAGE_WIDTH, ImageIO.read( largeImage ).getWidth() );

        Files.deleteIfExists( smallImage.toPath() );
        Files.deleteIfExists( mediumImage.toPath() );
        Files.deleteIfExists( largeImage.toPath() );
    }
}
