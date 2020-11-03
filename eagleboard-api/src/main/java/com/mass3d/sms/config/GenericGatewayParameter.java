package com.mass3d.sms.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import com.mass3d.sms.config.views.SmsConfigurationViews;

public class GenericGatewayParameter
    implements Serializable
{
    private static final long serialVersionUID = -863990758156009672L;

    @JsonView( SmsConfigurationViews.Public.class )
    private String key;

    @JsonView( SmsConfigurationViews.Public.class )
    private String value;

    @JsonView( SmsConfigurationViews.Public.class )
    private boolean header;

    @JsonView( SmsConfigurationViews.Public.class )
    private boolean encode;

    @JsonView( SmsConfigurationViews.Public.class )
    private boolean confidential;

    @JsonView( SmsConfigurationViews.Public.class )
    public String getKey()
    {
        return key;
    }

    public void setKey( String key )
    {
        this.key = key;
    }

    public String getValue()
    {
        return confidential ? "" : value;
    }

    @JsonIgnore
    public String getDisplayValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    public boolean isConfidential()
    {
        return confidential;
    }

    public void setConfidential( boolean confidential )
    {
        this.confidential = confidential;
    }

    public boolean isHeader()
    {
        return header;
    }

    public void setHeader( boolean header )
    {
        this.header = header;
    }

    public boolean isEncode()
    {
        return encode;
    }

    public void setEncode( boolean encode )
    {
        this.encode = encode;
    }
}
