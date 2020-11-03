package com.mass3d.sms.config;

import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import com.mass3d.sms.config.views.SmsConfigurationViews;

@XmlRootElement( name = "smsConfiguration" )
public class SmsConfiguration
    implements Serializable
{
    private static final long serialVersionUID = 7460688383539123303L;

    private List<SmsGatewayConfig> gateways = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public SmsConfiguration()
    {
        this.gateways = new ArrayList<>();
    }

    public SmsConfiguration( boolean enabled )
    {
        this.gateways = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // Getter && Setter
    // -------------------------------------------------------------------------

    @JsonView( SmsConfigurationViews.Public.class )
    public List<SmsGatewayConfig> getGateways()
    {
        return gateways;
    }

    public void setGateways( List<SmsGatewayConfig> gateways )
    {
        this.gateways = gateways;
    }
}
