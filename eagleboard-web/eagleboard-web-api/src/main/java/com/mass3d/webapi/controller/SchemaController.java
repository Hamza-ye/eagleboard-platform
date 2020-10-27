package com.mass3d.webapi.controller;

import com.google.common.collect.Lists;
import com.mass3d.common.DhisApiVersion;
import com.mass3d.dxf2.webmessage.WebMessage;
import com.mass3d.dxf2.webmessage.WebMessageUtils;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.fieldfilter.FieldFilterParams;
import com.mass3d.fieldfilter.FieldFilterService;
import com.mass3d.node.NodeUtils;
import com.mass3d.node.types.CollectionNode;
import com.mass3d.node.types.RootNode;
import com.mass3d.render.RenderService;
import com.mass3d.schema.Property;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaService;
import com.mass3d.schema.Schemas;
import com.mass3d.schema.validation.SchemaValidator;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import com.mass3d.webapi.service.ContextService;
import com.mass3d.webapi.service.LinkService;
import com.mass3d.webapi.service.WebMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping( value = "/schemas", method = RequestMethod.GET )
@ApiVersion( { DhisApiVersion.DEFAULT, DhisApiVersion.ALL } )
public class SchemaController
{
    @Autowired
    private SchemaService schemaService;

    @Autowired
    private SchemaValidator schemaValidator;

    @Autowired
    private RenderService renderService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private FieldFilterService fieldFilterService;

    @Autowired
    private ContextService contextService;

    @Autowired
    private WebMessageService webMessageService;

    @RequestMapping
    public @ResponseBody RootNode getSchemas()
    {
        List<String> fields = Lists.newArrayList( contextService.getParameterValues( "fields" ) );

        if ( fields.isEmpty() )
        {
            fields.add( "*" );
        }

        Schemas schemas = new Schemas( schemaService.getSortedSchemas() );
        linkService.generateSchemaLinks( schemas.getSchemas() );

        RootNode rootNode = NodeUtils.createRootNode( "schemas" );
        CollectionNode collectionNode = fieldFilterService.toCollectionNode( Schema.class, new FieldFilterParams( schemas.getSchemas(), fields ) );
        collectionNode.setWrapping( false );
        rootNode.addChild( collectionNode );

        return rootNode;
    }

    @RequestMapping( value = "/{type}", method = RequestMethod.GET )
    public @ResponseBody RootNode getSchema( @PathVariable String type )
    {
        List<String> fields = Lists.newArrayList( contextService.getParameterValues( "fields" ) );

        if ( fields.isEmpty() )
        {
            fields.add( "*" );
        }

        Schema schema = getSchemaFromType( type );

        if ( schema != null )
        {
            linkService.generateSchemaLinks( schema );

            CollectionNode collectionNode = fieldFilterService.toCollectionNode( Schema.class,
                new FieldFilterParams( Collections.singletonList( schema ), fields ) );
            return NodeUtils.createRootNode( collectionNode.getChildren().get( 0 ) );
        }

        throw new HttpClientErrorException( HttpStatus.NOT_FOUND, "Type " + type + " does not exist." );
    }

    @RequestMapping( value = "/{type}", method = { RequestMethod.POST, RequestMethod.PUT }, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    public void validateSchema( @PathVariable String type, HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        Schema schema = getSchemaFromType( type );

        if ( schema == null )
        {
            throw new HttpClientErrorException( HttpStatus.NOT_FOUND, "Type " + type + " does not exist." );
        }

        Object object = renderService.fromJson( request.getInputStream(), schema.getKlass() );
        List<ErrorReport> validationViolations = schemaValidator.validate( object );

        WebMessage webMessage = WebMessageUtils.errorReports( validationViolations );
        webMessageService.send( webMessage, response, request );
    }

    @RequestMapping( value = "/{type}/{property}", method = RequestMethod.GET )
    public @ResponseBody Property getSchemaProperty( @PathVariable String type, @PathVariable String property )
    {
        Schema schema = getSchemaFromType( type );

        if ( schema == null )
        {
            throw new HttpClientErrorException( HttpStatus.NOT_FOUND, "Type " + type + " does not exist." );
        }

        if ( schema.haveProperty( property ) )
        {
            return schema.getProperty( property );
        }

        throw new HttpClientErrorException( HttpStatus.NOT_FOUND, "Property " + property + " does not exist on type " + type + "." );
    }

    private Schema getSchemaFromType( String type )
    {
        Schema schema = schemaService.getSchemaBySingularName( type );

        if ( schema == null )
        {
            try
            {
                schema = schemaService.getSchema( Class.forName( type ) );
            }
            catch ( ClassNotFoundException ignored )
            {
            }
        }

        return schema;
    }
}
