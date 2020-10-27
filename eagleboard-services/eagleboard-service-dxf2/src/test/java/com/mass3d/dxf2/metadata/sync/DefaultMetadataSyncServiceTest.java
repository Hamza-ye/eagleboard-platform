package com.mass3d.dxf2.metadata.sync;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mass3d.dxf2.metadata.AtomicMode;
import com.mass3d.dxf2.metadata.MetadataImportParams;
import com.mass3d.dxf2.metadata.sync.exception.DhisVersionMismatchException;
import com.mass3d.dxf2.metadata.sync.exception.MetadataSyncServiceException;
import com.mass3d.dxf2.metadata.version.MetadataVersionDelegate;
import com.mass3d.metadata.version.MetadataVersion;
import com.mass3d.metadata.version.MetadataVersionService;
import com.mass3d.metadata.version.VersionType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class DefaultMetadataSyncServiceTest
{

    private MetadataSyncService metadataSyncService;

    @Mock
    private MetadataVersionDelegate metadataVersionDelegate;

    @Mock
    private MetadataSyncDelegate metadataSyncDelegate;

    @Mock
    private MetadataVersionService metadataVersionService;

    @Mock
    private MetadataSyncImportHandler metadataSyncImportHandler;

    private Map<String, List<String>> parameters;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup()
    {
        metadataSyncService = new DefaultMetadataSyncService( metadataVersionDelegate, metadataVersionService,
            metadataSyncDelegate, metadataSyncImportHandler );
        parameters = new HashMap<>();

    }

    @Test
    public void testShouldThrowExceptionWhenVersionNameNotPresentInParameters()
        throws MetadataSyncServiceException
    {
        expectedException.expect( MetadataSyncServiceException.class );
        expectedException.expectMessage( "Missing required parameter: 'versionName'" );

        metadataSyncService.getParamsFromMap( parameters );
    }

    @Test
    public void testShouldThrowExceptionWhenParametersAreNull()
        throws MetadataSyncServiceException
    {
        expectedException.expect( MetadataSyncServiceException.class );
        expectedException.expectMessage( "Missing required parameter: 'versionName'" );

        metadataSyncService.getParamsFromMap( null );
    }

    @Test
    public void testShouldThrowExceptionWhenParametersHaveVersionNameAsNull()
        throws MetadataSyncServiceException
    {
        parameters.put( "versionName", null );

        expectedException.expect( MetadataSyncServiceException.class );
        expectedException.expectMessage( "Missing required parameter: 'versionName'" );

        metadataSyncService.getParamsFromMap( parameters );
    }

    @Test
    public void testShouldThrowExceptionWhenParametersHaveVersionNameAssignedToEmptyList()
        throws MetadataSyncServiceException
    {
        parameters.put( "versionName", new ArrayList<>() );

        expectedException.expect( MetadataSyncServiceException.class );
        expectedException.expectMessage( "Missing required parameter: 'versionName'" );

        metadataSyncService.getParamsFromMap( parameters );
    }

    @Test
    public void testShouldReturnNullWhenVersionNameIsAssignedToListHavingNullEntry()
    {
        parameters.put( "versionName", new ArrayList<>() );
        parameters.get( "versionName" ).add( null );

        MetadataSyncParams paramsFromMap = metadataSyncService.getParamsFromMap( parameters );

        assertNull( paramsFromMap.getVersion() );
    }

    @Test
    public void testShouldReturnNullWhenVersionNameIsAssignedToListHavingEmptyString()
    {
        parameters.put( "versionName", new ArrayList<>() );
        parameters.get( "versionName" ).add( "" );

        MetadataSyncParams paramsFromMap = metadataSyncService.getParamsFromMap( parameters );

        assertNull( paramsFromMap.getVersion() );
    }

    @Test
    public void testShouldGetExceptionIfRemoteVersionIsNotAvailable()
    {
        parameters.put( "versionName", new ArrayList<>() );
        parameters.get( "versionName" ).add( "testVersion" );

        when( metadataVersionDelegate.getRemoteMetadataVersion( "testVersion" ) ).thenReturn( null );

        expectedException.expect( MetadataSyncServiceException.class );
        expectedException.expectMessage(
            "The MetadataVersion could not be fetched from the remote server for the versionName: testVersion" );

        metadataSyncService.getParamsFromMap( parameters );
    }

    @Test
    public void testShouldGetMetadataVersionForGivenVersionName()
    {
        parameters.put( "versionName", new ArrayList<>() );
        parameters.get( "versionName" ).add( "testVersion" );
        MetadataVersion metadataVersion = new MetadataVersion( "testVersion", VersionType.ATOMIC );

        when( metadataVersionDelegate.getRemoteMetadataVersion( "testVersion" ) ).thenReturn( metadataVersion );
        MetadataSyncParams metadataSyncParams = metadataSyncService.getParamsFromMap( parameters );

        assertEquals( metadataVersion, metadataSyncParams.getVersion() );
    }

    @Test
    public void testShouldThrowExceptionWhenSyncParamsIsNull()
        throws DhisVersionMismatchException
    {
        expectedException.expect( MetadataSyncServiceException.class );
        expectedException.expectMessage( "MetadataSyncParams cant be null" );

        metadataSyncService.doMetadataSync( null );
    }

    @Test
    public void testShouldThrowExceptionWhenVersionIsNulInSyncParams()
        throws DhisVersionMismatchException
    {
        MetadataSyncParams syncParams = new MetadataSyncParams();
        syncParams.setVersion( null );

        expectedException.expect( MetadataSyncServiceException.class );
        expectedException
            .expectMessage( "MetadataVersion for the Sync cant be null. The ClassListMap could not be constructed." );

        metadataSyncService.doMetadataSync( syncParams );
    }

    @Test
    public void testShouldThrowExceptionWhenSnapshotReturnsNullForGivenVersion()
        throws DhisVersionMismatchException
    {

        MetadataSyncParams syncParams = Mockito.mock( MetadataSyncParams.class );
        MetadataVersion metadataVersion = new MetadataVersion( "testVersion", VersionType.ATOMIC );

        when( syncParams.getVersion() ).thenReturn( metadataVersion );
        when( metadataVersionDelegate.downloadMetadataVersionSnapshot( metadataVersion ) ).thenReturn( null );

        expectedException.expect( MetadataSyncServiceException.class );
        expectedException.expectMessage( "Metadata snapshot can't be null." );

        metadataSyncService.doMetadataSync( syncParams );
    }

    @Test
    public void testShouldThrowExceptionWhenDHISVersionsMismatch()
        throws DhisVersionMismatchException
    {
        MetadataSyncParams syncParams = Mockito.mock( MetadataSyncParams.class );
        MetadataVersion metadataVersion = new MetadataVersion( "testVersion", VersionType.ATOMIC );
        String expectedMetadataSnapshot = "{\"date\":\"2016-05-24T05:27:25.128+0000\"}";

        when( syncParams.getVersion() ).thenReturn( metadataVersion );
        when( metadataVersionDelegate.downloadMetadataVersionSnapshot( metadataVersion ) )
            .thenReturn( expectedMetadataSnapshot );
        when( metadataSyncDelegate.shouldStopSync( expectedMetadataSnapshot ) ).thenReturn( true );
        when( metadataVersionService.isMetadataPassingIntegrity( metadataVersion, expectedMetadataSnapshot ) )
            .thenReturn( true );

        expectedException.expect( DhisVersionMismatchException.class );
        expectedException
            .expectMessage( "Metadata sync failed because your version of DHIS does not match the master version" );

        metadataSyncService.doMetadataSync( syncParams );
    }

    @Test
    public void testShouldNotThrowExceptionWhenDHISVersionsMismatch()
        throws DhisVersionMismatchException
    {
        MetadataSyncParams syncParams = Mockito.mock( MetadataSyncParams.class );
        MetadataVersion metadataVersion = new MetadataVersion( "testVersion", VersionType.ATOMIC );
        String expectedMetadataSnapshot = "{\"date\":\"2016-05-24T05:27:25.128+0000\", \"version\": \"2.26\"}";

        when( syncParams.getVersion() ).thenReturn( metadataVersion );
        when( metadataVersionDelegate.downloadMetadataVersionSnapshot( metadataVersion ) )
            .thenReturn( expectedMetadataSnapshot );
        when( metadataSyncDelegate.shouldStopSync( expectedMetadataSnapshot ) ).thenReturn( false );
        when( metadataVersionService.isMetadataPassingIntegrity( metadataVersion, expectedMetadataSnapshot ) )
            .thenReturn( true );

        metadataSyncService.doMetadataSync( syncParams );
    }

    @Test
    public void testShouldThrowExceptionWhenSnapshotNotPassingIntegrity()
        throws DhisVersionMismatchException
    {
        MetadataSyncParams syncParams = Mockito.mock( MetadataSyncParams.class );
        MetadataVersion metadataVersion = new MetadataVersion( "testVersion", VersionType.ATOMIC );
        String expectedMetadataSnapshot = "{\"date\":\"2016-05-24T05:27:25.128+0000\"}";

        when( syncParams.getVersion() ).thenReturn( metadataVersion );
        when( metadataVersionDelegate.downloadMetadataVersionSnapshot( metadataVersion ) )
            .thenReturn( expectedMetadataSnapshot );
        when( metadataVersionService.isMetadataPassingIntegrity( metadataVersion, expectedMetadataSnapshot ) )
            .thenReturn( false );

        expectedException.expect( MetadataSyncServiceException.class );
        expectedException.expectMessage( "Metadata snapshot is corrupted." );

        metadataSyncService.doMetadataSync( syncParams );
    }

    @Test
    public void testShouldStoreMetadataSnapshotInDataStoreAndImport()
        throws DhisVersionMismatchException
    {
        MetadataSyncParams syncParams = Mockito.mock( MetadataSyncParams.class );
        MetadataVersion metadataVersion = new MetadataVersion( "testVersion", VersionType.ATOMIC );
        MetadataSyncSummary metadataSyncSummary = new MetadataSyncSummary();
        metadataSyncSummary.setMetadataVersion( metadataVersion );
        String expectedMetadataSnapshot = "{\"date\":\"2016-05-24T05:27:25.128+0000\"}";

        when( syncParams.getVersion() ).thenReturn( metadataVersion );
        when( metadataVersionService.getVersionData( "testVersion" ) ).thenReturn( null );
        when( metadataVersionDelegate.downloadMetadataVersionSnapshot( metadataVersion ) )
            .thenReturn( expectedMetadataSnapshot );
        when( metadataVersionService.isMetadataPassingIntegrity( metadataVersion, expectedMetadataSnapshot ) )
            .thenReturn( true );
        when( metadataSyncImportHandler.importMetadata( syncParams, expectedMetadataSnapshot ) )
            .thenReturn( metadataSyncSummary );

        MetadataSyncSummary actualSummary = metadataSyncService.doMetadataSync( syncParams );

        verify( metadataVersionService, times( 1 ) ).createMetadataVersionInDataStore( metadataVersion.getName(),
            expectedMetadataSnapshot );
        assertNull( actualSummary.getImportReport() );
        assertNull( actualSummary.getImportSummary() );
        assertEquals( metadataVersion, actualSummary.getMetadataVersion() );
    }

    @Test
    public void testShouldNotStoreMetadataSnapshotInDataStoreWhenAlreadyExistsInLocalStore()
        throws DhisVersionMismatchException
    {
        MetadataSyncParams syncParams = Mockito.mock( MetadataSyncParams.class );

        MetadataVersion metadataVersion = new MetadataVersion( "testVersion", VersionType.ATOMIC );

        MetadataSyncSummary metadataSyncSummary = new MetadataSyncSummary();
        metadataSyncSummary.setMetadataVersion( metadataVersion );

        String expectedMetadataSnapshot = "{\"date\":\"2016-05-24T05:27:25.128+0000\"}";

        when( syncParams.getVersion() ).thenReturn( metadataVersion );
        when( metadataVersionService.getVersionData( "testVersion" ) ).thenReturn( expectedMetadataSnapshot );

        when( metadataSyncImportHandler.importMetadata( syncParams, expectedMetadataSnapshot ) )
            .thenReturn( metadataSyncSummary );

        MetadataSyncSummary actualSummary = metadataSyncService.doMetadataSync( syncParams );

        verify( metadataVersionService, never() ).createMetadataVersionInDataStore( metadataVersion.getName(),
            expectedMetadataSnapshot );
        verify( metadataVersionDelegate, never() ).downloadMetadataVersionSnapshot( metadataVersion );
        assertNull( actualSummary.getImportReport() );
        assertNull( actualSummary.getImportSummary() );
        assertEquals( metadataVersion, actualSummary.getMetadataVersion() );
    }

    @Test
    public void testShouldVerifyImportParamsAtomicTypeForTheGivenBestEffortVersion()
        throws DhisVersionMismatchException
    {
        MetadataSyncParams syncParams = new MetadataSyncParams();

        MetadataVersion metadataVersion = new MetadataVersion( "testVersion", VersionType.BEST_EFFORT );
        MetadataImportParams metadataImportParams = new MetadataImportParams();

        syncParams.setVersion( metadataVersion );
        syncParams.setImportParams( metadataImportParams );

        MetadataSyncSummary metadataSyncSummary = new MetadataSyncSummary();
        metadataSyncSummary.setMetadataVersion( metadataVersion );
        String expectedMetadataSnapshot = "{\"date\":\"2016-05-24T05:27:25.128+0000\"}";

        when( metadataVersionService.getVersionData( "testVersion" ) ).thenReturn( expectedMetadataSnapshot );

        metadataSyncService.doMetadataSync( syncParams );

        verify( metadataSyncImportHandler, times( 1 ) ).importMetadata(
            (argThat( metadataSyncParams -> syncParams.getImportParams().getAtomicMode().equals( AtomicMode.NONE ) )),
            eq( expectedMetadataSnapshot ) );

        verify( metadataVersionService, never() ).createMetadataVersionInDataStore( metadataVersion.getName(),
            expectedMetadataSnapshot );
        verify( metadataVersionDelegate, never() ).downloadMetadataVersionSnapshot( metadataVersion );

    }
}