package com.mass3d.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Objects;
import com.mass3d.audit.AuditAttribute;

@JacksonXmlRootElement( localName = "softDeletableObject", namespace = DxfNamespaces.DXF_2_0 )
public class SoftDeletableObject extends BaseIdentifiableObject
{
    /**
     * Boolean to check if the object is soft deleted.
     */
    @AuditAttribute
    private boolean deleted = false;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public SoftDeletableObject()
    {
    }

    public SoftDeletableObject( boolean deleted )
    {
        this.deleted = deleted;
    }

    // -------------------------------------------------------------------------
    // Setters and getters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( localName = "deleted", namespace = DxfNamespaces.DXF_2_0 )
    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted( boolean deleted )
    {
        this.deleted = deleted;
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
        if ( !super.equals( o ) )
        {
            return false;
        }
        SoftDeletableObject that = (SoftDeletableObject) o;
        return deleted == that.deleted;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( super.hashCode(), deleted );
    }
}
