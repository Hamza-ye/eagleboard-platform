package com.mass3d.system.database;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link DatabaseInfo}.
 *
 */
public class DatabaseInfoTest
{
    private DatabaseInfo databaseInfo;

    @Before
    public void setUp()
    {
        databaseInfo = new DatabaseInfo();
        databaseInfo.setName( "testDatabase" );
        databaseInfo.setUser( "testUser" );
        databaseInfo.setUrl( "theUrl" );
        databaseInfo.setPassword( "myPassword" );
        databaseInfo.setDatabaseVersion( "xzy 10.7" );
        databaseInfo.setSpatialSupport( true );
    }

    @Test
    public void cloneDatabaseInfo()
    {
        final DatabaseInfo cloned = databaseInfo.instance();
        Assert.assertNotSame( databaseInfo, cloned );
        Assert.assertEquals( databaseInfo.getName(), cloned.getName() );
        Assert.assertEquals( databaseInfo.getUser(), cloned.getUser() );
        Assert.assertEquals( databaseInfo.getUrl(), cloned.getUrl() );
        Assert.assertEquals( databaseInfo.getPassword(), cloned.getPassword() );
        Assert.assertEquals( databaseInfo.getDatabaseVersion(), cloned.getDatabaseVersion() );
        Assert.assertEquals( databaseInfo.isSpatialSupport(), cloned.isSpatialSupport() );
    }

    @Test
    public void clearSensitiveInfo()
    {
        databaseInfo.clearSensitiveInfo();
        Assert.assertNull( databaseInfo.getName() );
        Assert.assertNull( databaseInfo.getUser() );
        Assert.assertNull( databaseInfo.getUrl() );
        Assert.assertNull( databaseInfo.getPassword() );
        Assert.assertNull( databaseInfo.getDatabaseVersion() );
        Assert.assertTrue( databaseInfo.isSpatialSupport() );
    }
}
