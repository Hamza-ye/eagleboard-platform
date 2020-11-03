package com.mass3d.sms.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonView;
import com.mass3d.sms.config.views.SmsConfigurationViews;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;

@JsonTypeName( "smpp" )
public class SMPPGatewayConfig extends SmsGatewayConfig
{
    @JsonView( SmsConfigurationViews.Public.class )
    private String systemType;

    @JsonView( SmsConfigurationViews.Public.class )
    private NumberingPlanIndicator numberPlanIndicator = NumberingPlanIndicator.UNKNOWN;

    @JsonView( SmsConfigurationViews.Public.class )
    private TypeOfNumber typeOfNumber = TypeOfNumber.UNKNOWN;

    @JsonView( SmsConfigurationViews.Public.class )
    private BindType bindType = BindType.BIND_TX;

    @JsonView( SmsConfigurationViews.Public.class )
    private int port;

    @JsonView( SmsConfigurationViews.Public.class )
    private boolean compressed;

    @Override
    @JsonProperty( value = "host" )
    public String getUrlTemplate()
    {
        return super.getUrlTemplate();
    }

    @Override
    @JsonProperty( value = "systemId" )
    public String getUsername()
    {
        return super.getUsername();
    }

    public int getPort()
    {
        return port;
    }

    public void setPort( int port )
    {
        this.port = port;
    }

    public String getSystemType()
    {
        return systemType;
    }

    public void setSystemType( String systemType )
    {
        this.systemType = systemType;
    }

    public NumberingPlanIndicator getNumberPlanIndicator()
    {
        return numberPlanIndicator;
    }

    public void setNumberPlanIndicator( NumberingPlanIndicator numberPlanIndicator )
    {
        this.numberPlanIndicator = numberPlanIndicator;
    }

    public TypeOfNumber getTypeOfNumber()
    {
        return typeOfNumber;
    }

    public void setTypeOfNumber( TypeOfNumber typeOfNumber )
    {
        this.typeOfNumber = typeOfNumber;
    }

    public BindType getBindType()
    {
        return bindType;
    }

    public void setBindType( BindType bindType )
    {
        this.bindType = bindType;
    }

    public boolean isCompressed()
    {
        return compressed;
    }

    public void setCompressed( boolean compressed )
    {
        this.compressed = compressed;
    }
}
