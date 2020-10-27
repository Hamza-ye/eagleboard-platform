package com.mass3d.dxf2.datavalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "dataValue", namespace = DxfNamespaces.DXF_2_0 )
public class DataValue
{
    //--------------------------------------------------------------------------
    // Properties
    //--------------------------------------------------------------------------

    protected String dataElement;

    protected String period;

    protected String orgUnit;

    protected String categoryOptionCombo;

    protected String attributeOptionCombo;

    protected String value;

    protected String storedBy;

    protected String created;

    protected String lastUpdated;

    protected String comment;

    protected Boolean followup;
    
    protected Boolean deleted;

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public DataValue()
    {
    }

    //--------------------------------------------------------------------------
    // Logic
    //--------------------------------------------------------------------------

    public boolean hasLastUpdated()
    {
        String updated = getLastUpdated();
        return updated != null && !updated.isEmpty();
    }

    public boolean hasCreated()
    {
        String creat = getCreated();
        return creat != null && !creat.isEmpty();
    }
    
    public String getPrimaryKey()
    {
        return new StringBuilder()
            .append( dataElement )
            .append( period )
            .append( orgUnit )
            .append( categoryOptionCombo )
            .append( attributeOptionCombo )
            .toString();
    }
    
    public boolean isNullValue()
    {
        return getValue() == null && getComment() == null;
    }
    
    public boolean isDeletedValue()
    {
        Boolean deleted = getDeleted();
        return deleted != null && deleted;
    }
    
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "dataElement", dataElement )
            .add( "period", period )
            .add( "orgUnit", orgUnit )
            .add( "categoryOptionCombo", categoryOptionCombo )
            .add( "attributeOptionCombo", attributeOptionCombo )
            .add( "value", value )
            .add( "comment", comment )
            .add( "followup", followup )
            .add( "deleted", deleted )
            .toString();
    }

    //--------------------------------------------------------------------------
    // Getters and setters
    //--------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getDataElement()
    {
        return dataElement;
    }

    public void setDataElement( String dataElement )
    {
        this.dataElement = dataElement;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getPeriod()
    {
        return period;
    }

    public void setPeriod( String period )
    {
        this.period = period;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getOrgUnit()
    {
        return orgUnit;
    }

    public void setOrgUnit( String orgUnit )
    {
        this.orgUnit = orgUnit;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getCategoryOptionCombo()
    {
        return categoryOptionCombo;
    }

    public void setCategoryOptionCombo( String categoryOptionCombo )
    {
        this.categoryOptionCombo = categoryOptionCombo;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getAttributeOptionCombo()
    {
        return attributeOptionCombo;
    }

    public void setAttributeOptionCombo( String attributeOptionCombo )
    {
        this.attributeOptionCombo = attributeOptionCombo;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    public void setValueForced( String value )
    {
        this.value = value;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getStoredBy()
    {
        return storedBy;
    }

    public void setStoredBy( String storedBy )
    {
        this.storedBy = storedBy;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getCreated()
    {
        return created;
    }

    public void setCreated( String created )
    {
        this.created = created;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated( String lastUpdated )
    {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getComment()
    {
        return comment;
    }

    public void setComment( String comment )
    {
        this.comment = comment;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public Boolean getFollowup()
    {
        return followup;
    }

    public void setFollowup( Boolean followup )
    {
        this.followup = followup;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public Boolean getDeleted()
    {
        return deleted;
    }

    public void setDeleted( Boolean deleted )
    {
        this.deleted = deleted;
    }

    public void close()
    {
    }
}
