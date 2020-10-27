package com.mass3d.webapi.controller;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static com.mass3d.common.DhisApiVersion.ALL;
import static com.mass3d.common.DhisApiVersion.DEFAULT;
import static com.mass3d.dxf2.webmessage.WebMessageUtils.badRequest;
import static com.mass3d.dxf2.webmessage.WebMessageUtils.error;
import static com.mass3d.dxf2.webmessage.WebMessageUtils.notFound;
import static com.mass3d.feedback.Status.WARNING;
import static com.mass3d.fileresource.FileResourceDomain.DOCUMENT;
import static com.mass3d.fileresource.FileResourceKeyUtil.makeKey;
import static com.mass3d.setting.SettingKey.USE_CUSTOM_LOGO_BANNER;
import static com.mass3d.setting.SettingKey.USE_CUSTOM_LOGO_FRONT;
import static com.mass3d.webapi.utils.ContextUtils.getContextPath;
import static com.mass3d.webapi.utils.FileResourceUtils.build;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG;
import static org.springframework.util.MimeTypeUtils.parseMimeType;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import com.mass3d.dxf2.webmessage.WebMessage;
import com.mass3d.dxf2.webmessage.WebMessageException;
import com.mass3d.fileresource.FileResourceContentStore;
import com.mass3d.fileresource.FileResourceDomain;
import com.mass3d.fileresource.JCloudsFileResourceContentStore;
import com.mass3d.fileresource.SimpleImageResource;
import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.ImmutableMap;

/**
 * Serves and uploads custom images for the logo on the front page (logo_front)
 * and for the logo on the top banner (logo_banner).
 *
 */
@RestController
@RequestMapping(StaticContentController.RESOURCE_PATH)
@Slf4j
@ApiVersion( { DEFAULT, ALL } )
public class StaticContentController
{
    protected static final String RESOURCE_PATH = "/staticContent";

    private SystemSettingManager systemSettingManager;

//    private StyleManager styleManager;

    private FileResourceContentStore contentStore;

    protected static final String LOGO_BANNER = "logo_banner";

    protected static final String LOGO_FRONT = "logo_front";

    private static final FileResourceDomain DEFAULT_RESOURCE_DOMAIN = DOCUMENT;

    private static final Map<String, SettingKey> KEY_WHITELIST_MAP = ImmutableMap.of(
            LOGO_BANNER, USE_CUSTOM_LOGO_BANNER,
            LOGO_FRONT, USE_CUSTOM_LOGO_FRONT );

    @Autowired
    public StaticContentController(
        SystemSettingManager systemSettingManager,
//        StyleManager styleManager,
        JCloudsFileResourceContentStore contentStore
    )
    {
        checkNotNull( systemSettingManager );
//        checkNotNull( styleManager );
        checkNotNull( contentStore );
        this.systemSettingManager = systemSettingManager;
//        this.styleManager = styleManager;
        this.contentStore = contentStore;
    }

    /**
     * Serves the descriptor object for the file associated with the given key. If
     * the given key of the associated file is not found, this endpoint will return
     * HTTP NOT_FOUND. The attribute "Accept=application/json" in the HTTP Header
     * should be set in order to trigger this endpoint. The only supported image
     * type at this moment is PNG.
     *
     * @param key the key associated with the static file.
     * @param request the current HttpServletRequest.
     *
     * @return the SimpleStaticResource object related to the given key.
     * @throws WebMessageException if the the informed key is not found or the
     *         associated file is not persisted.
     */
    @GetMapping( value = "/{key}", produces = APPLICATION_JSON_VALUE )
    public ResponseEntity<SimpleImageResource> getStaticImages( final @PathVariable( "key" ) String key,
        final HttpServletRequest request )
        throws WebMessageException
    {
        if ( !KEY_WHITELIST_MAP.containsKey( key ) )
        {
            throw new WebMessageException( notFound( "Key does not exist." ) );
        }

        final boolean useCustomFile = (boolean) systemSettingManager.getSystemSetting( KEY_WHITELIST_MAP.get( key ) );

        if ( useCustomFile )
        {
            final String storageKey = makeKey( DEFAULT_RESOURCE_DOMAIN, Optional.of( key ) );
            final boolean customFileExists = contentStore.fileResourceContentExists( storageKey );

            if ( customFileExists )
            {
                final String blobEndpoint = getContextPath( request ) + "/api" + RESOURCE_PATH + "/" + key;

                final SimpleImageResource imageResource = new SimpleImageResource();
                imageResource.addImage( IMAGE_PNG.getSubtype(), blobEndpoint );

                return new ResponseEntity<>( imageResource, FOUND );
            }
        }
        throw new WebMessageException( notFound( "No custom file found." ) );
    }

