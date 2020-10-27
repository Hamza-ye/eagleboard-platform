package com.mass3d.artemis.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "artemisEmbedded", namespace = DxfNamespaces.DXF_2_0 )
public class ArtemisEmbeddedConfig
{
    private boolean security = false;

    private boolean persistence = true;

    private int nioRemotingThreads = 5;

    public ArtemisEmbeddedConfig()
    {
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isSecurity()
    {
        return security;
    }

    public ArtemisEmbeddedConfig setSecurity( boolean security )
    {
        this.security = security;
        return this;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isPersistence()
    {
        return persistence;
    }

    public ArtemisEmbeddedConfig setPersistence( boolean persistence )
    {
        this.persistence = persistence;
        return this;
    }

    public int getNioRemotingThreads()
    {
        return nioRemotingThreads;
    }

    public ArtemisEmbeddedConfig setNioRemotingThreads( int nioRemotingThreads )
    {
        this.nioRemotingThreads = nioRemotingThreads;
        return this;
    }
}
