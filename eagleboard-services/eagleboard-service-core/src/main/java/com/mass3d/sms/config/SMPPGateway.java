package com.mass3d.sms.config;

import java.util.List;
import java.util.Set;
import com.mass3d.outboundmessage.OutboundMessageBatch;
import com.mass3d.outboundmessage.OutboundMessageResponse;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.sms.config.SMPPGateway" )
public class SMPPGateway extends SmsGateway
{
    private final SMPPClient smppClient;

    public SMPPGateway( SMPPClient smppClient )
    {
        this.smppClient = smppClient;
    }

    @Override
    public boolean accept( SmsGatewayConfig gatewayConfig )
    {
        return gatewayConfig instanceof SMPPGatewayConfig;
    }

    @Override
    public OutboundMessageResponse send( String subject, String text, Set<String> recipients, SmsGatewayConfig gatewayConfig )
    {
        SMPPGatewayConfig config = (SMPPGatewayConfig) gatewayConfig;

        return smppClient.send( text, recipients, config );
    }

    @Override
    public List<OutboundMessageResponse> sendBatch( OutboundMessageBatch batch, SmsGatewayConfig gatewayConfig )
    {
        SMPPGatewayConfig config = (SMPPGatewayConfig) gatewayConfig;

        return smppClient.sendBatch( batch, config );
    }
}
