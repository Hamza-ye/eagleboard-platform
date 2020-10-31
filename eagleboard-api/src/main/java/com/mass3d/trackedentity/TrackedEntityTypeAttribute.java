package com.mass3d.trackedentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.EmbeddedObject;
import com.mass3d.common.ValueType;

@JacksonXmlRootElement( localName = "trackedEntityTypeAttribute", namespace = DxfNamespaces.DXF_2_0 )
public class TrackedEntityTypeAttribute
    extends BaseIdentifiableObject implements EmbeddedObject
{
    private TrackedEntityType trackedEntityType;

    private TrackedEntityAttribute trackedEntityAttribute;

    private boolean displayInList;

    private Boolean mandatory;

    private Boolean searchable = false;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public TrackedEntityTypeAttribute()
    {
        setAutoFields();
    }

    public TrackedEntityTypeAttribute( TrackedEntityType trackedEntityType, TrackedEntityAttribute trackedEntityAttribute )
    {
        this();
        this.trackedEntityType = trackedEntityType;
        this.trackedEntityAttribute = trackedEntityAttribute;
    }

    public TrackedEntityTypeAttribute( TrackedEntityType trackedEntityType, TrackedEntityAttribute attribute, boolean displayInList,
        Boolean mandatory )
    {
        this( trackedEntityType, attribute );
        this.displayInList = displayInList;
        this.mandatory = mandatory;
    }

    @Override
    public String getName()
    {
        return (trackedEntityType != null ? trackedEntityType.getDisplayName() + " " : "") + (trackedEntityAttribute != null ? trackedEntityAttribute.getDisplayName() : "");
    }

    @JsonProperty
    public String getDisplayShortName()
    {
        return (trackedEntityType != null ? trackedEntityType.getDisplayShortName() + " " : "") + (trackedEntityAttribute != null ? trackedEntityAttribute.getDisplayShortName() : "");
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ValueType getValueType()
    {
        return trackedEntityAttribute != null ? trackedEntityAttribute.getValueType() : null;
    }

    @Override
    public String toString()
    {
        return "{" +
            "\"class\":\"" + getClass() + "\", " +
            "\"id\":\"" + id + "\", " +
            "\"uid\":\"" + uid + "\", " +
            "\"trackedEntityType\":" + trackedEntityType + ", " +
            "\"trackedEntityAttribute\":" + trackedEntityAttribute + ", " +
            "\"created\":\"" + created + "\", " +
            "\"lastUpdated\":\"" + lastUpdated + "\" " +
            "}";
    }

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public TrackedEntityType getTrackedEntityType()
    {
        return trackedEntityType;
    }

    public void setTrackedEntityType( TrackedEntityType trackedEntityType )
    {
        this.trackedEntityType = trackedEntityType;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public TrackedEntityAttribute getTrackedEntityAttribute()
    {
        return trackedEntityAttribute;
    }

    public void setTrackedEntityAttribute( TrackedEntityAttribute trackedEntityAttribute )
    {
        this.trackedEntityAttribute = trackedEntityAttribute;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean isMandatory()
    {
        return mandatory;
    }

    public void setMandatory( Boolean mandatory )
    {
        this.mandatory = mandatory;
    }

    @JsonProperty
    @JacksonXmlProperty( localName = "displayInList", namespace = DxfNamespaces.DXF_2_0 )
    public boolean isDisplayInList()
    {
        return displayInList;
    }

    public void setDisplayInList( boolean displayInList )
    {
        this.displayInList = displayInList;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean isSearchable()
    {
        return searchable;
    }

    public void setSearchable( Boolean searchable )
    {
        this.searchable = searchable;
    }
}