    /**
     * Serves the PNG associated with the key. If custom logo is not used the
     * request will redirect to the default.
     *
     * @param key key associated with the file.
     */
    @RequestMapping( value = "/{key}", method = GET )
    public void getStaticContent(
        @PathVariable( "key" ) String key, HttpServletRequest request,
        HttpServletResponse response
    )
        throws WebMessageException
    {
        if ( !KEY_WHITELIST_MAP.containsKey( key ) )
        {
            throw new WebMessageException( notFound( "Key does not exist." ) );
        }

        boolean useCustomFile = (boolean) systemSettingManager.getSystemSetting( KEY_WHITELIST_MAP.get( key ) );

        if ( !useCustomFile ) // Serve default
        {
//            try
//            {
//                response.sendRedirect( getDefaultLogoUrl( request, key ) );
//            }
//            catch ( IOException e )
//            {
//                throw new WebMessageException( error( "Can't read the file." ) );
//            }
        }
        else // Serve custom
        {
            try
            {
                response.setContentType( IMAGE_PNG_VALUE );

                contentStore.copyContent( makeKey( DEFAULT_RESOURCE_DOMAIN, Optional.of( key ) ),
                    response.getOutputStream() );
            }
            catch ( NoSuchElementException e )
            {
                throw new WebMessageException( notFound( e.getMessage() ) );
            }
            catch ( IOException e )
            {
                throw new WebMessageException( error( "Failed to retrieve image", e.getMessage() ) );
            }
        }
    }

    /**
     * Uploads PNG images based on a key. Only accepts PNG and white listed keys.
     *
     * @param key  the key.
     * @param file the image file.
     */
    @PreAuthorize( "hasRole('ALL') or hasRole('F_SYSTEM_SETTING')" )
    @ResponseStatus( NO_CONTENT )
    @RequestMapping( value = "/{key}", method = POST )
    public void updateStaticContent( @PathVariable( "key" ) String key,
        @RequestParam( value = "file" ) MultipartFile file )
        throws WebMessageException
    {
        if ( file == null || file.isEmpty() )
        {
            throw new WebMessageException( badRequest( "Missing parameter 'file'" ) );
        }

        // Only PNG is accepted at the current time

        MimeType mimeType = parseMimeType( file.getContentType() );

        if ( !mimeType.isCompatibleWith( IMAGE_PNG ) )
        {
            throw new WebMessageException( new WebMessage( WARNING, UNSUPPORTED_MEDIA_TYPE ) );
        }

        // Only keys in the white list are accepted at the current time

        if ( !KEY_WHITELIST_MAP.containsKey( key ) )
        {
            throw new WebMessageException( badRequest( "This key is not supported." ) );
        }

        try
        {
            String fileKey = contentStore.saveFileResourceContent(
                build( key, file, DEFAULT_RESOURCE_DOMAIN ), file.getBytes() );

            if ( fileKey == null )
            {
                throw new WebMessageException( badRequest( "The resource was not saved" ) );
            }
            else
            {
                log.info( format( "File [%s] uploaded. Storage key: [%s]", file.getName(), fileKey ) );
            }
        }
        catch ( Exception e )
        {
            throw new WebMessageException( error( e.getMessage() ) );
        }
    }

//    /**
//     * Returns the relative URL of the default logo for a given key.
//     *
//     * @param key the key associated with the logo or null if the key does not exist.
//     * @return the relative URL of the logo.
//     */
//    private String getDefaultLogoUrl( HttpServletRequest request, String key )
//    {
//        String relativeUrlToImage = getContextPath( request );
//
//        if ( key.equals( LOGO_BANNER ) )
//        {
//            relativeUrlToImage += "/dhis-web-commons/css/" + styleManager.getCurrentStyleDirectory()
//                + "/logo_banner.png";
//        }
//
//        if ( key.equals( LOGO_FRONT ) )
//        {
//            relativeUrlToImage += "/dhis-web-commons/security/logo_front.png";
//        }
//
//        return relativeUrlToImage;
//    }
}
