package com.mass3d.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.mass3d.commons.collection.ListUtils;
import org.junit.Test;

public class ListUtilsTest
{
    @Test
    public void testRemoveAll()
    {
        List<String> list = new ArrayList<>( Arrays.asList( "a", "b", "c", "d", "e", "f", "g", "h" ) );
        
        Integer[] indexes = { 0, 2, 5, 7, -1, 78 };

        assertEquals( 8, list.size() );
        
        ListUtils.removeAll( list, indexes );
        
        assertEquals( 4, list.size() );
        assertTrue( list.contains( "b" ) );
        assertTrue( list.contains( "d" ) );
        assertTrue( list.contains( "e" ) );
        assertTrue( list.contains( "g" ) );
    }
    
    @Test
    public void testGetDuplicates()
    {
        List<String> list = new ArrayList<>( Arrays
            .asList( "a", "b", "c", "c", "d", "e", "e", "e", "f" ) );
        Set<String> expected = new HashSet<>( Arrays.asList( "c", "e" ) );
        assertEquals( expected, ListUtils.getDuplicates( list ) );
        
        list = new ArrayList<>( Arrays.asList( "a", "b", "c", "d", "e", "f", "g", "h" ) );
        assertEquals( 0, ListUtils.getDuplicates( list ).size() );
    }
}
