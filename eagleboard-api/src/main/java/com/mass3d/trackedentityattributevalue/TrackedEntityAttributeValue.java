package com.mass3d.trackedentityattributevalue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.Date;
import com.mass3d.audit.AuditAttribute;
import com.mass3d.audit.AuditScope;
import com.mass3d.audit.Auditable;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.trackedentity.TrackedEntityInstance;

/**
 * TODO index on attribute and instance
 *
 */
@Auditable( scope = AuditScope.TRACKER )
@JacksonXmlRootElement( localName = "trackedEntityAttributeValue", namespace = DxfNamespaces.DXF_2_0 )
public class TrackedEntityAttributeValue
    implements Serializable
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = -4469496681709547707L;

    @AuditAttribute
    private TrackedEntityAttribute attribute;

    @AuditAttribute
    private TrackedEntityInstance entityInstance;

    private Date created;

    private Date lastUpdated;

    private String encryptedValue;

    private String plainValue;

    /**
     * This value is only used to store values from setValue when we don't know
     * if attribute is set or not.
     */
    private String value;

    private String storedBy;

    // -------------------------------------------------------------------------
    // Transient properties
    // -------------------------------------------------------------------------

    private transient boolean auditValueIsSet = false;

    private transient boolean valueIsSet = false;

    private transient String auditValue;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public TrackedEntityAttributeValue()
    {
        setAutoFields();
    }

    public TrackedEntityAttributeValue( TrackedEntityAttribute attribute, TrackedEntityInstance entityInstance )
    {
        setAttribute( attribute );
        setEntityInstance( entityInstance );
    }

    public TrackedEntityAttributeValue( TrackedEntityAttribute attribute, TrackedEntityInstance entityInstance,
        String value )
    {
        setAttribute( attribute );
        setEntityInstance( entityInstance );
        setValue( value );
    }

    public void setAutoFields()
    {
        Date date = new Date();

        if ( created == null )
        {
            created = date;
        }

        setLastUpdated( date );
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entityInstance == null) ? 0 : entityInstance.hashCode());
        result = prime * result + ((attribute == null) ? 0 : attribute.hashCode());
        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
        return result;
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null )
        {
            return false;
        }

        if ( !getClass().isAssignableFrom( object.getClass() ) )
        {
            return false;
        }

        final TrackedEntityAttributeValue other = (TrackedEntityAttributeValue) object;

        if ( entityInstance == null )
        {
            if ( other.entityInstance != null )
            {
                return false;
            }
        }
        else if ( !entityInstance.equals( other.entityInstance ) )
        {
            return false;
        }

        if ( attribute == null )
        {
            if ( other.attribute != null )
            {
                return false;
            }
        }
        else if ( !attribute.equals( other.attribute ) )
        {
            return false;
        }

        if ( getValue() == null )
        {
            if ( other.getValue() != null )
            {
                return false;
            }
        }
        else if ( !getValue().equals( other.getValue() ) )
        {
            return false;
        }

        return true;
    }


    @Override
    public String toString()
    {
        return "TrackedEntityAttributeValue{" +
            "attribute=" + attribute +
            ", entityInstance=" + (entityInstance != null ? entityInstance.getUid() : "null") +
            ", value='" + value + '\'' +
            ", created=" + created +
            ", lastUpdated=" + lastUpdated +
            ", storedBy='" + storedBy + '\'' +
            '}';
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public Date getCreated()
    {
        return created;
    }

    public TrackedEntityAttributeValue setCreated( Date created )
    {
        this.created = created;
        return this;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public TrackedEntityAttributeValue setLastUpdated( Date lastUpdated )
    {
        this.lastUpdated = lastUpdated;
        return this;
    }

    /**
     * Retrieves the encrypted value if the attribute is confidential. If the
     * value is not confidential, returns old value. Should be null unless it was
     * confidential at an earlier stage.
     *
     * @return String with decrypted value or null.
     */
    @JsonIgnore
    public String getEncryptedValue()
    {
        return (getAttribute().getConfidential() && this.value != null ? this.value : this.encryptedValue);
    }

    public void setEncryptedValue( String encryptedValue )
    {
        this.encryptedValue = encryptedValue;

        if ( getAttribute().getConfidential() )
        {
            auditValue = encryptedValue;
            auditValueIsSet = true;
        }
    }

    /**
     * Retrieves the plain-text value is the attribute isn't confidential. If
     * the value is confidential, this value should be null, unless it was
     * non-confidential at an earlier stage.
     *
     * @return String with plain-text value or null.
     */
    @JsonIgnore
    public String getPlainValue()
    {
        return (!getAttribute().getConfidential() && this.value != null ? this.value : this.plainValue);
    }

    public void setPlainValue( String plainValue )
    {
        this.plainValue = plainValue;

        if ( !getAttribute().getConfidential() )
        {
            auditValue = plainValue;
            auditValueIsSet = true;
        }
    }

    /**
     * Returns the encrypted or the plain-text value based on the confidential
     * state of the attribute.
     *
     * @return String with value, either plain-text or decrypted.
     */
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
     * @return a {@link TrackedEntityAttributeValue}.
     */
    public TrackedEntityAttributeValue setValue( String value )
    {
        if ( !auditValueIsSet )
        {
            this.auditValue = valueIsSet ? this.value : value;
            auditValueIsSet = true;
        }

        valueIsSet = true;

        this.value = value;

        return this;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getStoredBy()
    {
        return storedBy;
    }

    public TrackedEntityAttributeValue setStoredBy( String storedBy )
    {
        this.storedBy = storedBy;
        return this;
    }

    @JsonProperty( "trackedEntityAttribute" )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( localName = "trackedEntityAttribute", namespace = DxfNamespaces.DXF_2_0 )
    public TrackedEntityAttribute getAttribute()
    {
        return attribute;
    }

    public TrackedEntityAttributeValue setAttribute( TrackedEntityAttribute attribute )
    {
        this.attribute = attribute;
        return this;
    }

    @JsonProperty( "trackedEntityInstance" )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( localName = "trackedEntityInstance", namespace = DxfNamespaces.DXF_2_0 )
    public TrackedEntityInstance getEntityInstance()
    {
        return entityInstance;
    }

    public TrackedEntityAttributeValue setEntityInstance( TrackedEntityInstance entityInstance )
    {
        this.entityInstance = entityInstance;
        return this;
    }

    public String getAuditValue()
    {
        return auditValue;
    }
}
