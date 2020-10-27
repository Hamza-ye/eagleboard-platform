package com.mass3d.dxf2.webmessage.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dxf2.webmessage.AbstractWebMessageResponse;

@JsonPropertyOrder( {
    "type", "created", "updated", "deleted"
} )
public class ImportCountWebMessageResponse extends AbstractWebMessageResponse
{
    private int created;

    private int updated;

    private int deleted;

    public ImportCountWebMessageResponse( int created, int updated, int deleted )
    {
        this.created = created;
        this.updated = updated;
        this.deleted = deleted;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getCreated()
    {
        return created;
    }

    public void setCreated( int created )
    {
        this.created = created;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getUpdated()
    {
        return updated;
    }

    public void setUpdated( int updated )
    {
        this.updated = updated;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getDeleted()
    {
        return deleted;
    }

    public void setDeleted( int deleted )
    {
        this.deleted = deleted;
    }
}
