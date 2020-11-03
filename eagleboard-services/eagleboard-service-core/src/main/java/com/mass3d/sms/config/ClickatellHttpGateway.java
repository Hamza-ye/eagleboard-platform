package com.mass3d.sms.config;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.mass3d.outboundmessage.OutboundMessageBatch;
import com.mass3d.outboundmessage.OutboundMessageResponse;
import com.mass3d.sms.outbound.ClickatellRequestEntity;
import com.mass3d.sms.outbound.ClickatellResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.sms.config.ClickatellGateway" )
public class ClickatellHttpGateway
    extends SmsGateway
{
    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public boolean accept( SmsGatewayConfig gatewayConfig )
    {
        return gatewayConfig instanceof ClickatellGatewayConfig;
    }

    @Override
    public List<OutboundMessageResponse> sendBatch( OutboundMessageBatch batch, SmsGatewayConfig config )
    {
        return batch.getMessages()
            .parallelStream()
            .map( m -> send( m.getSubject(), m.getText(), m.getRecipients(), config ) )
            .collect( Collectors.toList() );
    }

    @Override
    public OutboundMessageResponse send( String subject, String text, Set<String> recipients, SmsGatewayConfig config )
    {
        ClickatellGatewayConfig clickatellConfiguration = (ClickatellGatewayConfig) config;
        HttpEntity<ClickatellRequestEntity> request =
            new HttpEntity<>( getRequestBody( text, recipients ), getRequestHeaderParameters( clickatellConfiguration ) );

        HttpStatus httpStatus = send( clickatellConfiguration.getUrlTemplate() + MAX_MESSAGE_PART, request, HttpMethod.POST ,ClickatellResponseEntity.class );

        return wrapHttpStatus( httpStatus );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private ClickatellRequestEntity getRequestBody( String text, Set<String> recipients )
    {
        ClickatellRequestEntity requestBody = new ClickatellRequestEntity();
        requestBody.setContent( text );
        requestBody.setTo( recipients );

        return requestBody;
    }

    private HttpHeaders getRequestHeaderParameters( ClickatellGatewayConfig clickatellConfiguration )
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set( HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE );
        headers.set( HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE );
        headers.set( PROTOCOL_VERSION, "1" );
        headers.set( HttpHeaders.AUTHORIZATION, clickatellConfiguration.getAuthToken() );

        return headers;
    }
}
