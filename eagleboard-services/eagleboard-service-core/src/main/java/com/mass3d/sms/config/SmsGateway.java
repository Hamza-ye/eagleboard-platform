package com.mass3d.sms.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.outboundmessage.OutboundMessageBatch;
import com.mass3d.outboundmessage.OutboundMessageResponse;
import com.mass3d.sms.outbound.GatewayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
public abstract class SmsGateway
{
    protected static final String PROTOCOL_VERSION = "X-Version";
    protected static final String MAX_MESSAGE_PART = "?maxMessageParts=4";
    protected static final String BASIC = " Basic ";
    public static final String KEY_TEXT = "text";
    public static final String KEY_RECIPIENT = "recipients";

    public static final Set<HttpStatus> OK_CODES = ImmutableSet.of( HttpStatus.OK,
        HttpStatus.ACCEPTED, HttpStatus.CREATED );

    private static final ImmutableMap<HttpStatus, GatewayResponse> GATEWAY_RESPONSE_MAP = new ImmutableMap.Builder<HttpStatus, GatewayResponse>()
        .put( HttpStatus.OK, GatewayResponse.RESULT_CODE_200 )
        .put( HttpStatus.ACCEPTED, GatewayResponse.RESULT_CODE_202 )
        .put( HttpStatus.CREATED, GatewayResponse.RESULT_CODE_202 )
        .put( HttpStatus.MULTI_STATUS, GatewayResponse.RESULT_CODE_207 )
        .put( HttpStatus.BAD_REQUEST, GatewayResponse.RESULT_CODE_400 )
        .put( HttpStatus.UNAUTHORIZED, GatewayResponse.RESULT_CODE_401 )
        .put( HttpStatus.PAYMENT_REQUIRED, GatewayResponse.RESULT_CODE_402 )
        .put( HttpStatus.NOT_FOUND, GatewayResponse.RESULT_CODE_404 )
        .put( HttpStatus.METHOD_NOT_ALLOWED, GatewayResponse.RESULT_CODE_405 )
        .put( HttpStatus.GONE, GatewayResponse.RESULT_CODE_410 )
        .put( HttpStatus.SERVICE_UNAVAILABLE, GatewayResponse.RESULT_CODE_503 )
        .put( HttpStatus.FORBIDDEN, GatewayResponse.RESULT_CODE_403 )
        .put( HttpStatus.INTERNAL_SERVER_ERROR, GatewayResponse.RESULT_CODE_504 ).build();

    @Autowired
    private RestTemplate restTemplate;

    protected abstract List<OutboundMessageResponse> sendBatch( OutboundMessageBatch batch, SmsGatewayConfig gatewayConfig );

    protected abstract boolean accept( SmsGatewayConfig gatewayConfig );

    protected abstract OutboundMessageResponse send( String subject, String text, Set<String> recipients, SmsGatewayConfig gatewayConfig );

    public HttpStatus send( String urlTemplate, HttpEntity<?> request, HttpMethod httpMethod, Class<?> klass )
    {
        ResponseEntity<?> response;
        HttpStatus statusCode;

        try
        {
            response = restTemplate.exchange( urlTemplate, httpMethod, request, klass );

            if ( response != null )
            {
                statusCode = response.getStatusCode();
            }
            else
            {
                log.error( "Server response is null" );
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        catch ( HttpClientErrorException ex )
        {
            log.error( "Client error", ex );

            statusCode = ex.getStatusCode();
        }
        catch ( HttpServerErrorException ex )
        {
            log.error( "Server error", ex );

            statusCode = ex.getStatusCode();
        }
        catch ( Exception ex )
        {
            log.error( "Error", ex );

            statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        log.info( "Response status code: " + statusCode );

        return statusCode;
    }

    public OutboundMessageResponse wrapHttpStatus( HttpStatus httpStatus )
    {
        GatewayResponse gatewayResponse;

        OutboundMessageResponse status = new OutboundMessageResponse();

        if ( OK_CODES.contains( httpStatus ) )
        {
            gatewayResponse = GATEWAY_RESPONSE_MAP.get( httpStatus );

            status.setOk( true );
        }
        else
        {
            gatewayResponse = GATEWAY_RESPONSE_MAP.getOrDefault( httpStatus, GatewayResponse.FAILED );

            status.setOk( false );
        }

        status.setResponseObject( gatewayResponse );
        status.setDescription( gatewayResponse.getResponseMessage() );

        return status;
    }
}
