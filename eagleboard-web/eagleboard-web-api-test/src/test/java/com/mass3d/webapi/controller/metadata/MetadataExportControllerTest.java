package com.mass3d.webapi.controller.metadata;

import com.mass3d.dxf2.metadata.MetadataExportService;
import com.mass3d.node.types.RootNode;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.UserSettingService;
import com.mass3d.webapi.service.ContextService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

/**
 * Unit tests for {@link MetadataExportControllerTest}.
 *
 */
public class MetadataExportControllerTest
{
    @Mock
    private MetadataExportService metadataExportService;

    @Mock
    private ContextService contextService;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private UserSettingService userSettingService;

    @InjectMocks
    private MetadataImportExportController controller;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Test
    public void withoutDownload()
    {
        ResponseEntity<RootNode> responseEntity = controller.getMetadata( false, null, false );
        Assert.assertNull( responseEntity.getHeaders().get( HttpHeaders.CONTENT_DISPOSITION ) );
    }

    @Test
    public void withDownload()
    {
        ResponseEntity<RootNode> responseEntity = controller.getMetadata( false, null, true );
        Assert.assertNotNull( responseEntity.getHeaders().get( HttpHeaders.CONTENT_DISPOSITION ) );
        Assert.assertEquals( "attachment; filename=metadata", responseEntity.getHeaders().get( HttpHeaders.CONTENT_DISPOSITION ).get( 0 ) );
    }
}