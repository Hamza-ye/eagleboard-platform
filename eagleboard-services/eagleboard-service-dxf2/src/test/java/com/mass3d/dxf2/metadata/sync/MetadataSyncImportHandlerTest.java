package com.mass3d.dxf2.metadata.sync;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import com.mass3d.dxf2.metadata.MetadataImportParams;
import com.mass3d.dxf2.metadata.MetadataImportService;
import com.mass3d.dxf2.metadata.feedback.ImportReport;
import com.mass3d.dxf2.metadata.sync.exception.MetadataSyncImportException;
import com.mass3d.dxf2.metadata.sync.exception.MetadataSyncServiceException;
import com.mass3d.dxf2.metadata.version.MetadataVersionDelegate;
import com.mass3d.feedback.Status;
import com.mass3d.metadata.version.MetadataVersion;
import com.mass3d.metadata.version.VersionType;
import com.mass3d.render.RenderFormat;
import com.mass3d.render.RenderService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class MetadataSyncImportHandlerTest
{
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    MetadataImportService metadataImportService;

    @Mock
    MetadataVersionDelegate metadataVersionDelegate;

    @Mock
    private RenderService renderService;

    @InjectMocks
    private MetadataSyncImportHandler metadataSyncImportHandler;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private MetadataVersion metadataVersion;

    private String expectedMetadataSnapshot;

    private MetadataSyncParams syncParams;

    private ImportReport importReport;

    @Before
    public void setup()
    {
        metadataVersion = new MetadataVersion( "testVersion", VersionType.ATOMIC );
        expectedMetadataSnapshot = "{\"date\":\"2016-05-24T05:27:25.128+0000\"}";
        syncParams = new MetadataSyncParams();
        importReport = new ImportReport();
    }

    @Test
    public void testShouldThrowExceptionWhenNoVersionSet()
    {
        syncParams.setImportParams( null );
        expectedException.expect( MetadataSyncServiceException.class );
        expectedException.expectMessage( "MetadataVersion for the Sync cant be null." );
        metadataSyncImportHandler.importMetadata( syncParams, expectedMetadataSnapshot );
    }

    @Test
    public void testShouldThrowExceptionWhenNoImportParams()
    {
        syncParams.setVersion( metadataVersion );
        syncParams.setImportParams( null );

        expectedException.expect( MetadataSyncServiceException.class );
        expectedException.expectMessage( "MetadataImportParams for the Sync cant be null." );

        metadataSyncImportHandler.importMetadata( syncParams, expectedMetadataSnapshot );
    }

    @Test
    public void testShouldThrowExceptionWhenImportServiceFails()
    {
        syncParams.setImportParams( new MetadataImportParams() );
        syncParams.setVersion( metadataVersion );

        when( metadataImportService.importMetadata( syncParams.getImportParams() ) )
            .thenThrow( new MetadataSyncServiceException( "" ) );
        expectedException.expect( MetadataSyncImportException.class );
        metadataSyncImportHandler.importMetadata( syncParams, expectedMetadataSnapshot );
        verify( metadataVersionDelegate, never() ).addNewMetadataVersion( metadataVersion );
    }

    @Test
    public void testShouldImportMetadata()
    {
        syncParams.setImportParams( new MetadataImportParams() );
        syncParams.setVersion( metadataVersion );
        MetadataSyncSummary metadataSyncSummary = new MetadataSyncSummary();
        importReport.setStatus( Status.OK );

        when( metadataImportService.importMetadata( syncParams.getImportParams() ) ).thenReturn( importReport );

        metadataSyncSummary.setImportReport( importReport );
        metadataSyncSummary.setMetadataVersion( metadataVersion );

        doNothing().when( metadataVersionDelegate ).addNewMetadataVersion( metadataVersion );

        MetadataSyncSummary actualMetadataSyncSummary = metadataSyncImportHandler.importMetadata( syncParams,
            expectedMetadataSnapshot );

        verify( metadataVersionDelegate ).addNewMetadataVersion( metadataVersion );
        assertEquals( metadataSyncSummary.getImportReport(), actualMetadataSyncSummary.getImportReport() );
        assertEquals( metadataSyncSummary.getImportSummary(), actualMetadataSyncSummary.getImportSummary() );
        assertEquals( metadataSyncSummary.getMetadataVersion(), actualMetadataSyncSummary.getMetadataVersion() );
        assertEquals( metadataSyncSummary.getMetadataVersion().getType(),
            actualMetadataSyncSummary.getMetadataVersion().getType() );
        assertEquals( metadataSyncSummary.getImportReport().getStatus(),
            actualMetadataSyncSummary.getImportReport().getStatus() );
    }

    @Test
    public void testShouldImportMetadataWhenBestEffortWithWarnings() {
        syncParams.setImportParams( new MetadataImportParams() );
        syncParams.setVersion( metadataVersion );
        MetadataSyncSummary metadataSyncSummary = new MetadataSyncSummary();
        importReport.setStatus( Status.WARNING );
        metadataVersion.setType( VersionType.BEST_EFFORT );

        when( metadataImportService.importMetadata( syncParams.getImportParams() ) ).thenReturn( importReport );

        metadataSyncSummary.setImportReport( importReport );
        metadataSyncSummary.setMetadataVersion( metadataVersion );

        doNothing().when( metadataVersionDelegate ).addNewMetadataVersion( metadataVersion );

        MetadataSyncSummary actualMetadataSyncSummary = metadataSyncImportHandler.importMetadata( syncParams,
            expectedMetadataSnapshot );
        verify( metadataVersionDelegate ).addNewMetadataVersion( metadataVersion );
        assertEquals( metadataSyncSummary.getImportReport(), actualMetadataSyncSummary.getImportReport() );
        assertEquals( metadataSyncSummary.getImportSummary(), actualMetadataSyncSummary.getImportSummary() );
        assertEquals( metadataSyncSummary.getMetadataVersion(), actualMetadataSyncSummary.getMetadataVersion() );
        assertEquals( metadataSyncSummary.getMetadataVersion().getType(),
            actualMetadataSyncSummary.getMetadataVersion().getType() );
        assertEquals( metadataSyncSummary.getImportReport().getStatus(),
            actualMetadataSyncSummary.getImportReport().getStatus() );
    }

    @Test
    public void testShouldThrowExceptionWhenClassListMapIsNull()
        throws IOException
    {
        syncParams.setImportParams( new MetadataImportParams() );
        syncParams.setVersion( metadataVersion );
        importReport.setStatus( Status.OK );

        when( renderService.fromMetadata( any( InputStream.class ), eq( RenderFormat.JSON ) ) ).thenReturn( null );

        expectedException.expect( MetadataSyncServiceException.class );
        expectedException.expectMessage( "ClassListMap can't be null" );

        metadataSyncImportHandler.importMetadata( syncParams, expectedMetadataSnapshot );

        verify( renderService ).fromMetadata( any( InputStream.class ), RenderFormat.JSON );
        verify( metadataImportService, never() ).importMetadata( syncParams.getImportParams() );
        verify( metadataVersionDelegate, never() ).addNewMetadataVersion( metadataVersion );
        verify( metadataVersionDelegate, never() ).addNewMetadataVersion( metadataVersion );
    }

    @Test
    public void testShouldThrowExceptionWhenParsingClassListMap()
        throws IOException
    {
        syncParams.setImportParams( new MetadataImportParams() );
        syncParams.setVersion( metadataVersion );
        importReport.setStatus( Status.OK );

        when( renderService.fromMetadata( any( InputStream.class ), eq( RenderFormat.JSON ) ) )
            .thenThrow( new IOException() );

        expectedException.expect( MetadataSyncServiceException.class );
        expectedException
            .expectMessage( "Exception occurred while trying to do JSON conversion while parsing class list map" );

        metadataSyncImportHandler.importMetadata( syncParams, expectedMetadataSnapshot );

        verify( renderService ).fromMetadata( any( InputStream.class ), RenderFormat.JSON );
        verify( metadataImportService, never() ).importMetadata( syncParams.getImportParams() );
        verify( metadataVersionDelegate, never() ).addNewMetadataVersion( metadataVersion );
        verify( metadataVersionDelegate, never() ).addNewMetadataVersion( metadataVersion );
    }

    @Test
    public void testShouldReturnDefaultSummaryWhenImportStatusIsError()
    {
        syncParams.setImportParams( new MetadataImportParams() );
        syncParams.setVersion( metadataVersion );
        MetadataSyncSummary metadataSyncSummary = new MetadataSyncSummary();

        metadataSyncSummary.setImportReport( new ImportReport() );
        metadataSyncSummary.setMetadataVersion( metadataVersion );

        importReport.setStatus( Status.ERROR );

        when( metadataImportService.importMetadata( syncParams.getImportParams() ) ).thenReturn( importReport );

        MetadataSyncSummary actualMetadataSyncSummary = metadataSyncImportHandler.importMetadata( syncParams,
            expectedMetadataSnapshot );

        verify( metadataVersionDelegate, never() ).addNewMetadataVersion( metadataVersion );
        assertEquals( metadataSyncSummary.getImportReport().toString(),
            actualMetadataSyncSummary.getImportReport().toString() );
        assertEquals( metadataSyncSummary.getImportSummary(), actualMetadataSyncSummary.getImportSummary() );
        assertEquals( metadataSyncSummary.getMetadataVersion(), actualMetadataSyncSummary.getMetadataVersion() );
    }
}