package com.mass3d.trackedentityattributevalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import com.mass3d.common.AuditType;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.trackedentity.TrackedEntityInstance;

@JacksonXmlRootElement( localName = "trackedEntityAttributeValueAudit", namespace = DxfNamespaces.DXF_2_0 )
public class TrackedEntityAttributeValueAudit
    implements Serializable
{
    private long id;

    private TrackedEntityAttribute attribute;

    private TrackedEntityInstance entityInstance;

    private Date created;

    private String plainValue;

    private String encryptedValue;

    private String modifiedBy;

    private AuditType auditType;

    /**
     * This value is only used to store values from setValue when we don't know
     * if attribute is set or not.
     */
    private String value;

    public TrackedEntityAttributeValueAudit()
    {
    }

    public TrackedEntityAttributeValueAudit( TrackedEntityAttributeValue trackedEntityAttributeValue, String value,
        String modifiedBy, AuditType auditType )
    {
        this.attribute = trackedEntityAttributeValue.getAttribute();
        this.entityInstance = trackedEntityAttributeValue.getEntityInstance();

        this.created = new Date();
        this.value = value;
        this.modifiedBy = modifiedBy;
        this.auditType = auditType;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( attribute, entityInstance, created, getValue(), modifiedBy, auditType );
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }

        final TrackedEntityAttributeValueAudit other = (TrackedEntityAttributeValueAudit) obj;

        return Objects.equals( this.attribute, other.attribute )
            && Objects.equals( this.entityInstance, other.entityInstance )
            && Objects.equals( this.created, other.created )
            && Objects.equals( this.getValue(), other.getValue() )
            && Objects.equals( this.modifiedBy, other.modifiedBy )
            && Objects.equals( this.auditType, other.auditType );
    }

    public long getId()
    {
        return id;
    }

    public void setId( long id )
    {
        this.id = id;
    }

    public String getPlainValue()
    {
        return (!getAttribute().getConfidential() && this.value != null ? this.value : this.plainValue);
    }

    public void setPlainValue( String plainValue )
    {
        this.plainValue = plainValue;
    }

    public String getEncryptedValue()
    {
        return (getAttribute().getConfidential() && this.value != null ? this.value : this.encryptedValue);
    }

    public void setEncryptedValue( String encryptedValue )
    {
        this.encryptedValue = encryptedValue;
    }

    @JsonProperty( "trackedEntityAttribute" )
    @JacksonXmlProperty( localName = "trackedEntityAttribute", namespace = DxfNamespaces.DXF_2_0 )
    public TrackedEntityAttribute getAttribute()
    {
        return attribute;
    }

    public void setAttribute( TrackedEntityAttribute attribute )
    {
        this.attribute = attribute;
    }

    @JsonProperty( "trackedEntityInstance" )
    @JacksonXmlProperty( localName = "trackedEntityInstance", namespace = DxfNamespaces.DXF_2_0 )
    public TrackedEntityInstance getEntityInstance()
    {
        return entityInstance;
    }

    public void setEntityInstance( TrackedEntityInstance entityInstance )
    {
        this.entityInstance = entityInstance;
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

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getValue()
    {
        return (getAttribute().getConfidential() ? this.getEncryptedValue() : this.getPlainValue());
    }

    /**
     * Property which temporarily stores the attribute value. The
     * {@link #getEncryptedValue} and {@link #getPlainValue} methods handle the
     * value when requested.
     *
     * @param value the value to be stored.
     */
    public void setValue( String value )
    {
        this.value = value;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getModifiedBy()
    {
        return modifiedBy;
    }

    public void setModifiedBy( String modifiedBy )
    {
        this.modifiedBy = modifiedBy;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public AuditType getAuditType()
    {
        return auditType;
    }

    public void setAuditType( AuditType auditType )
    {
        this.auditType = auditType;
    }
}
