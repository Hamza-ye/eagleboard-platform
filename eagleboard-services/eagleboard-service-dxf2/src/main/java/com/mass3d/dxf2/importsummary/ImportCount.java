package com.mass3d.dxf2.importsummary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "count", namespace = DxfNamespaces.DXF_2_0 )
public class ImportCount
{
    private int imported;

    private int updated;

    private int ignored;

    private int deleted;

    public ImportCount()
    {
    }

    public ImportCount( int imported, int updated, int ignored, int deleted )
    {
        this.imported = imported;
        this.updated = updated;
        this.ignored = ignored;
        this.deleted = deleted;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public int getImported()
    {
        return imported;
    }

    public void setImported( int imported )
    {
        this.imported = imported;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public int getUpdated()
    {
        return updated;
    }

    public void setUpdated( int updated )
    {
        this.updated = updated;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public int getIgnored()
    {
        return ignored;
    }

    public void setIgnored( int ignored )
    {
        this.ignored = ignored;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public int getDeleted()
    {
        return deleted;
    }

    public void setDeleted( int deleted )
    {
        this.deleted = deleted;
    }

    @Override
    public String toString()
    {
        return "[imports=" + imported + ", updates=" + updated + ", ignores=" + ignored + "]";
    }

    public void incrementImported()
    {
        imported++;
    }

    public void incrementUpdated()
    {
        updated++;
    }

    public void incrementIgnored()
    {
        ignored++;
    }

    public void incrementDeleted()
    {
        deleted++;
    }

    public void incrementImported( int n )
    {
        imported += n;
    }

    public void incrementUpdated( int n )
    {
        updated += n;
    }

    public void incrementIgnored( int n )
    {
        ignored += n;
    }

    public void incrementDeleted( int n )
    {
        deleted += n;
    }
}
