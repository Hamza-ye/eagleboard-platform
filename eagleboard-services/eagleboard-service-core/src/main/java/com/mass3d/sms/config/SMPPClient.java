package com.mass3d.sms.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.outboundmessage.OutboundMessage;
import com.mass3d.outboundmessage.OutboundMessageBatch;
import com.mass3d.outboundmessage.OutboundMessageResponse;
import com.mass3d.sms.outbound.GatewayResponse;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.Address;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceIfPresentFlag;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.SubmitMultiResult;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.DeliveryReceiptState;
import org.jsmpp.util.TimeFormatter;
import org.springframework.stereotype.Component;

@Slf4j
@Component( "com.mass3d.sms.config.SMPPClient" )
public class SMPPClient
{
    private static final String SOURCE = "DHIS2";
    private static final String SENDING_FAILED = "SMS sending failed";
    private static final String SESSION_ERROR = "SMPP Session cannot be null";

    private static final TimeFormatter TIME_FORMATTER = new AbsoluteTimeFormatter();

    public OutboundMessageResponse send( String text, Set<String> recipients, SMPPGatewayConfig config )
    {
        SMPPSession session = start( config );

        if ( session == null )
        {
            return new OutboundMessageResponse( SESSION_ERROR, GatewayResponse.SMPP_SESSION_FAILURE, false );
        }

        OutboundMessageResponse response = send( session, text, recipients, config );

        stop( session );

        return response;
    }

    public List<OutboundMessageResponse> sendBatch( OutboundMessageBatch batch, SMPPGatewayConfig config )
    {
        SMPPSession session = start( config );

        if ( session == null )
        {
            return Collections.emptyList();
        }

        List<OutboundMessageResponse> responses = new ArrayList<>();

        for ( OutboundMessage message : batch.getMessages() )
        {
            OutboundMessageResponse response =  send( session, message.getText(), message.getRecipients(), config );

            responses.add( response );
        }

        stop( session );

        return responses;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private OutboundMessageResponse send( SMPPSession session, String text, Set<String> recipients, SMPPGatewayConfig config )
    {
        OutboundMessageResponse response = new OutboundMessageResponse();
        SubmitMultiResult result = null;
        try
        {
            result = session.submitMultiple( config.getSystemType(), config.getTypeOfNumber(), config.getNumberPlanIndicator(), SOURCE, getAddresses( recipients ), new ESMClass(), (byte) 0, (byte) 1, TIME_FORMATTER.format( new Date() ), null,
                new RegisteredDelivery( SMSCDeliveryReceipt.SUCCESS_FAILURE ), ReplaceIfPresentFlag.DEFAULT, new GeneralDataCoding( Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, config.isCompressed() ), (byte) 0,
                text.getBytes() );

            log.info( String.format( "Messages submitted, result is %s", result.getMessageId() ) );
        }
        catch ( PDUException e )
        {
            log.error( "Invalid PDU parameter", e );
        }
        catch ( ResponseTimeoutException e )
        {
            log.error( "Response timeout", e );
        }
        catch ( InvalidResponseException e )
        {
            log.error( "Receive invalid response", e );
        }
        catch ( NegativeResponseException e )
        {
            log.error( "Receive negative response", e );
        }
        catch ( IOException e )
        {
            log.error( "I/O error", e );
        }
        catch ( Exception e )
        {
            log.error( "Exception in submitting SMPP request", e );
        }

        if ( result != null )
        {
            if ( result.getUnsuccessDeliveries() == null || result.getUnsuccessDeliveries().length == 0 )
            {
                log.info( "Message pushed to broker successfully" );
                response.setOk( true );
                response.setDescription( result.getMessageId() );
                response.setResponseObject( GatewayResponse.RESULT_CODE_0 );
            }
            else
            {
                String failureCause = DeliveryReceiptState.valueOf( result.getUnsuccessDeliveries()[0].getErrorStatusCode() ) + " - " + result.getMessageId();

                log.error( failureCause );
                response.setDescription( failureCause );
                response.setResponseObject( GatewayResponse.FAILED );
            }
        }
        else
        {
            response.setDescription( SENDING_FAILED );
            response.setResponseObject( GatewayResponse.FAILED );
        }

        return response;
    }

    private void stop( SMPPSession session )
    {
        if( session != null )
        {
            session.unbindAndClose();
        }
    }

    private SMPPSession start( SMPPGatewayConfig config )
    {
        try
        {
            SMPPSession session = new SMPPSession( config.getUrlTemplate(), config.getPort(), getBindParameters( config ) );

            log.info( "SMPP client connected to SMSC: " + config.getUrlTemplate() );
            return session;
        }
        catch ( IOException e )
        {
            log.error( "I/O error occured", e );
        }

        return null;
    }

    private BindParameter getBindParameters( SMPPGatewayConfig config )
    {
       return new BindParameter( config.getBindType(), config.getUsername(), config.getPassword(), config.getSystemType(),
            config.getTypeOfNumber(), config.getNumberPlanIndicator(), null );
    }

    private Address[] getAddresses( Set<String> recipients )
    {
        Address[] addresses = new Address[recipients.size()];
        int i = 0;

        for( String number : recipients )
        {
            addresses[i] = new Address( TypeOfNumber.NATIONAL, NumberingPlanIndicator.UNKNOWN, number );
            i++;
        }

        return addresses;
    }
}
