package com.mass3d.trackedentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.BaseNameableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.organisationunit.FeatureType;

@JacksonXmlRootElement( localName = "trackedEntityType", namespace = DxfNamespaces.DXF_2_0 )
public class TrackedEntityType
    extends BaseNameableObject implements MetadataObject
{
    private List<TrackedEntityTypeAttribute> trackedEntityTypeAttributes = new ArrayList<>();

    private FeatureType featureType = FeatureType.NONE;

//    private ObjectStyle style;

    private String formName;

    /**
     * Property indicating minimum number of attributes required to fill
     * before search is triggered
     */
    private int minAttributesRequiredToSearch = 1;

    /**
     * Property indicating maximum number of TEI to return after search
     */
    private int maxTeiCountToReturn = 0;

    /**
     * Property indicating whether to allow (read) audit log or not
     */
    private boolean allowAuditLog;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public TrackedEntityType()
    {

    }

    public TrackedEntityType( String name, String description )
    {
        this.name = name;
        this.description = description;
    }

    // -------------------------------------------------------------------------
    // Logic methods
    // -------------------------------------------------------------------------

    /**
     * Returns TrackedEntityAttributes from TrackedEntityTypeAttributes.
     */
    public List<TrackedEntityAttribute> getTrackedEntityAttributes()
    {
        return trackedEntityTypeAttributes.stream().map( TrackedEntityTypeAttribute::getTrackedEntityAttribute ).collect( Collectors
            .toList() );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------    

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "trackedEntityTypeAttributes", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "trackedEntityTypeAttribute", namespace = DxfNamespaces.DXF_2_0 )
    public List<TrackedEntityTypeAttribute> getTrackedEntityTypeAttributes()
    {
        return trackedEntityTypeAttributes;
    }

    public void setTrackedEntityTypeAttributes( List<TrackedEntityTypeAttribute> trackedEntityTypeAttributes )
    {
        this.trackedEntityTypeAttributes = trackedEntityTypeAttributes;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getMinAttributesRequiredToSearch()
    {
        return minAttributesRequiredToSearch;
    }

    public void setMinAttributesRequiredToSearch( int minAttributesRequiredToSearch )
    {
        this.minAttributesRequiredToSearch = minAttributesRequiredToSearch;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getMaxTeiCountToReturn()
    {
        return maxTeiCountToReturn;
    }

    public void setMaxTeiCountToReturn( int maxTeiCountToReturn )
    {
        this.maxTeiCountToReturn = maxTeiCountToReturn;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isAllowAuditLog()
    {
        return allowAuditLog;
    }

    public void setAllowAuditLog( boolean allowAuditLog )
    {
        this.allowAuditLog = allowAuditLog;
    }

    // -------------------------------------------------------------------------
    // Logic methods
    // -------------------------------------------------------------------------    

    /**
     * Returns IDs of searchable TrackedEntityAttributes.
     */
    public List<String> getSearchableAttributeIds()
    {
        List<String> searchableAttributes = new ArrayList<>();

        for ( TrackedEntityTypeAttribute trackedEntityTypeAttribute : trackedEntityTypeAttributes )
        {
            if ( trackedEntityTypeAttribute.isSearchable() || trackedEntityTypeAttribute.getTrackedEntityAttribute().isSystemWideUnique() )
            {
                searchableAttributes.add( trackedEntityTypeAttribute.getTrackedEntityAttribute().getUid() );
            }
        }

        return searchableAttributes;
    }

//    @JsonProperty
//    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
//    public ObjectStyle getStyle()
//    {
//        return style;
//    }
//
//    public void setStyle( ObjectStyle style )
//    {
//        this.style = style;
//    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getFormName()
    {
        return formName;
    }

    public void setFormName( String formName )
    {
        this.formName = formName;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public FeatureType getFeatureType()
    {
        return featureType;
    }

    public void setFeatureType( FeatureType featureType )
    {
        this.featureType = featureType;
    }
}
