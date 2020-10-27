package com.mass3d.dxf2.metadata.version;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.time.DateUtils;
import com.mass3d.DhisSpringTest;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dxf2.metadata.systemsettings.MetadataSystemSettingService;
import com.mass3d.dxf2.metadata.version.exception.MetadataVersionServiceException;
import com.mass3d.keyjsonvalue.KeyJsonValue;
import com.mass3d.keyjsonvalue.MetadataKeyJsonService;
import com.mass3d.metadata.version.MetadataVersion;
import com.mass3d.metadata.version.MetadataVersionService;
import com.mass3d.metadata.version.VersionType;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultMetadataVersionServiceTest
    extends DhisSpringTest
{
    @Autowired
    private MetadataVersionService versionService;

    @Autowired
    private MetadataKeyJsonService metaDataKeyJsonService;

    @Autowired
    private IdentifiableObjectManager manager;

    @Autowired
    private MetadataSystemSettingService metadataSystemSettingService;

    private MetadataVersion versionA;
    private MetadataVersion versionB;

    public static boolean compareVersionsUtil( MetadataVersion v1, MetadataVersion v2 )
    {
        if ( v1 == null && v2 == null )
        {
            return true;
        }
        else if ( v1 == null || v2 == null )
        {
            return false;
        }

        return (v1.getCreated() == v2.getCreated()) && (v1.getName().equals( v2.getName() )) && (v1.getType() == v2.getType());
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    @Override
    protected void setUpTest()
    {
        MockitoAnnotations.initMocks( this );
        versionA = new MetadataVersion( "Version_1", VersionType.ATOMIC );
        versionA.setHashCode( "12345" );
        versionB = new MetadataVersion( "Version_2", VersionType.BEST_EFFORT );
        versionB.setHashCode( "abcdef" );
    }

    @Test
    public void testShouldAddVersions()
    {
        long idA = versionService.addVersion( versionA );
        long idB = versionService.addVersion( versionB );

        assertTrue( idA >= 0 );
        assertTrue( idB >= 0 );

        assertTrue( compareVersionsUtil( versionA, versionService.getVersionById( idA ) ) );
        assertTrue( compareVersionsUtil( versionB, versionService.getVersionById( idB ) ) );
    }

    @Test
    public void testShouldDeleteAVersion()
    {
        long id = versionService.addVersion( versionA );

        versionService.deleteVersion( versionA );

        assertNull( versionService.getVersionById( id ) );
    }

    @Test
    public void testShouldGetVersionsBasedOnIdOrName()
    {
        long idA = versionService.addVersion( versionA );

        assertTrue( compareVersionsUtil( versionA, versionService.getVersionById( idA ) ) );

        versionService.addVersion( versionB );

        assertTrue( compareVersionsUtil( versionB, versionService.getVersionByName( "Version_2" ) ) );
    }

    @Test
    public void testShouldReturnTheLatestVersion()
    {
        versionService.addVersion( versionA );
        sleepFor( 100 );
        versionService.addVersion( versionB );

        assertTrue( compareVersionsUtil( versionB, versionService.getCurrentVersion() ) );
    }

    @Test
    public void testGetInitialVersion()
    {
        versionService.addVersion( versionA );
        sleepFor( 100 );
        versionService.addVersion( versionB );

        assertEquals( versionA, versionService.getInitialVersion() );
    }

    @Test
    public void testShouldReturnVersionsBetweenGivenTimeStamps()
    {
        List<MetadataVersion> versions = null;
        Date startDate = new Date();
        versionService.addVersion( versionA );
        versions = versionService.getAllVersionsInBetween( startDate, new Date() );

        assertEquals( 1, versions.size() );
        assertTrue( compareVersionsUtil( versionA, versions.get( 0 ) ) );

        versionService.addVersion( versionB );
        versions = versionService.getAllVersionsInBetween( startDate, new Date() );

        assertEquals( 2, versions.size() );
        assertTrue( compareVersionsUtil( versionB, versions.get( 1 ) ) );

        Date dateBetweenAandB = DateUtils.addMilliseconds( versions.get( 0 ).getCreated(), 1 );
        versions = versionService.getAllVersionsInBetween( dateBetweenAandB, new Date() );

        assertEquals( 1, versions.size() );
        assertTrue( compareVersionsUtil( versionB, versions.get( 0 ) ) );
    }

    @Test
    public void testShouldReturnAllVersionsInSystem()
    {
        assertEquals( 0, versionService.getAllVersions().size() );
        versionService.addVersion( versionA );
        versionService.addVersion( versionB );

        List<MetadataVersion> versions = versionService.getAllVersions();

        assertNotNull( versions );
        assertEquals( 2, versions.size() );
    }

    @Test
    public void testShouldSaveVersionAndSnapShot() throws NoSuchAlgorithmException
    {
        versionService.addVersion( versionA );
        versionService.saveVersion( VersionType.ATOMIC );

        //testing if correct version is saved in metadataVersion table
        assertEquals( "Version_2", versionService.getCurrentVersion().getName() );
        assertEquals( VersionType.ATOMIC, versionService.getCurrentVersion().getType() );

        //testing if correct version name is saved in system setting
        assertEquals( "Version_2", metadataSystemSettingService.getSystemMetadataVersion() );

        //testing hash code for the given metadata string
        MetadataVersion metadataVersionSnap = versionService.getVersionByName( "Version_2" );
        assertEquals( metadataVersionSnap.getHashCode(), versionService.getCurrentVersion().getHashCode() );

        //testing if correct version is saved in keyjsonvalue table
        List<String> versions = null;
        versions = metaDataKeyJsonService.getAllVersions();

        assertEquals( 1, versions.size() );
        assertEquals( "Version_2", versions.get( 0 ) );

        DataElement de1 = createDataElement( 'A' );
        manager.save( de1 );
        sleepFor( 100 );

        versionService.saveVersion( VersionType.BEST_EFFORT );
        KeyJsonValue expectedJson = metaDataKeyJsonService.getMetaDataVersion( "Version_3" );
        List<String> allVersions = metaDataKeyJsonService.getAllVersions( );

        assertEquals( 2, allVersions.size() );
        assertEquals( "Version_3", allVersions.get( 1 ) );
        assertEquals( true, expectedJson.getJbPlainValue().contains( "DataElementA" ) );
    }

    @Test
    public void testShouldCreateASnapshotThatContainsOnlyDelta()
    {
        versionService.addVersion( versionA );
        DataElement de1 = createDataElement( 'A' );
        manager.save( de1 );
        sleepFor( 100 );
        versionService.saveVersion( VersionType.BEST_EFFORT );
        de1 = createDataElement( 'B' );
        manager.save( de1 );
        sleepFor( 100 );
        versionService.saveVersion( VersionType.BEST_EFFORT );

        KeyJsonValue expectedJson = metaDataKeyJsonService.getMetaDataVersion( "Version_3" );

        assertEquals( false, expectedJson.getJbPlainValue().contains( "DataElementA" ) );
        assertEquals( true, expectedJson.getJbPlainValue().contains( "DataElementB" ) );
    }

    @Test
    public void testShouldGiveValidVersionDataIfExists() throws Exception
    {
        versionService.createMetadataVersionInDataStore( "myVersion", "myJson" );

        assertEquals( "myJson", versionService.getVersionData( "myVersion" ) );
    }

    @Test
    public void testShouldReturnNullWhenAVersionDoesNotExist() throws Exception
    {
        assertEquals( null, versionService.getVersionData( "myNonExistingVersion" ) );
    }

    @Test
    public void testShouldStoreSnapshotInMetadataStore()
    {
        versionService.createMetadataVersionInDataStore( "myVersion", "mySnapshot" );

        assertEquals( "mySnapshot", versionService.getVersionData( "myVersion" ) );
    }

    @Test( expected = MetadataVersionServiceException.class )
    public void testShouldThrowMetadataVersionServiceExceptionWhenSnapshotIsEmpty()
    {
        versionService.createMetadataVersionInDataStore( "myVersion", "" );
    }

    @Test( expected = MetadataVersionServiceException.class )
    public void testShouldThrowMetadataVersionServiceExceptionWhenSnapshotIsNull()
    {
        versionService.createMetadataVersionInDataStore( "myVersion", null );
    }

    @Test( expected = MetadataVersionServiceException.class )
    public void shouldThrowAnExceptionWhenVersionAndItsShanpShotAreNull()
    {
        versionService.isMetadataPassingIntegrity( null, null );
    }

    //--------------------------------------------------------------------------
    // Supportive methods
    //--------------------------------------------------------------------------

    private void sleepFor( int time )
    {
        try
        {
            Thread.sleep( time );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
    }
}
