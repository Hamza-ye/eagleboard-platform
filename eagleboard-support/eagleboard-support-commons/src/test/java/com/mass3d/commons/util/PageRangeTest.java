package com.mass3d.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Test;

public class PageRangeTest
{
    @Test
    public void testPageSize()
    {
        PageRange range = new PageRange( 12 ).setPageSize( 5 );
        
        assertTrue( range.nextPage() );        
        assertEquals( 0, range.getFromIndex() );
        assertEquals( 5, range.getToIndex() );

        assertTrue( range.nextPage() );        
        assertEquals( 5, range.getFromIndex() );
        assertEquals( 10, range.getToIndex() );
        
        assertTrue( range.nextPage() );        
        assertEquals( 10, range.getFromIndex() );
        assertEquals( 12, range.getToIndex() );
        
        assertFalse( range.nextPage() );
    }

    @Test
    public void testPages()
    {
        PageRange range = new PageRange( 11 ).setPages( 3 );
        
        assertTrue( range.nextPage() );        
        assertEquals( 0, range.getFromIndex() );
        assertEquals( 4, range.getToIndex() );

        assertTrue( range.nextPage() );        
        assertEquals( 4, range.getFromIndex() );
        assertEquals( 8, range.getToIndex() );
        
        assertTrue( range.nextPage() );        
        assertEquals( 8, range.getFromIndex() );
        assertEquals( 11, range.getToIndex() );
        
        assertFalse( range.nextPage() );
    }
    
    @Test
    public void testGetPages()
    {
        PageRange range = new PageRange( 12 ).setPageSize( 5 );
        
        List<int[]> pages = range.getPages();
        
        assertEquals( 3, pages.size() );
        assertEquals( 0, pages.get( 0 )[0] );
        assertEquals( 5, pages.get( 0 )[1] );
        assertEquals( 5, pages.get( 1 )[0] );
        assertEquals( 10, pages.get( 1 )[1] );
        assertEquals( 10, pages.get( 2 )[0] );
        assertEquals( 12, pages.get( 2 )[1] );
    }
}
