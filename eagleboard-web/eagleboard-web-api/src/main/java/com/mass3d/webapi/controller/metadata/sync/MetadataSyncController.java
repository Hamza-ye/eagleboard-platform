package com.mass3d.webapi.controller.metadata.sync;

import com.mass3d.common.DhisApiVersion;
import com.mass3d.dxf2.metadata.feedback.ImportReport;
import com.mass3d.dxf2.metadata.sync.MetadataSyncParams;
import com.mass3d.dxf2.metadata.sync.MetadataSyncService;
import com.mass3d.dxf2.metadata.sync.MetadataSyncSummary;
import com.mass3d.dxf2.metadata.sync.exception.DhisVersionMismatchException;
import com.mass3d.dxf2.metadata.sync.exception.MetadataSyncImportException;
import com.mass3d.dxf2.metadata.sync.exception.MetadataSyncServiceException;
import com.mass3d.dxf2.metadata.sync.exception.RemoteServerUnavailableException;
import com.mass3d.dxf2.webmessage.WebMessageResponse;
import com.mass3d.feedback.Status;
import com.mass3d.webapi.controller.CrudControllerAdvice;
import com.mass3d.webapi.controller.exception.BadRequestException;
import com.mass3d.webapi.controller.exception.MetadataImportConflictException;
import com.mass3d.webapi.controller.exception.MetadataSyncException;
import com.mass3d.webapi.controller.exception.OperationNotAllowedException;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import com.mass3d.webapi.service.ContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller for the automated sync of the metadata
 *
 */

@RestController
@RequestMapping( "/metadata/sync" )
@ApiVersion( { DhisApiVersion.DEFAULT, DhisApiVersion.ALL } )
public class MetadataSyncController
    extends CrudControllerAdvice
{
    @Autowired
    private ContextService contextService;

    @Autowired
    private MetadataSyncService metadataSyncService;

    @PreAuthorize( "hasRole('ALL') or hasRole('F_METADATA_MANAGE')" )
    @GetMapping
    public ResponseEntity<? extends WebMessageResponse> metadataSync(HttpServletRequest request, HttpServletResponse response)
        throws MetadataSyncException, BadRequestException, MetadataImportConflictException, OperationNotAllowedException
    {
        MetadataSyncParams syncParams;
        MetadataSyncSummary metadataSyncSummary = null;

        synchronized ( metadataSyncService )
        {
            try
            {
                syncParams = metadataSyncService.getParamsFromMap( contextService.getParameterValuesMap() );
            }
            catch ( RemoteServerUnavailableException exception )
            {
                throw new MetadataSyncException( exception.getMessage(), exception );

            }
            catch ( MetadataSyncServiceException serviceException )
            {
                throw new BadRequestException( "Error in parsing inputParams " + serviceException.getMessage(), serviceException );
            }

            try
            {
                boolean isSyncRequired = metadataSyncService.isSyncRequired(syncParams);

                if ( isSyncRequired )
                {
                    metadataSyncSummary = metadataSyncService.doMetadataSync( syncParams );
                    validateSyncSummaryResponse(metadataSyncSummary);
                }
                else
                {
                    throw new MetadataImportConflictException( "Version already exists in system and hence not starting the sync." );
                }
            }

            catch ( MetadataSyncImportException importerException )
            {
                throw new MetadataSyncException( "Runtime exception occurred while doing import: " + importerException.getMessage() );
            }
            catch ( MetadataSyncServiceException serviceException )
            {
                throw new MetadataSyncException( "Exception occurred while doing metadata sync: " + serviceException.getMessage() );
            }
            catch ( DhisVersionMismatchException versionMismatchException )
            {
                throw new OperationNotAllowedException( "Exception occurred while doing metadata sync: " + versionMismatchException.getMessage() );
            }
        }

        return new ResponseEntity<MetadataSyncSummary>( metadataSyncSummary, HttpStatus.OK );
    }

    private void validateSyncSummaryResponse( MetadataSyncSummary metadataSyncSummary )
        throws MetadataImportConflictException
    {
        ImportReport importReport = metadataSyncSummary.getImportReport();
        if ( importReport.getStatus() != Status.OK )
        {
            throw new MetadataImportConflictException( metadataSyncSummary );
        }
    }
}
