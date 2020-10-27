package com.mass3d.dxf2.webmessage;

import java.util.List;
import com.mass3d.dxf2.importsummary.ImportStatus;
import com.mass3d.dxf2.importsummary.ImportSummaries;
import com.mass3d.dxf2.importsummary.ImportSummary;
import com.mass3d.dxf2.metadata.feedback.ImportReport;
import com.mass3d.dxf2.scheduling.JobConfigurationWebMessageResponse;
import com.mass3d.dxf2.webmessage.responses.ErrorReportsWebMessageResponse;
import com.mass3d.dxf2.webmessage.responses.ImportReportWebMessageResponse;
import com.mass3d.dxf2.webmessage.responses.ObjectReportWebMessageResponse;
import com.mass3d.dxf2.webmessage.responses.TypeReportWebMessageResponse;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.feedback.ObjectReport;
import com.mass3d.feedback.Status;
import com.mass3d.feedback.TypeReport;
import com.mass3d.scheduling.JobConfiguration;
import org.springframework.http.HttpStatus;

public final class WebMessageUtils
{
    public static WebMessage createWebMessage( String message, Status status, HttpStatus httpStatus )
    {
        WebMessage webMessage = new WebMessage( status, httpStatus );
        webMessage.setMessage( message );

        return webMessage;
    }

    public static WebMessage createWebMessage( String message, Status status, HttpStatus httpStatus, ErrorCode errorCode )
    {
        WebMessage webMessage = new WebMessage( status, httpStatus );
        webMessage.setErrorCode( errorCode );
        webMessage.setMessage( message );

        return webMessage;
    }

    public static WebMessage createWebMessage( String message, String devMessage, Status status, HttpStatus httpStatus )
    {
        WebMessage webMessage = new WebMessage( status, httpStatus );
        webMessage.setMessage( message );
        webMessage.setDevMessage( devMessage );

        return webMessage;
    }

    public static WebMessage ok( String message )
    {
        return createWebMessage( message, Status.OK, HttpStatus.OK );
    }

