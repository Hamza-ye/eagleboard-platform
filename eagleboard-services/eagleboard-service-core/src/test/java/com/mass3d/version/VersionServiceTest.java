package com.mass3d.version;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import com.mass3d.DhisSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class VersionServiceTest
    extends DhisSpringTest
{
    @Autowired
    private VersionService versionService;

    private Version versionA;

    private Version versionB;

    @Override
    protected void setUpTest()
    {
        versionA = new Version();
        versionA.setKey( "keyA" );
        versionA.setValue( "valueA" );

        versionB = new Version();
        versionB.setKey( "keyB" );
        versionB.setValue( "valueB" );
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    @Test
    public void testAddVersion()
    {
        long idA = versionService.addVersion( versionA );
        long idB = versionService.addVersion( versionB );

        assertTrue( idA >= 0 );
        assertTrue( idB >= 0 );

        versionA = versionService.getVersion( idA );
        versionB = versionService.getVersion( idB );

        assertNotNull( versionA );
        assertNotNull( versionB );

        assertEquals( "valueA", versionA.getValue() );
        assertEquals( "valueB", versionB.getValue() );
    }

    @Test
    public void testUpdateVersion()
    {
        long id = versionService.addVersion( versionA );
        versionService.updateVersion( "keyA", "changedValueA" );
        versionA = versionService.getVersion( id );

        assertNotNull( versionA );
        assertEquals( "changedValueA", versionA.getValue() );
    }

    @Test
    public void testDeleteVersion()
    {
        long id = versionService.addVersion( versionA );
        versionService.deleteVersion( versionA );
        assertNull( versionService.getVersion( id ) );
    }

    @Test
    public void testGetVersion()
    {
        long id = versionService.addVersion( versionA );
        versionA = versionService.getVersion( id );

        assertNotNull( versionA );
        assertEquals( "valueA", versionA.getValue() );
    }

    @Test
    public void testGetVersionByKey()
    {
        versionService.addVersion( versionA );
        versionA = versionService.getVersionByKey( "keyA" );

        assertNotNull( versionA );
        assertEquals( "valueA", versionA.getValue() );
    }

    @Test
    public void testGetAllVersions()
    {
        versionService.addVersion( versionA );
        versionService.addVersion( versionB );

        List<Version> versions = versionService.getAllVersions();

        assertNotNull( versions );
        assertEquals( 2, versions.size() );
    }
}
