package com.mass3d.security.acl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AccessStringHelperTest
{
    @Test
    public void testCanRead()
    {
        String access = "r-------";
        String access_will_fail = "--------";

        assertTrue( AccessStringHelper.canRead( access ) );
        assertFalse( AccessStringHelper.canRead( access_will_fail ) );
    }

    @Test
    public void testCanWrite()
    {
        String access1 = "rw------";
        String access2 = "-w------";
        String access_will_fail = "--------";

        assertTrue( AccessStringHelper.canWrite( access1 ) );
        assertTrue( AccessStringHelper.canWrite( access2 ) );
        assertFalse( AccessStringHelper.canWrite( access_will_fail ) );
    }

    @Test
    public void staticRead()
    {
        assertTrue( AccessStringHelper.canRead( AccessStringHelper.READ ) );
        assertFalse( AccessStringHelper.canWrite( AccessStringHelper.READ ) );
    }

    @Test
    public void staticWrite()
    {
        assertFalse( AccessStringHelper.canRead( AccessStringHelper.WRITE ) );
        assertTrue( AccessStringHelper.canWrite( AccessStringHelper.WRITE ) );
    }

    @Test
    public void staticReadWrite()
    {
        assertTrue( AccessStringHelper.canRead( AccessStringHelper.READ_WRITE ) );
        assertTrue( AccessStringHelper.canWrite( AccessStringHelper.READ_WRITE ) );
    }
}
