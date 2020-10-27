package com.mass3d.dxf2.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Objects;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "metadataPayload", namespace = DxfNamespaces.DXF_2_0 )
public class MetadataWrapper
{
    private String metadata;

    public MetadataWrapper( )
    {
    }

    public MetadataWrapper( String metadata )
    {
        this.metadata = metadata;
    }

    @JsonProperty( "metadata" )
    @JacksonXmlProperty( localName = "metadata", namespace = DxfNamespaces.DXF_2_0 )
    public String getMetadata()
    {
        return metadata;
    }

    public void setMetadata( String metadata )
    {
        this.metadata = metadata;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        MetadataWrapper temp = (MetadataWrapper) o;

        return Objects.equals( temp.getMetadata(), this.getMetadata() );
    }

    @Override
    public int hashCode()
    {
        return metadata != null ? metadata.hashCode() : 0;
    }

    @Override
    public java.lang.String toString()
    {
        return "MetadataWrapper{" +
            "metadata=" + metadata +
            '}';
    }
}