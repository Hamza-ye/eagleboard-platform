package com.mass3d.artemis.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "artemis", namespace = DxfNamespaces.DXF_2_0 )
public class ArtemisConfigData
{
    private ArtemisMode mode = ArtemisMode.EMBEDDED;

    private String host = "127.0.0.1";

    // AMQP port should be 5672/5673 but we don't want to cause issues with existing AMQP installations
    // so we keep 25672 as default port (since we default to embedded server).
    //
    // NOTE we used 15672 before here, but it was changed as it interfers with RabbitMQ default port
    private int port = 25672;

    private String username = "guest";

    private String password = "guest";

    private ArtemisEmbeddedConfig embedded = new ArtemisEmbeddedConfig();

    // if true, the producer will not wait for the broker ACK
    private boolean sendAsync = true;

    public ArtemisConfigData()
    {
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ArtemisMode getMode()
    {
        return mode;
    }

    public void setMode( ArtemisMode mode )
    {
        this.mode = mode;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getHost()
    {
        return host;
    }

    public void setHost( String host )
    {
        this.host = host;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getPort()
    {
        return port;
    }

    public void setPort( int port )
    {
        this.port = port;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ArtemisEmbeddedConfig getEmbedded()
    {
        return embedded;
    }

    public void setEmbedded( ArtemisEmbeddedConfig embedded )
    {
        this.embedded = embedded;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isSendAsync()
    {
        return sendAsync;
    }

    public void setSendAsync( boolean sendAsync )
    {
        this.sendAsync = sendAsync;
    }
}
