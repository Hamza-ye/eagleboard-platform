package com.mass3d.webapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import com.mass3d.common.cache.CacheStrategy;
import com.mass3d.document.Document;
import com.mass3d.document.DocumentService;
import com.mass3d.dxf2.webmessage.WebMessageException;
import com.mass3d.dxf2.webmessage.WebMessageUtils;
import com.mass3d.external.location.LocationManager;
import com.mass3d.fileresource.FileResource;
import com.mass3d.fileresource.FileResourceService;
import com.mass3d.schema.descriptors.DocumentSchemaDescriptor;
import com.mass3d.webapi.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
@Slf4j
@RequestMapping( value = DocumentSchemaDescriptor.API_ENDPOINT )
public class DocumentController
    extends AbstractCrudController<Document>
{

    @Autowired
    private DocumentService documentService;

    @Autowired
    private LocationManager locationManager;

    @Autowired
    private FileResourceService fileResourceService;

    @Autowired
    private ContextUtils contextUtils;

    @RequestMapping( value = "/{uid}/data", method = RequestMethod.GET )
    public void getDocumentContent( @PathVariable( "uid" ) String uid, HttpServletResponse response )
        throws Exception
    {
        Document document = documentService.getDocument( uid );

        if ( document == null )
        {
            throw new WebMessageException( WebMessageUtils.notFound( "Document not found for uid: " + uid ) );
        }

        if ( document.isExternal() )
        {
            response.sendRedirect( response.encodeRedirectURL( document.getUrl() ) );
        }
        else if ( document.getFileResource() != null )
        {
            FileResource fileResource = document.getFileResource();

            response.setContentType( fileResource.getContentType() );
            response.setContentLength( new Long( fileResource.getContentLength() ).intValue() );
            response.setHeader( HttpHeaders.CONTENT_DISPOSITION, "filename=" + fileResource.getName() );

            try
            {
                fileResourceService.copyFileResourceContent( fileResource, response.getOutputStream() );
            }
            catch ( IOException e )
            {
                throw new WebMessageException( WebMessageUtils.error( "Failed fetching the file from storage",
                    "There was an exception when trying to fetch the file from the storage backend, could be network or filesystem related" ) );
            }
        }
        else
        {
            contextUtils.configureResponse( response, document.getContentType(), CacheStrategy.CACHE_TWO_WEEKS,
                document.getUrl(),
                document.getAttachment() == null ? false : document.getAttachment() );

            try ( InputStream in = locationManager.getInputStream( document.getUrl(), DocumentService.DIR ) )
            {
                IOUtils.copy( in, response.getOutputStream() );
            }
            catch ( IOException e )
            {
                log.error( "Could not retrieve file.", e );
                throw new WebMessageException( WebMessageUtils.error( "Failed fetching the file from storage",
                    "There was an exception when trying to fetch the file from the storage backend. " +
                        "Depending on the provider the root cause could be network or file system related." ) );
            }
        }
    }
}
