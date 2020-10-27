package com.mass3d.dxf2.metadata.jobs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.mass3d.DhisSpringTest;
import com.mass3d.dxf2.metadata.feedback.ImportReport;
import com.mass3d.dxf2.metadata.sync.MetadataSyncSummary;
import com.mass3d.feedback.Status;
import com.mass3d.metadata.version.MetadataVersion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.retry.RetryContext;

public class MetadataRetryContextTest
    extends DhisSpringTest
{
    @Mock
    RetryContext retryContext;

    @InjectMocks
    MetadataRetryContext metadataRetryContext;

    private MetadataVersion mockVersion;
    private String testKey = "testKey";
    private String testMessage = "testMessage";

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );

        mockVersion = mock( MetadataVersion.class );
    }

    @Test
    public void testShouldGetRetryContextCorrectly() throws Exception
    {
        assertEquals( retryContext, metadataRetryContext.getRetryContext() );
    }

    @Test
    public void testShouldSetRetryContextCorrectly() throws Exception
    {
        RetryContext newMock = mock( RetryContext.class );

        metadataRetryContext.setRetryContext( newMock );

        assertEquals( newMock, metadataRetryContext.getRetryContext() );
    }

    @Test
    public void testIfVersionIsNull() throws Exception
    {
        metadataRetryContext.updateRetryContext( testKey, testMessage, null );

        verify( retryContext ).setAttribute( testKey, testMessage );
        verify( retryContext, never() ).setAttribute( MetadataSyncJob.VERSION_KEY, null );
    }

    @Test
    public void testIfVersionIsNotNull() throws Exception
    {
        metadataRetryContext.updateRetryContext( testKey, testMessage, mockVersion );

        verify( retryContext ).setAttribute( testKey, testMessage );
        verify( retryContext ).setAttribute( MetadataSyncJob.VERSION_KEY, mockVersion );
    }

    @Test
    public void testIfSummaryIsNull() throws Exception
    {
        MetadataSyncSummary metadataSyncSummary = mock( MetadataSyncSummary.class );

        metadataRetryContext.updateRetryContext( testKey, testMessage, mockVersion, null );

        verify( retryContext ).setAttribute( testKey, testMessage );
        verify( metadataSyncSummary, never() ).getImportReport();

    }

    @Test
    public void testIfSummaryIsNotNull() throws Exception
    {
        MetadataSyncSummary testSummary = new MetadataSyncSummary();
        ImportReport importReport = new ImportReport();
        importReport.setStatus( Status.ERROR );
        testSummary.setImportReport( importReport );

        metadataRetryContext.updateRetryContext( testKey, testMessage, mockVersion, testSummary );

        verify( retryContext ).setAttribute( testKey, testMessage );
    }
}