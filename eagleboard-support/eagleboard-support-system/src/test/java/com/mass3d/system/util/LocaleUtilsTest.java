package com.mass3d.system.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;
import org.junit.Test;

public class LocaleUtilsTest
{
    @Test
    public void testGetLocaleFallbacks()
    {
        Locale l1 = new Locale( "en", "UK", "en" );
        Locale l2 = new Locale( "en", "UK" );
        Locale l3 = new Locale( "en" );
        
        List<String> locales = LocaleUtils.getLocaleFallbacks( l1 );
        
        assertEquals( 3, locales.size() );
        assertTrue( locales.contains( "en_UK_en" ) );
        assertTrue( locales.contains( "en_UK" ) );
        assertTrue( locales.contains( "en_UK" ) );
        
        assertEquals( 2, LocaleUtils.getLocaleFallbacks( l2 ).size() );
        assertEquals( 1, LocaleUtils.getLocaleFallbacks( l3 ).size() );        
    }
}
