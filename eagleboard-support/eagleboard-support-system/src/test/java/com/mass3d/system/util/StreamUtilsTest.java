package com.mass3d.system.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import com.mass3d.commons.util.StreamUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StreamUtilsTest
{
    private BufferedInputStream zipStream;

    private BufferedInputStream gzipStream;

    private BufferedInputStream plainStream;

    @Before
    public void setUp()
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        zipStream = new BufferedInputStream( classLoader.getResourceAsStream("dxfA.zip") );

        gzipStream = new BufferedInputStream( classLoader.getResourceAsStream("Export.xml.gz") );
        
        plainStream = new BufferedInputStream( classLoader.getResourceAsStream("Export.xml") );
    }

    @After
    public void tearDown()
        throws Exception
    {
        zipStream.close();

        gzipStream.close();
        
        plainStream.close();
    }

    @Test
    public void testIsZip()
    {
        assertTrue( StreamUtils.isZip( zipStream ) );

        assertFalse( StreamUtils.isGZip( zipStream ) );
        
        assertFalse( StreamUtils.isZip( plainStream ) );
    }

    @Test
    public void testIsGZip()
    {
        assertTrue( StreamUtils.isGZip( gzipStream ) );

        assertFalse( StreamUtils.isZip( gzipStream ) );
        
        assertFalse( StreamUtils.isGZip( plainStream ) );
    }
    
    @Test
    public void testWrapAndCheckZip()
        throws Exception
    {
        Reader reader = new InputStreamReader( StreamUtils.wrapAndCheckCompressionFormat( zipStream ) );
        
        assertEquals( '<', reader.read() );
        assertEquals( '?', reader.read() );
        assertEquals( 'x', reader.read() );
        assertEquals( 'm', reader.read() );
        assertEquals( 'l', reader.read() );
    }
}
