package com.mass3d.sms.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.sms.config.views.SmsConfigurationViews;

/**
 * Super class for gateway configurations
 *
 */
@JacksonXmlRootElement( localName = "smsgatewayconfig", namespace = DxfNamespaces.DXF_2_0 )
@JsonInclude( JsonInclude.Include.NON_NULL )
@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, property = "type" )
@JsonSubTypes( { @JsonSubTypes.Type( value = BulkSmsGatewayConfig.class, name = "bulksms" ),
                 @JsonSubTypes.Type( value = GenericHttpGatewayConfig.class, name = "http" ),
                 @JsonSubTypes.Type( value = ClickatellGatewayConfig.class, name = "clickatell" ),
                 @JsonSubTypes.Type( value = SMPPGatewayConfig.class, name = "smpp" ) } )
public abstract class SmsGatewayConfig
    implements Serializable
{
    private static final long serialVersionUID = -4288220735161151632L;

    @JsonView( SmsConfigurationViews.Public.class )
    private String uid;

    @JsonView( SmsConfigurationViews.Public.class )
    private String name;

    @JsonView( SmsConfigurationViews.Public.class )
    private String username;

    @JsonView( SmsConfigurationViews.Internal.class )
    private String password;

    @JsonView( SmsConfigurationViews.Public.class )
    private boolean isDefault;

    @JsonView( SmsConfigurationViews.Public.class )
    private boolean sendUrlParameters;

    @JsonView( SmsConfigurationViews.Public.class )
    private String urlTemplate;

    public String getUrlTemplate()
    {
        return urlTemplate;
    }

    public void setUrlTemplate( String urlTemplate )
    {
        this.urlTemplate = urlTemplate;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public boolean isDefault()
    {
        return isDefault;
    }

    public void setDefault( boolean isDefault )
    {
        this.isDefault = isDefault;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid( String uid )
    {
        this.uid = uid;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public boolean isSendUrlParameters()
    {
        return sendUrlParameters;
    }

    public void setSendUrlParameters( boolean sendUrlParameters )
    {
        this.sendUrlParameters = sendUrlParameters;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( !( o instanceof SmsGatewayConfig ) )
        {
            return false;
        }

        final SmsGatewayConfig other = (SmsGatewayConfig) o;

        return uid.equals( other.getUid() );
    }

    @Override
    public int hashCode()
    {
        return uid.hashCode();
    }

    @Override
    public String toString()
    {
        return "SmsGatewayConfig{" +
            "uid='" + uid + '\'' +
            ", name='" + name + '\'' +
            ", username='" + username + '\'' +
            ", isDefault=" + isDefault +
            ", urlTemplate='" + urlTemplate + '\'' +
            '}';
    }
}
