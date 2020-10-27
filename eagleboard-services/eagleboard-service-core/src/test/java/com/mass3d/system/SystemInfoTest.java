package com.mass3d.system;

import com.mass3d.system.database.DatabaseInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link SystemInfo}.
 *
 */
public class SystemInfoTest
{
    private DatabaseInfo databaseInfo;

    private SystemInfo systemInfo;

    @Before
    public void setUp()
    {
        databaseInfo = new DatabaseInfo();
        systemInfo = new SystemInfo();

        systemInfo.setDatabaseInfo( databaseInfo );
    }

    @Test
    public void instance()
    {
        final SystemInfo si = systemInfo.instance();
        Assert.assertNotSame( systemInfo, si );
        Assert.assertNotSame( systemInfo.getDatabaseInfo(), si.getDatabaseInfo() );
    }
}