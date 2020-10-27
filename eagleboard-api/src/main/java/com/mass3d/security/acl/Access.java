package com.mass3d.security.acl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "access", namespace = DxfNamespaces.DXF_2_0 )
public class Access
{
    private boolean manage;

    private boolean externalize;

    private boolean write;

    private boolean read;

    private boolean update;

    private boolean delete;

    private AccessData data;

    public Access()
    {
    }

    public Access( boolean value )
    {
        this.manage = value;
        this.externalize = value;
        this.write = value;
        this.read = value;
        this.update = value;
        this.delete = value;
    }

    @JsonProperty
    @JacksonXmlProperty( localName = "manage", namespace = DxfNamespaces.DXF_2_0 )
    public boolean isManage()
    {
        return manage;
    }

    public Access setManage( boolean manage )
    {
        this.manage = manage;
        return this;
    }

    @JsonProperty
    @JacksonXmlProperty( localName = "externalize", namespace = DxfNamespaces.DXF_2_0 )
    public boolean isExternalize()
    {
        return externalize;
    }

    public Access setExternalize( boolean externalize )
    {
        this.externalize = externalize;
        return this;
    }

    @JsonProperty
    @JacksonXmlProperty( localName = "write", namespace = DxfNamespaces.DXF_2_0 )
    public boolean isWrite()
    {
        return write;
    }

    public Access setWrite( boolean write )
    {
        this.write = write;
        return this;
    }

    @JsonProperty
    @JacksonXmlProperty( localName = "read", namespace = DxfNamespaces.DXF_2_0 )
    public boolean isRead()
    {
        return read;
    }

    public Access setRead( boolean read )
    {
        this.read = read;
        return this;
    }

    @JsonProperty
    @JacksonXmlProperty( localName = "update", namespace = DxfNamespaces.DXF_2_0 )
    public boolean isUpdate()
    {
        return update;
    }

    public Access setUpdate( boolean update )
    {
        this.update = update;
        return this;
    }

    @JsonProperty
    @JacksonXmlProperty( localName = "delete", namespace = DxfNamespaces.DXF_2_0 )
    public boolean isDelete()
    {
        return delete;
    }

    public Access setDelete( boolean delete )
    {
        this.delete = delete;
        return this;
    }

    @JsonProperty
    @JacksonXmlProperty( localName = "data", namespace = DxfNamespaces.DXF_2_0 )
    public AccessData getData()
    {
        return data;
    }

    public Access setData( AccessData data )
    {
        this.data = data;
        return this;
    }

    @Override
    public String toString()
    {
        return "Access{" +
            "manage=" + manage +
            ", externalize=" + externalize +
            ", write=" + write +
            ", read=" + read +
            ", update=" + update +
            ", delete=" + delete +
            ", data=" + data +
            '}';
    }
}
