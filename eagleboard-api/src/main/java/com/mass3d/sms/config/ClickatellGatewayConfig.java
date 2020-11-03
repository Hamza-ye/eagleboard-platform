package com.mass3d.sms.config;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonView;
import com.mass3d.sms.config.views.SmsConfigurationViews;

@JsonTypeName( "clickatell" )
public class ClickatellGatewayConfig
    extends SmsGatewayConfig
{
    private static final long serialVersionUID = -4286107769356591957L;

    @JsonView( SmsConfigurationViews.Internal.class )
    private String authToken;

    public String getAuthToken()
    {
        return authToken;
    }

    public void setAuthToken( String authToken )
    {
        this.authToken = authToken;
    }
}
