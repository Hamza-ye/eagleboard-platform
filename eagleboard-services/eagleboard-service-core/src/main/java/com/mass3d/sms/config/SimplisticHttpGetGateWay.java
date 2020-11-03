package com.mass3d.sms.config;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import com.mass3d.outboundmessage.OutboundMessageBatch;
import com.mass3d.outboundmessage.OutboundMessageResponse;
import com.mass3d.sms.outbound.GatewayResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component( "com.mass3d.sms.config.SimplisticHttpGetGateWay" )
public class SimplisticHttpGetGateWay
    extends SmsGateway
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final RestTemplate restTemplate;

    public SimplisticHttpGetGateWay( RestTemplate restTemplate )
    {
        checkNotNull( restTemplate );
        this.restTemplate = restTemplate;
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public boolean accept( SmsGatewayConfig gatewayConfig )
    {
        return gatewayConfig instanceof GenericHttpGatewayConfig;
    }

    @Override
    public List<OutboundMessageResponse> sendBatch( OutboundMessageBatch batch, SmsGatewayConfig gatewayConfig )
    {
        return batch.getMessages()
            .parallelStream()
            .map( m -> send( m.getSubject(), m.getText(), m.getRecipients(), gatewayConfig ) )
            .collect( Collectors.toList() );
    }

    @Override
    public OutboundMessageResponse send( String subject, String text, Set<String> recipients, SmsGatewayConfig config )
    {
        GenericHttpGatewayConfig genericConfig = (GenericHttpGatewayConfig) config;

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl( config.getUrlTemplate() );

        ResponseEntity<String> responseEntity = null;

        HttpEntity<String> requestEntity = null;

        URI uri;

        try
        {
            if ( genericConfig.isSendUrlParameters() )
            {
                uri = uriBuilder.buildAndExpand( getValueStore( genericConfig, text, recipients ) ).encode().toUri();

                requestEntity = new HttpEntity<>( null, getHeaderParameters( genericConfig ) );
            }
            else
            {
                uri = uriBuilder.build().encode().toUri();

                requestEntity = getRequestEntity( genericConfig, text, recipients );
            }

            responseEntity = restTemplate.exchange( uri, genericConfig.isUseGet() ? HttpMethod.GET : HttpMethod.POST, requestEntity, String.class );
        }
        catch ( HttpClientErrorException ex )
        {
            log.error( "Client error " + ex.getMessage() );
        }
        catch ( HttpServerErrorException ex )
        {
            log.error( "Server error " + ex.getMessage() );
        }
        catch ( Exception ex )
        {
            log.error( "Error " + ex.getMessage() );
        }

        return getResponse( responseEntity );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private HttpEntity<String> getRequestEntity( GenericHttpGatewayConfig config, String text, Set<String> recipients )
    {
        Map<String, String> valueStore = getValueStore( config, text, recipients );

        final StringSubstitutor substitutor = new StringSubstitutor( valueStore ); // Matches on ${...}

        String data = substitutor.replace( config.getConfigurationTemplate() );

        return new HttpEntity<>( data, getHeaderParameters( config ) );
    }

    private HttpHeaders getHeaderParameters( GenericHttpGatewayConfig config )
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put( "Content-type", Collections.singletonList( config.getContentType().getValue() ) );

        for ( GenericGatewayParameter parameter : config.getParameters() )
        {
            if ( parameter.isHeader() )
            {
                httpHeaders.put(parameter.getKey(), Collections.singletonList( parameter.getDisplayValue() ) );
            }
        }

        return httpHeaders;
    }

    private Map<String, String> getValueStore( GenericHttpGatewayConfig config, String text, Set<String> recipients )
    {
        List<GenericGatewayParameter> parameters = config.getParameters();

        Map<String, String> valueStore = new HashMap<>();

        for ( GenericGatewayParameter parameter : parameters )
        {
            if ( !parameter.isHeader() )
            {
                valueStore.put( parameter.getKey(), parameter.getDisplayValue() );
            }
        }

        valueStore.put( KEY_TEXT, text );
        valueStore.put( KEY_RECIPIENT, StringUtils.join( recipients, "," ) );

        return valueStore;
    }

    private OutboundMessageResponse getResponse( ResponseEntity<String> responseEntity )
    {
        OutboundMessageResponse status = new OutboundMessageResponse();

        if ( responseEntity == null || !OK_CODES.contains( responseEntity.getStatusCode() ) )
        {
            status.setResponseObject( GatewayResponse.FAILED );
            status.setOk( false );

            return status;
        }

        log.info( responseEntity.getBody() );
        return wrapHttpStatus( responseEntity.getStatusCode() );
    }
}
