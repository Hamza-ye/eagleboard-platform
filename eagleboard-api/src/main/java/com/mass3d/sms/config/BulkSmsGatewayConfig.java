package com.mass3d.sms.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonView;
import com.mass3d.sms.config.views.SmsConfigurationViews;

@JsonTypeName( "bulksms" )
public class BulkSmsGatewayConfig
    extends SmsGatewayConfig
{
    private static final long serialVersionUID = 5249703354480948250L;

    private final String JSON_API_URL = "https://api.bulksms.com/v1/messages";

    @Override
    @JsonProperty( value = "urlTemplate" )
    @JsonView( SmsConfigurationViews.Public.class )
    public String getUrlTemplate()
    {
        return this.JSON_API_URL;
    }
}
