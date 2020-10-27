package com.mass3d.webapi.controller.metadata;

import org.apache.commons.lang3.StringUtils;
import com.mass3d.common.DhisApiVersion;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.UserContext;
import com.mass3d.commons.util.StreamUtils;
import com.mass3d.dxf2.common.TranslateParams;
import com.mass3d.dxf2.csv.CsvImportClass;
import com.mass3d.dxf2.csv.CsvImportOptions;
import com.mass3d.dxf2.csv.CsvImportService;
import com.mass3d.dxf2.metadata.Metadata;
import com.mass3d.dxf2.metadata.MetadataExportParams;
import com.mass3d.dxf2.metadata.MetadataExportService;
import com.mass3d.dxf2.metadata.MetadataImportParams;
import com.mass3d.dxf2.metadata.MetadataImportService;
import com.mass3d.dxf2.metadata.feedback.ImportReport;
import com.mass3d.dxf2.webmessage.WebMessageUtils;
import com.mass3d.node.types.RootNode;
import com.mass3d.render.RenderFormat;
import com.mass3d.render.RenderService;
import com.mass3d.scheduling.SchedulingManager;
import com.mass3d.schema.SchemaService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.user.UserSettingKey;
import com.mass3d.user.UserSettingService;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import com.mass3d.webapi.service.ContextService;
import com.mass3d.webapi.service.WebMessageService;
import com.mass3d.webapi.utils.ContextUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.mass3d.dxf2.webmessage.WebMessageUtils.jobConfigurationReport;
import static com.mass3d.scheduling.JobType.GML_IMPORT;
import static com.mass3d.scheduling.JobType.METADATA_IMPORT;

@Controller
@RequestMapping( "/metadata" )
@ApiVersion( { DhisApiVersion.DEFAULT, DhisApiVersion.ALL } )
public class MetadataImportExportController
{
    @Autowired
    private MetadataImportService metadataImportService;

    @Autowired
    private ContextService contextService;

    @Autowired
    private RenderService renderService;

    @Autowired
    private SchemaService schemaService;

    @Autowired
    private WebMessageService webMessageService;

    @Autowired
    private CsvImportService csvImportService;

//    @Autowired
//    private GmlImportService gmlImportService;

    @Autowired
    private SchedulingManager schedulingManager;

    @Autowired
    private MetadataExportService metadataExportService;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private UserSettingService userSettingService;

    @Autowired
    private ObjectFactory<MetadataAsyncImporter> metadataAsyncImporterFactory;

//    @Autowired
//    private ObjectFactory<GmlAsyncImporter> gmlAsyncImporterFactory;

    @PostMapping( value = "", consumes = MediaType.APPLICATION_JSON_VALUE )
    public void postJsonMetadata( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        MetadataImportParams params = metadataImportService.getParamsFromMap( contextService.getParameterValuesMap() );

        final Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> objects =
            renderService.fromMetadata( StreamUtils.wrapAndCheckCompressionFormat( request.getInputStream() ), RenderFormat.JSON );
        params.setObjects( objects );

        response.setContentType( MediaType.APPLICATION_JSON_VALUE );

        if ( params.hasJobId() )
        {
            startAsyncMetadata( params, request, response );
        }
        else
        {
            ImportReport importReport = metadataImportService.importMetadata( params );
            renderService.toJson( response.getOutputStream(), importReport );
        }
    }

    @PostMapping( value = "", consumes = "application/csv" )
    public void postCsvMetadata( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        MetadataImportParams params = metadataImportService.getParamsFromMap( contextService.getParameterValuesMap() );

        String classKey = request.getParameter( "classKey" );

        if ( StringUtils.isEmpty( classKey ) || !CsvImportClass.classExists( classKey ) )
        {
            webMessageService.send( WebMessageUtils.conflict( "Cannot find Csv import class:  " + classKey ), response, request );
            return;
        }

        params.setCsvImportClass( CsvImportClass.valueOf( classKey ) );

        Metadata metadata = csvImportService.fromCsv( request.getInputStream(), new CsvImportOptions()
            .setImportClass( params.getCsvImportClass() )
            .setFirstRowIsHeader( params.isFirstRowIsHeader() ) );

        params.addMetadata( schemaService.getMetadataSchemas(), metadata );

        if ( params.hasJobId() )
        {
            startAsyncMetadata( params, request, response );
        }
        else
        {
            ImportReport importReport = metadataImportService.importMetadata( params );
            renderService.toJson( response.getOutputStream(), importReport );
        }
    }

//    @PostMapping( value = "/gml", consumes = MediaType.APPLICATION_XML_VALUE )
//    public void postGmlMetadata( HttpServletRequest request, HttpServletResponse response ) throws IOException
//    {
//        MetadataImportParams params = metadataImportService.getParamsFromMap( contextService.getParameterValuesMap() );
//
//        if ( params.hasJobId() )
//        {
//            startAsyncGml( params, request, response );
//        }
//        else
//        {
//            ImportReport importReport = gmlImportService.importGml( request.getInputStream(), params );
//            renderService.toJson( response.getOutputStream(), importReport );
//        }
//    }