    public static WebMessage ok( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.OK, HttpStatus.OK );
    }

    public static WebMessage created( String message )
    {
        return createWebMessage( message, Status.OK, HttpStatus.CREATED );
    }

    public static WebMessage created( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.OK, HttpStatus.CREATED );
    }

    public static WebMessage notFound( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.NOT_FOUND );
    }

    public static WebMessage notFound( Class<?> klass, String id )
    {
        String message = klass.getSimpleName() + " with id " + id + " could not be found.";
        return createWebMessage( message, Status.ERROR, HttpStatus.NOT_FOUND );
    }

    public static WebMessage notFound( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.NOT_FOUND );
    }

    public static WebMessage conflict( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.CONFLICT );
    }

    public static WebMessage conflict( String message, ErrorCode errorCode )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.CONFLICT, errorCode );
    }

    public static WebMessage conflict( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.CONFLICT );
    }

    public static WebMessage error( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    public static WebMessage error( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    public static WebMessage badRequest( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.BAD_REQUEST );
    }

    public static WebMessage badRequest( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.BAD_REQUEST );
    }

    public static WebMessage forbidden( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.FORBIDDEN );
    }

    public static WebMessage forbidden( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.FORBIDDEN );
    }

    public static WebMessage serviceUnavailable( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.SERVICE_UNAVAILABLE );
    }

    public static WebMessage serviceUnavailable( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.SERVICE_UNAVAILABLE );
    }

    public static WebMessage unprocessableEntity( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.UNPROCESSABLE_ENTITY );
    }

    public static WebMessage unprocessableEntity( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.UNPROCESSABLE_ENTITY );
    }

    public static WebMessage unathorized( String message )
    {
        return createWebMessage( message, Status.ERROR, HttpStatus.UNAUTHORIZED );
    }

    public static WebMessage unathorized( String message, String devMessage )
    {
        return createWebMessage( message, devMessage, Status.ERROR, HttpStatus.UNAUTHORIZED );
    }

    public static WebMessage importSummary( ImportSummary importSummary )
    {
        WebMessage webMessage = new WebMessage();

        if ( importSummary.isStatus( ImportStatus.ERROR ) )
        {
            webMessage.setMessage( "An error occurred, please check import summary." );
            webMessage.setStatus( Status.ERROR );
            webMessage.setHttpStatus( HttpStatus.CONFLICT );
        }
        else if ( importSummary.isStatus( ImportStatus.WARNING ) )
        {
            webMessage.setMessage( "One more conflicts encountered, please check import summary." );
            webMessage.setStatus( Status.WARNING );
            webMessage.setHttpStatus( HttpStatus.CONFLICT );
        }
        else
        {
            webMessage.setMessage( "Import was successful." );
            webMessage.setStatus( Status.OK );
            webMessage.setHttpStatus( HttpStatus.OK );
        }

        webMessage.setResponse( importSummary );

        return webMessage;
    }

    public static WebMessage importSummaries( ImportSummaries importSummaries )
    {
        WebMessage webMessage = new WebMessage();

        if ( importSummaries.isStatus( ImportStatus.ERROR ) )
        {
            webMessage.setMessage( "An error occurred, please check import summary." );
            webMessage.setStatus( Status.ERROR );
            webMessage.setHttpStatus( HttpStatus.CONFLICT );
        }
        else if ( importSummaries.isStatus( ImportStatus.WARNING ) )
        {
            webMessage.setMessage( "One or more conflicts encountered, please check import summary." );
            webMessage.setStatus( Status.WARNING );
            webMessage.setHttpStatus( HttpStatus.CONFLICT );
        }
        else
        {
            webMessage.setMessage( "Import was successful." );
            webMessage.setStatus( Status.OK );
            webMessage.setHttpStatus( HttpStatus.OK );
        }

        webMessage.setResponse( importSummaries );

        return webMessage;
    }

    public static WebMessage importReport( ImportReport importReport )
    {
        WebMessage webMessage = new WebMessage();
        webMessage.setResponse( new ImportReportWebMessageResponse( importReport ) );

        webMessage.setStatus( importReport.getStatus() );

        if ( webMessage.getStatus() != Status.OK )
        {
            webMessage.setMessage( "One more more errors occurred, please see full details in import report." );
            webMessage.setStatus( Status.WARNING );
            webMessage.setHttpStatus( HttpStatus.CONFLICT );
        }

        return webMessage;
    }

    public static WebMessage typeReport( TypeReport typeReport )
    {
        WebMessage webMessage = new WebMessage();
        webMessage.setResponse( new TypeReportWebMessageResponse( typeReport ) );

        if ( typeReport.getErrorReports().isEmpty() )
        {
            webMessage.setStatus( Status.OK );
            webMessage.setHttpStatus( HttpStatus.OK );
        }
        else
        {
            webMessage.setMessage( "One more more errors occurred, please see full details in import report." );
            webMessage.setStatus( Status.ERROR );
            webMessage.setHttpStatus( HttpStatus.CONFLICT );
        }

        return webMessage;
    }

    public static WebMessage objectReport( ImportReport importReport )
    {
        WebMessage webMessage = new WebMessage( Status.OK, HttpStatus.OK );

        if ( !importReport.getTypeReports().isEmpty() )
        {
            TypeReport typeReport = importReport.getTypeReports().get( 0 );

            if ( !typeReport.getObjectReports().isEmpty() )
            {
                return objectReport( typeReport.getObjectReports().get( 0 ) );
            }
        }

        return webMessage;
    }

    public static WebMessage objectReport( ObjectReport objectReport )
    {
        WebMessage webMessage = new WebMessage();
        webMessage.setResponse( new ObjectReportWebMessageResponse( objectReport ) );

        if ( objectReport.isEmpty() )
        {
            webMessage.setStatus( Status.OK );
            webMessage.setHttpStatus( HttpStatus.OK );
        }
        else
        {
            webMessage.setMessage( "One more more errors occurred, please see full details in import report." );
            webMessage.setStatus( Status.WARNING );
            webMessage.setHttpStatus( HttpStatus.CONFLICT );
        }

        return webMessage;
    }

    public static WebMessage jobConfigurationReport( JobConfiguration jobConfiguration )
    {
        WebMessage webMessage = WebMessageUtils.ok( "Initiated " + jobConfiguration.getName() );
        webMessage.setResponse( new JobConfigurationWebMessageResponse( jobConfiguration ) );

        return webMessage;
    }

    public static WebMessage errorReports( List<ErrorReport> errorReports )
    {
        WebMessage webMessage = new WebMessage();
        webMessage.setResponse( new ErrorReportsWebMessageResponse( errorReports ) );

        if ( !errorReports.isEmpty() )
        {
            webMessage.setStatus( Status.ERROR );
            webMessage.setHttpStatus( HttpStatus.BAD_REQUEST );
        }

        return webMessage;
    }

    private WebMessageUtils()
    {
    }
}
