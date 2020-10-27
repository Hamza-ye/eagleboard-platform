package com.mass3d.external.location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.DhisSpringTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @version $Id$
 */
@Ignore // Takes forever to run, enable to test modifications
@Slf4j
public class LocationManagerTest
    extends DhisSpringTest
{
    private InputStream in;
    
    private OutputStream out;
    
    private File file;

    @Autowired
    private LocationManager locationManager;
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    @Override
    public void setUpTest()
    {
        setExternalTestDir( locationManager );
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // InputStream
    // -------------------------------------------------------------------------

    @Test
    public void testGetInputStream()
    {
        try
        {
            in = locationManager.getInputStream( "test.properties" );
            
            assertNotNull( in );
        }
        catch ( LocationManagerException ex )
        {
            // External directory not set or the file does not exist
        }
        
        try
        {
            in = locationManager.getInputStream( "doesnotexist.properties" );
            
            fail();
        }
        catch ( Exception ex )
        {
            assertEquals( LocationManagerException.class, ex.getClass() );
        }
    }

    @Test
    public void testInputStreamWithDirs()
    {
        try
        {
            in = locationManager.getInputStream( "test.properties", "test", "dir" );

            assertNotNull( in );
        }
        catch ( LocationManagerException ex )
        {
            log.debug( "External directory not set or the file does not exist" );
        }
                
        try
        {
            in = locationManager.getInputStream( "doesnotexist.properties", "does", "not", "exist" );
            
            fail();
        }
        catch ( Exception ex )
        {
            assertEquals( LocationManagerException.class, ex.getClass() );
        }
    }

    // -------------------------------------------------------------------------
    // File for reading
    // -------------------------------------------------------------------------

    @Test
    public void testGetFileForReading()
    {
        try
        {
            file = locationManager.getFileForReading( "test.properties" );
            
            assertTrue( file.getAbsolutePath().length() > 0 );
            
            log.debug( "File for reading: " + file.getAbsolutePath() );
        }
        catch ( LocationManagerException ex )
        {
            log.debug( "External directory not set or the file does not exist" );
        }
        
        try
        {
            file = locationManager.getFileForReading( "doesnotexist.properties" );
            
            fail();
        }
        catch ( Exception ex )
        {
            assertEquals( LocationManagerException.class, ex.getClass() );
        }
    }

    @Test
    public void testGetFileForReadingWithDirs()
    {
        try
        {
            file = locationManager.getFileForReading( "test.properties", "test", "dir" );

            assertTrue( file.getAbsolutePath().length() > 0 );
            
            log.debug( "File for reading with dirs: " + file.getAbsolutePath() );
        }
        catch ( LocationManagerException ex )
        {
            log.debug( "External directory not set or the file does not exist" );
        }
                
        try
        {
            file = locationManager.getFileForReading( "doesnotexist.properties", "does", "not", "exist" );
            
            fail();
        }
        catch ( Exception ex )
        {
            assertEquals( LocationManagerException.class, ex.getClass() );
        }
    }
    
    public void testBuildDirectory()
    {
        try
        {
            File dir = locationManager.buildDirectory( "test", "dir" );
            
            log.debug( "Built directory: " + dir.getAbsolutePath() );
        }
        catch ( LocationManagerException ex )
        {
            log.debug( "External directory not set" );
        }
    }

    // -------------------------------------------------------------------------
    // OutputStream
    // -------------------------------------------------------------------------

    @Test
    public void testGetOutputStream()
    {
        try
        {
            out = locationManager.getOutputStream( "test.properties" );
            
            assertNotNull( out );
        }
        catch ( LocationManagerException ex )
        {
            log.debug( "External directory not set" );
        }
    }

    @Test
    public void testGetOutputStreamWithDirs()
    {
        try
        {
            out = locationManager.getOutputStream( "test.properties", "test" );
            
            assertNotNull( out );
        }
        catch ( LocationManagerException ex )
        {
            log.debug( "External directory not set" );
        }
    }

    // -------------------------------------------------------------------------
    // File for writing
    // -------------------------------------------------------------------------

    @Test
    public void testGetFileForWriting()
    {
        try
        {
            file = locationManager.getFileForWriting( "test.properties" );
            
            assertTrue( file.getAbsolutePath().length() > 0 );
            
            log.debug( "File for writing: " + file.getAbsolutePath() );
        }
        catch ( LocationManagerException ex )
        {
            log.debug( "External directory not set" );
        }
        
        try
        {
            file = locationManager.getFileForWriting( "doesnotexist.properties" );
            
            assertTrue( file.getAbsolutePath().length() > 0 );
            
            log.debug( "File for writing which does not exist: " + file.getAbsolutePath() );
        }
        catch ( LocationManagerException ex )
        {
            log.debug( "External directory not set" );
        }
    }

    @Test
    public void testGetFileForWritingWithDirs()
    {
        try
        {
            file = locationManager.getFileForWriting( "test.properties", "test" );
            
            assertTrue( file.getAbsolutePath().length() > 0 );
            
            log.debug( "File for writing with dirs: " + file.getAbsolutePath() );
        }
        catch ( LocationManagerException ex )
        {
            log.debug( "External directory not set" );
        }
        
        try
        {
            file = locationManager.getFileForWriting( "doesnotexist.properties", "does", "not", "exist" );
            
            assertTrue( file.getAbsolutePath().length() > 0 );
            
            log.debug( "File for writing with dirs which does not exist: " + file.getAbsolutePath() );
        }
        catch ( LocationManagerException ex )
        {
            log.debug( "External directory not set" );
        }
    }

    // -------------------------------------------------------------------------
    // External directory and environment variables
    // -------------------------------------------------------------------------

    @Test
    public void testGetExternalDirectory()
    {
        try
        {
            file = locationManager.getExternalDirectory();

            assertTrue( file.getAbsolutePath().length() > 0 );
            
            log.debug( "External directory: " + file.getAbsolutePath() );
        }
        catch ( LocationManagerException ex )
        {
            log.debug( "External directory not set" );
        }
    }

    @Test
    public void testExternalDirectorySet()
    {        
        boolean set = locationManager.externalDirectorySet();
        
        if ( set )
        {
            log.debug( "External directory set" );
        }
        else
        {
            log.debug( "External directory not set" );
        }
    }

    @Test
    public void testGetEnvironmentVariable()
    {
        String env = locationManager.getEnvironmentVariable();
        
        log.debug( "Environment variable: " + env );
    }
}
