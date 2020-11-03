package com.mass3d.sms.config;

import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.mass3d.outboundmessage.OutboundMessageBatch;
import com.mass3d.outboundmessage.OutboundMessageResponse;
import com.mass3d.sms.outbound.BulkSmsRequestEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.sms.config.BulkSmsGateway" )
public class BulkSmsHttpGateway
    extends SmsGateway
{
    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public boolean accept( SmsGatewayConfig gatewayConfig )
    {
        return gatewayConfig instanceof BulkSmsGatewayConfig;
    }

    @Override
    public List<OutboundMessageResponse> sendBatch( OutboundMessageBatch smsBatch, SmsGatewayConfig config )
    {
        return smsBatch.getMessages().stream()
            .map( m -> send( m.getSubject(), m.getText(), m.getRecipients(), config ) )
            .collect( Collectors.toList() );
    }

    @Override
    public OutboundMessageResponse send( String subject, String text, Set<String> recipients, SmsGatewayConfig config )
    {
        BulkSmsGatewayConfig bulkSmsGatewayConfig = (BulkSmsGatewayConfig) config;

        HttpEntity<BulkSmsRequestEntity> request =
            new HttpEntity<>( new BulkSmsRequestEntity( text, recipients ), getRequestHeaderParameters( bulkSmsGatewayConfig ) );

        HttpStatus httpStatus = send( bulkSmsGatewayConfig.getUrlTemplate(), request, HttpMethod.POST, String.class );

        return wrapHttpStatus( httpStatus );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private HttpHeaders getRequestHeaderParameters( BulkSmsGatewayConfig bulkSmsGatewayConfig )
    {
        String credentials = bulkSmsGatewayConfig.getUsername().trim() + ":" + bulkSmsGatewayConfig.getPassword().trim();
        String encodedCredentials = Base64.getEncoder().encodeToString( credentials.getBytes() );

        HttpHeaders headers = new HttpHeaders();
        headers.set( HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE );
        headers.set( HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE );
        headers.set( HttpHeaders.AUTHORIZATION, BASIC + encodedCredentials );

        return headers;
    }
}
