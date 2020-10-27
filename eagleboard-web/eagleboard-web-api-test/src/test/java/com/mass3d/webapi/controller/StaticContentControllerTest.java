package com.mass3d.webapi.controller;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_MOVED_TEMPORARILY;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.StringContains.containsString;
import static com.mass3d.fileresource.FileResourceDomain.DOCUMENT;
import static com.mass3d.fileresource.FileResourceKeyUtil.makeKey;
import static com.mass3d.setting.SettingKey.USE_CUSTOM_LOGO_BANNER;
import static com.mass3d.webapi.controller.StaticContentController.LOGO_BANNER;
import static com.mass3d.webapi.controller.StaticContentController.RESOURCE_PATH;
import static com.mass3d.webapi.documentation.common.TestUtils.APPLICATION_JSON_UTF8;
import static com.mass3d.webapi.utils.FileResourceUtils.build;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG;

import java.util.Optional;

import com.mass3d.fileresource.JCloudsFileResourceContentStore;
import com.mass3d.setting.SystemSettingManager;
import com.mass3d.webapi.DhisWebSpringTest;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

public class StaticContentControllerTest
    extends
    DhisWebSpringTest
{

    private final static String URL = "/staticContent/";

    private final static String MIME_PNG = IMAGE_PNG.toString();

    private MockHttpSession session;

    private MockMultipartFile mockMultipartFile;

    @Autowired
    private SystemSettingManager systemSettingManager;

    @Autowired
    private JCloudsFileResourceContentStore fileResourceContentStore;

    @Before
    public void setUp()
    {
        this.session = getSession( "ALL" );
        this.mockMultipartFile = new MockMultipartFile( "file", "testlogo.png", MIME_PNG, "image".getBytes() );
        systemSettingManager.saveSystemSetting( USE_CUSTOM_LOGO_BANNER, FALSE );
    }

    @Test
    public void verifyFetchWithInvalidKey()
        throws Exception
    {
        mvc.perform( get( URL + "idontexist" ).session( session ).contentType( APPLICATION_JSON_UTF8 ) )
            .andExpect( status().is( SC_NOT_FOUND ) );
    }

    @Test
    public void verifyFetchWithDefaultKey()
        throws Exception
    {
        mvc.perform(
            get( URL + LOGO_BANNER )
                .accept( TEXT_HTML_VALUE )
                .session( session ) )
                    .andExpect( redirectedUrlPattern( "**/dhis-web-commons/css/light_blue/logo_banner.png" ) )
                    .andExpect( status().is( SC_MOVED_TEMPORARILY ) );
    }

    @Test
    public void verifyFetchCustom()
        throws Exception
    {
        // store a mock file to the content store, before fetching it
        fileResourceContentStore.saveFileResourceContent( build( LOGO_BANNER,
            mockMultipartFile, DOCUMENT ), "image".getBytes() );

        systemSettingManager.saveSystemSetting( USE_CUSTOM_LOGO_BANNER, TRUE );

        mvc.perform(
            get( URL + LOGO_BANNER )
                .accept( TEXT_HTML_VALUE )
                .session( session ) )
                    .andExpect( content().contentType( MIME_PNG ) )
                    .andExpect( content().bytes( mockMultipartFile.getBytes() ) )
                    .andExpect( status().is( SC_OK ) );
    }

    @Test
    public void testGetStaticImagesCustomKey()
            throws Exception
    {
        // Given
        final String theExpectedType = "png";
        final String theExpectedApiUrl = "/api" + RESOURCE_PATH;

        // a mock file in the content store used during the fetch
        fileResourceContentStore.saveFileResourceContent( build( LOGO_BANNER,
                mockMultipartFile, DOCUMENT ), "image".getBytes() );

        // a positive flag indicating the usage of a custom logo
        systemSettingManager.saveSystemSetting( USE_CUSTOM_LOGO_BANNER, TRUE );

        // When
        final ResultActions result = mvc.perform(
                get( URL + LOGO_BANNER )
                    .accept( APPLICATION_JSON )
                    .session( session ) );

        // Then
        result
            .andExpect( content().contentType( APPLICATION_JSON ) )
            .andExpect( content().string( containsString( theExpectedType ) ) )
            .andExpect( content().string( containsString( theExpectedApiUrl ) ) )
            .andExpect( status().isFound() );
    }

    @Test
    public void testGetStaticImagesUsingNonExistingKey()
            throws Exception
    {
        // Given
        final String theExpectedStatusMessage = "Not Found";
        final String theExpectedStatusCode = "404";
        final String theExpectedStatus = "ERROR";
        final String theExpectedMessage = "Key does not exist.";
        final String aNonExistingLogoBanner = "nonExistingLogo";

        // a mock file in the content store used during the fetch
        fileResourceContentStore.saveFileResourceContent( build( LOGO_BANNER,
                mockMultipartFile, DOCUMENT ), "image".getBytes() );

        // When
        final ResultActions result = mvc.perform(
                get( URL + aNonExistingLogoBanner )
                    .accept( APPLICATION_JSON )
                    .session( session ) );

        // Then
        result
            .andExpect( content().contentType( APPLICATION_JSON ) )
            .andExpect( content().string( not ( containsString( "png" ) ) ) )
            .andExpect( content().string( containsString( theExpectedStatusMessage ) ) )
            .andExpect( content().string( containsString( theExpectedStatus ) ) )
            .andExpect( content().string( containsString( theExpectedMessage ) ) )
            .andExpect( content().string( containsString( theExpectedStatusCode ) ) )
            .andExpect( status().isNotFound() );
    }

    @Test
    public void testGetStaticImagesUsingNonExistingLogo()
            throws Exception
    {
        // Given
        final String theExpectedStatusMessage = "Not Found";
        final String theExpectedStatusCode = "404";
        final String theExpectedStatus = "ERROR";
        final String theExpectedMessage = "No custom file found.";

        // a non existing logo in the content store used during the fetch
        fileResourceContentStore.deleteFileResourceContent( makeKey( DOCUMENT, Optional.of( LOGO_BANNER ) ) );

        // When
        final ResultActions result = mvc.perform(
                get( URL + LOGO_BANNER )
                        .accept( APPLICATION_JSON )
                        .session( session ) );

        // Then
        result
                .andExpect( content().contentType( APPLICATION_JSON ) )
                .andExpect( content().string( not ( containsString( "png" ) ) ) )
                .andExpect( content().string( containsString( theExpectedStatusMessage ) ) )
                .andExpect( content().string( containsString( theExpectedStatus ) ) )
                .andExpect( content().string( containsString( theExpectedMessage ) ) )
                .andExpect( content().string( containsString( theExpectedStatusCode ) ) )
                .andExpect( status().isNotFound() );
    }

    @Test
    public void verifyStoreImage()
        throws Exception
    {
        mvc.perform(
             multipart( URL + LOGO_BANNER ).file( mockMultipartFile ).session( session ) )
            .andExpect( status().is( SC_NO_CONTENT ) );
    }

    @Test
    public void verifyErrorWhenStoringInvalidMimeType()
        throws Exception
    {
        final String error = buildResponse( "Unsupported Media Type", 415, "WARNING", null );

        mvc.perform( multipart( URL + LOGO_BANNER ).file(
            new MockMultipartFile( "file", "testlogo.png", IMAGE_JPEG.toString(), "image".getBytes() ) )
            .session( session ) ).andExpect( content().json( error ) )
            .andExpect( status().is( SC_UNSUPPORTED_MEDIA_TYPE ) );
    }

    @Test
    public void verifyErrorWhenStoringInvalidKey()
        throws Exception
    {
        final String error = buildResponse( "Bad Request", 400, "ERROR", "This key is not supported." );

        mvc.perform( multipart( URL + "idontexist" ).file( mockMultipartFile ).session( session ) )
            .andExpect( content().json( error ) ).andExpect( status().is( SC_BAD_REQUEST ) );
    }

    private String buildResponse( String httpStatus, int code, String status, String message )
        throws Exception
    {
        return new JSONObject().put( "httpStatus", httpStatus ).put( "httpStatusCode", code ).put( "status", status )
            .putOpt( "message", message ).toString();
    }
}