    @PostMapping( value = "", consumes = MediaType.APPLICATION_XML_VALUE )
    public void postXmlMetadata( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        MetadataImportParams params = metadataImportService.getParamsFromMap( contextService.getParameterValuesMap() );
        Metadata metadata = renderService.fromXml( StreamUtils.wrapAndCheckCompressionFormat( request.getInputStream() ), Metadata.class );
        params.addMetadata( schemaService.getMetadataSchemas(), metadata );
        response.setContentType( MediaType.APPLICATION_XML_VALUE );

        if ( params.hasJobId() )
        {
            startAsyncMetadata( params, request, response );
        }
        else
        {
            ImportReport importReport = metadataImportService.importMetadata( params );
            renderService.toXml( response.getOutputStream(), importReport );
        }
    }

    @GetMapping( "/csvImportClasses" )
    public @ResponseBody List<CsvImportClass> getCsvImportClasses()
    {
        return Arrays.asList( CsvImportClass.values() );
    }

    @GetMapping
    public ResponseEntity<RootNode> getMetadata(
        @RequestParam( required = false, defaultValue = "false" ) boolean translate,
        @RequestParam( required = false ) String locale,
        @RequestParam( required = false, defaultValue = "false" ) boolean download )
    {
        if ( translate )
        {
            TranslateParams translateParams = new TranslateParams( true, locale );
            setUserContext( currentUserService.getCurrentUser(), translateParams );
        }

        MetadataExportParams params = metadataExportService.getParamsFromMap( contextService.getParameterValuesMap() );
        metadataExportService.validate( params );

        RootNode rootNode = metadataExportService.getMetadataAsNode( params );

        return MetadataExportControllerUtils.createResponseEntity( rootNode, download );
    }

    //----------------------------------------------------------------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------------------------------------------------------------

    private void startAsyncMetadata( MetadataImportParams params, HttpServletRequest request, HttpServletResponse response )
    {
        MetadataAsyncImporter metadataImporter = metadataAsyncImporterFactory.getObject();
        metadataImporter.setParams( params );
        schedulingManager.executeJob( metadataImporter );

        response.setHeader( "Location", ContextUtils.getRootPath( request ) + "/system/tasks/" + METADATA_IMPORT );
        webMessageService.send( jobConfigurationReport( params.getId() ), response, request );
    }

//    private void startAsyncGml( MetadataImportParams params, HttpServletRequest request, HttpServletResponse response ) throws IOException
//    {
//        GmlAsyncImporter gmlImporter = gmlAsyncImporterFactory.getObject();
//        gmlImporter.setInputStream( request.getInputStream() );
//        gmlImporter.setParams( params );
//        schedulingManager.executeJob( gmlImporter );
//
//        response.setHeader( "Location", ContextUtils.getRootPath( request ) + "/system/tasks/" + GML_IMPORT );
//        webMessageService.send( jobConfigurationReport( params.getId() ), response, request );
//    }

    private void setUserContext( User user, TranslateParams translateParams )
    {
        Locale dbLocale = getLocaleWithDefault( translateParams );
        UserContext.setUser( user );
        UserContext.setUserSetting( UserSettingKey.DB_LOCALE, dbLocale );
    }

    private Locale getLocaleWithDefault( TranslateParams translateParams )
    {
        return translateParams.isTranslate() ?
            translateParams.getLocaleWithDefault( (Locale) userSettingService.getUserSetting( UserSettingKey.DB_LOCALE ) ) : null;
    }
}