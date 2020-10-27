package com.mass3d.system.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CodeUtilsTest
{
    @Test
    public void testFilenameEncode()
    {
        assertEquals( "nicechart", CodecUtils.filenameEncode( "nicechart" ) );
    }

    @Test
    public void test()
    {
        assertEquals( "Basic am9objpkb2UxMjM=", CodecUtils.getBasicAuthString( "john", "doe123" ) );
        assertEquals( "Basic YWRtaW46ZGlzdHJpY3Q=", CodecUtils.getBasicAuthString( "admin", "district" ) );
    }
}
