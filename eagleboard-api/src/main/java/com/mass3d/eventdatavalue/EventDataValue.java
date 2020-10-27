package com.mass3d.eventdatavalue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "eventDataValue", namespace = DxfNamespaces.DXF_2_0 )
public class EventDataValue implements Serializable
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 2738519623273453182L;

    private String dataElement = "";

    private Date created = new Date();

    private Date lastUpdated = new Date();

    private String value;

    private Boolean providedElsewhere = false;

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

    public EventDataValue()
    {

    }

    public EventDataValue( String dataElement, String value )
    {
        this.dataElement = dataElement;
        setValue( value );
    }

    public EventDataValue( String dataElement, String value, String storedBy )
    {
        this.dataElement = dataElement;
        this.storedBy = storedBy;
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
    // hashCode, equals and toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return Objects.hash( dataElement );
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }
        if ( object == null || getClass() != object.getClass() )
        {
            return false;
        }

        return dataElement.equals( ( (EventDataValue) object ).dataElement );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------
    public Boolean getProvidedElsewhere()
    {
        return providedElsewhere;
    }

    public void setProvidedElsewhere( Boolean providedElsewhere )
    {
        this.providedElsewhere = providedElsewhere;
    }

    public void setDataElement( String dataElement )
    {
        this.dataElement = dataElement;
    }

    @JsonIgnore
    public String getDataElement()
    {
        return dataElement;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated( Date created )
    {
        this.created = created;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated( Date lastUpdated )
    {
        this.lastUpdated = lastUpdated;
    }

    public void setValue( String value )
    {
        if ( !auditValueIsSet )
        {
            auditValue = valueIsSet ? this.value : value;
            auditValueIsSet = true;
        }

        valueIsSet = true;

        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public String getStoredBy()
    {
        return storedBy;
    }

    public void setStoredBy( String storedBy )
    {
        this.storedBy = storedBy;
    }

    @JsonIgnore
    public String getAuditValue()
    {
        return auditValue;
    }

    @Override
    public String toString()
    {
        return "EventDataValue{" +
            "dataElement=" + dataElement +
            ", created=" + created +
            ", lastUpdated=" + lastUpdated +
            ", value='" + value + '\'' +
            ", providedElsewhere=" + providedElsewhere +
            ", storedBy='" + storedBy + '\'' +
            '}';
    }
}
