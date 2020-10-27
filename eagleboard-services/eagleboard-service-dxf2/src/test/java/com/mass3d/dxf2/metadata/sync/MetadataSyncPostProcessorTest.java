package com.mass3d.dxf2.metadata.sync;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import com.mass3d.dxf2.metadata.feedback.ImportReport;
import com.mass3d.dxf2.metadata.jobs.MetadataRetryContext;
import com.mass3d.email.EmailService;
import com.mass3d.feedback.Status;
import com.mass3d.metadata.version.MetadataVersion;
import com.mass3d.metadata.version.VersionType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.retry.RetryContext;

public class MetadataSyncPostProcessorTest
{
    @Mock
    private EmailService emailService;

    @Mock
    private MetadataRetryContext metadataRetryContext;

    @InjectMocks
    private MetadataSyncPostProcessor metadataSyncPostProcessor;

    private MetadataVersion dataVersion;
    private MetadataSyncSummary metadataSyncSummary;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {

        dataVersion = new MetadataVersion();
        dataVersion.setType( VersionType.BEST_EFFORT );
        dataVersion.setName( "testVersion" );
        dataVersion.setCreated( new Date() );
        dataVersion.setHashCode( "samplehashcode" );
        metadataSyncSummary = new MetadataSyncSummary();
    }

    @Test
    public void testShouldSendSuccessEmailIfSyncSummaryIsOk()
    {
        metadataSyncSummary.setImportReport( new ImportReport() );
        metadataSyncSummary.getImportReport().setStatus( Status.OK );
        metadataSyncSummary.setMetadataVersion( dataVersion );
        MetadataRetryContext mockRetryContext = mock( MetadataRetryContext.class );

        boolean status = metadataSyncPostProcessor.handleSyncNotificationsAndAbortStatus( metadataSyncSummary, mockRetryContext, dataVersion );

        assertFalse( status );
    }

    @Test
    public void testShouldSendSuccessEmailIfSyncSummaryIsWarning()
    {
        metadataSyncSummary.setImportReport( new ImportReport() );
        metadataSyncSummary.getImportReport().setStatus( Status.WARNING );
        metadataSyncSummary.setMetadataVersion( dataVersion );
        MetadataRetryContext mockRetryContext = mock( MetadataRetryContext.class );

        boolean status = metadataSyncPostProcessor.handleSyncNotificationsAndAbortStatus( metadataSyncSummary, mockRetryContext, dataVersion );

        assertFalse( status );

    }

    @Test
    public void testShouldSendSuccessEmailIfSyncSummaryIsError()
    {
        metadataSyncSummary.setImportReport( new ImportReport() );
        metadataSyncSummary.getImportReport().setStatus( Status.ERROR );
        metadataSyncSummary.setMetadataVersion( dataVersion );
        MetadataRetryContext mockMetadataRetryContext = mock( MetadataRetryContext.class );
        RetryContext mockRetryContext = mock( RetryContext.class );

        when( mockMetadataRetryContext.getRetryContext() ).thenReturn( mockRetryContext );
        boolean status = metadataSyncPostProcessor.handleSyncNotificationsAndAbortStatus( metadataSyncSummary, mockMetadataRetryContext, dataVersion );

        assertTrue( status );
    }

    @Test
    public void testShouldSendEmailToAdminWithProperSubjectAndBody()
    {
        ImportReport importReport = mock( ImportReport.class );

        metadataSyncSummary.setImportReport( importReport );
        metadataSyncSummary.getImportReport().setStatus( Status.OK );
        metadataSyncSummary.setMetadataVersion( dataVersion );
        MetadataRetryContext mockRetryContext = mock( MetadataRetryContext.class );

        boolean status = metadataSyncPostProcessor.handleSyncNotificationsAndAbortStatus( metadataSyncSummary, mockRetryContext, dataVersion );

        assertFalse( status );
    }
}