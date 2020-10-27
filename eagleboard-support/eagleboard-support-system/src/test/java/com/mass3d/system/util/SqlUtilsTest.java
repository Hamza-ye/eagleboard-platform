package com.mass3d.system.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SqlUtilsTest
{
    @Test
    public void testQuote()
    {
        assertEquals( "\"Some \"\"special\"\" value\"", SqlUtils.quote( "Some \"special\" value" ) );
        assertEquals( "\"Data element\"", SqlUtils.quote( "Data element" ) );
    }

    @Test
    public void testQuoteWithAlias()
    {
        assertEquals( "ougs.\"Short name\"", SqlUtils.quote( "ougs", "Short name" ) );
        assertEquals( "ous.\"uid\"", SqlUtils.quote( "ous", "uid" ) );
    }
}
