package com.mass3d.metadata.version;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.Date;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;

/**
 * Base class for MetadataVersion
 *
 */
@JacksonXmlRootElement( localName = "metadataVersion", namespace = DxfNamespaces.DXF_2_0 )
public class MetadataVersion
    extends BaseIdentifiableObject
    implements Serializable
{
    private Date importDate;

    private VersionType type;

    private String hashCode;

    public MetadataVersion()
    {
    }

    public MetadataVersion( String name, VersionType type )
    {
        super();
        this.type = type;
        this.name = name;
    }

    // -------------------------------------------------------------------------
    // Getter and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public VersionType getType()
    {
        return type;
    }

    public void setType( VersionType type )
    {
        this.type = type;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getImportDate()
    {
        return importDate;
    }

    public void setImportDate( Date importDate )
    {
        this.importDate = importDate;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getHashCode()
    {
        return hashCode;
    }

    public void setHashCode( String hashCode )
    {
        this.hashCode = hashCode;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getName()
    {
        return super.getName();
    }

    @Override
    public String toString()
    {
        return "MetadataVersion{" +
            "importDate=" + importDate +
            ", type=" + type +
            ", name='" + name + '\'' +
            ", hashCode='" + hashCode + '\'' +
            '}';
    }
}
