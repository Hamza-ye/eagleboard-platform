package com.mass3d.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "authority", namespace = DxfNamespaces.DXF_2_0 )
public class Authority
{
    private AuthorityType type;

    private List<String> authorities = new ArrayList<>();

    public Authority( AuthorityType type )
    {
        this.type = type;
    }

    public Authority( AuthorityType type, List<String> authorities )
    {
        this( type );
        this.authorities = authorities;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public AuthorityType getType()
    {
        return type;
    }

    public void setType( AuthorityType type )
    {
        this.type = type;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "authorities", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "authority", namespace = DxfNamespaces.DXF_2_0 )
    public List<String> getAuthorities()
    {
        return authorities;
    }

    public void setAuthorities( List<String> authorities )
    {
        this.authorities = authorities;
    }

    @Override
    public String toString()
    {
        return "Authority{" +
            "type=" + type +
            ", authorities=" + authorities +
            '}';
    }
}
