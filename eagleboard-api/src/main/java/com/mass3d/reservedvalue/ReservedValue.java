package com.mass3d.reservedvalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import com.mass3d.common.DxfNamespaces;

public class ReservedValue
    implements Serializable
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 334738541365949298L;

    private int id;

    private String ownerObject;

    private String ownerUid;

    private String key;

    private String value;

    private Date created;

    private Date expiryDate;

    public ReservedValue()
    {
        created = new Date();
    }

    public ReservedValue( String ownerObject, String ownerUid, String key, String value, Date expiryDate )
    {
        this.ownerObject = ownerObject;
        this.ownerUid = ownerUid;
        this.key = key;
        this.value = value;
        this.expiryDate = expiryDate;
        this.created = new Date();
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getOwnerObject()
    {
        return ownerObject;
    }

    public void setOwnerObject( String ownerObject )
    {
        this.ownerObject = ownerObject;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getOwnerUid()
    {
        return ownerUid;
    }

    public void setOwnerUid( String ownerUid )
    {
        this.ownerUid = ownerUid;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getKey()
    {
        return key;
    }

    public void setKey( String key )
    {
        this.key = key;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate( Date expires )
    {
        this.expiryDate = expires;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;
        ReservedValue that = (ReservedValue) o;
        return Objects.equals( ownerObject, that.ownerObject ) &&
            Objects.equals( ownerUid, that.ownerUid ) &&
            Objects.equals( key, that.key ) &&
            Objects.equals( value, that.value );
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getCreated()
    {
        return created;
    }

    public void setCreated( Date created )
    {
        this.created = created;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( ownerObject, ownerUid, key, value );
    }

    @Override
    public String toString()
    {
        return "ReservedValue{" +
            "ownerObject='" + ownerObject + '\'' +
            ", ownerUid='" + ownerUid + '\'' +
            ", key='" + key + '\'' +
            ", value='" + value + '\'' +
            ", expires=" + expiryDate +
            '}';
    }
}
