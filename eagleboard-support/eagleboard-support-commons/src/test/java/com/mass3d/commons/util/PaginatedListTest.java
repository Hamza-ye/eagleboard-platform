package com.mass3d.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.mass3d.commons.collection.PaginatedList;
import org.junit.Test;

public class PaginatedListTest
{
    @Test
    public void testNextPage()
    {
        PaginatedList<String> list = new PaginatedList<>( Arrays.asList( "A", "B", "C" ) ).setPageSize( 2 );
        
        List<String> page = list.nextPage();
        
        assertNotNull( page );
        assertEquals( 2, page.size() );
        assertTrue( page.contains( "A" ) );
        assertTrue( page.contains( "B" ) );
        
        page = list.nextPage();
        
        assertNotNull( page );
        assertEquals( 1, page.size() );
        assertTrue( page.contains( "C" ) );
        
        page = list.nextPage();
        
        assertNull( page );
    }
    
    @Test
    public void testGetPageEmpty()
    {
        PaginatedList<String> list = new PaginatedList<>( new ArrayList<String>() ).setPageSize( 2 );
        
        List<String> page = list.nextPage();
        
        assertNull( page );
    }
    
    @Test
    public void testPageCount()
    {
        PaginatedList<String> list = new PaginatedList<>( Arrays.asList( "A", "B", "C" ) ).setPageSize( 2 );
        
        assertEquals( 2, list.pageCount() );
        
        list = new PaginatedList<>( Arrays.asList( "A", "B", "C", "D" ) ).setPageSize( 2 );
        
        assertEquals( 2, list.pageCount() );

        list = new PaginatedList<>( Arrays.asList( "A", "B", "C", "D", "E" ) ).setPageSize( 2 );
        
        assertEquals( 3, list.pageCount() );
    }
    
    @Test
    public void testReset()
    {
        PaginatedList<String> list = new PaginatedList<>( Arrays.asList( "A", "B", "C" ) ).setPageSize( 2 );
        
        assertTrue( list.nextPage().contains( "A" ) );
        
        list.reset();

        assertTrue( list.nextPage().contains( "A" ) );        
    }
    
    @Test
    public void testSetNumberOfPages()
    {
        PaginatedList<String> list = new PaginatedList<>( Arrays.asList( "A", "B", "C", "D", "E" ) ).setNumberOfPages( 3 );
        
        assertEquals( 3, list.pageCount() );
        
        assertEquals( 2, list.nextPage().size() );
    }
    
    @Test
    public void testNextPageNumberOfPages()
    {
        PaginatedList<String> list = new PaginatedList<>( Arrays.asList( "A", "B", "C", "D", "E" ) ).setNumberOfPages( 2 );
        
        List<String> page = list.nextPage();
        
        assertNotNull( page );
        assertEquals( 3, page.size() );
        assertTrue( page.contains( "A" ) );
        assertTrue( page.contains( "B" ) );
        assertTrue( page.contains( "C" ) );
        
        page = list.nextPage();
        
        assertNotNull( page );
        assertEquals( 2, page.size() );
        assertTrue( page.contains( "D" ) );
        assertTrue( page.contains( "E" ) );
        
        page = list.nextPage();
        
        assertNull( page );
    }
    
    @Test
    public void testGetPages()
    {
        PaginatedList<String> list = new PaginatedList<>( Arrays.asList( "A", "B", "C", "D", "E" ) ).setPageSize( 2 );
        
        List<List<String>> pages = list.getPages();

        assertNotNull( pages );
        assertEquals( 3, pages.size() );
        
        List<String> page = pages.get( 0 );
        assertNotNull( page );
        assertEquals( 2, page.size() );
        assertTrue( page.contains( "A" ) );
        assertTrue( page.contains( "B" ) );
    }
}
