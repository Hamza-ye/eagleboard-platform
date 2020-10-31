package com.mass3d.program;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.EmbeddedObject;
import com.mass3d.common.ValueType;
import com.mass3d.common.adapter.DeviceRenderTypeMapSerializer;
import com.mass3d.render.DeviceRenderTypeMap;
import com.mass3d.render.type.ValueTypeRenderingObject;
import com.mass3d.trackedentity.TrackedEntityAttribute;

@JacksonXmlRootElement( localName = "programTrackedEntityAttribute", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramTrackedEntityAttribute
    extends BaseIdentifiableObject implements EmbeddedObject
{
    private Program program;

    private TrackedEntityAttribute attribute;

    private boolean displayInList;

    private Integer sortOrder;

    private Boolean mandatory;

    private Boolean allowFutureDate;

    private Boolean renderOptionsAsRadio = false; //TODO: Remove, replaced by renderType

    /**
     * Represents how the client should render the TrackedEntityAttribute
     */
    private DeviceRenderTypeMap<ValueTypeRenderingObject> renderType;

    private Set<ProgramTrackedEntityAttributeGroup> groups = new HashSet<>();

    private Boolean searchable = false;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramTrackedEntityAttribute()
    {
        setAutoFields();
    }

    public ProgramTrackedEntityAttribute( Program program, TrackedEntityAttribute attribute )
    {
        this();
        this.program = program;
        this.attribute = attribute;
    }

    public ProgramTrackedEntityAttribute( Program program, TrackedEntityAttribute attribute, boolean displayInList,
        Boolean mandatory )
    {
        this( program, attribute );
        this.displayInList = displayInList;
        this.mandatory = mandatory;
    }

    public ProgramTrackedEntityAttribute( Program program, TrackedEntityAttribute attribute, boolean displayInList,
        Boolean mandatory, Integer sortOrder )
    {
        this( program, attribute );
        this.displayInList = displayInList;
        this.mandatory = mandatory;
        this.sortOrder = sortOrder;
    }

    public ProgramTrackedEntityAttribute( Program program, TrackedEntityAttribute attribute, boolean displayInList,
        Boolean mandatory, Boolean allowFutureDate )
    {
        this( program, attribute, displayInList, mandatory );
        this.allowFutureDate = allowFutureDate;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addGroup( ProgramTrackedEntityAttributeGroup group )
    {
        groups.add( group );
        group.getAttributes().add( this );
    }

    public void removeGroup( ProgramTrackedEntityAttributeGroup group )
    {
        groups.remove( group );
        group.getAttributes().remove( this );
    }

    public void updateProgramTrackedEntityAttributeGroups( Set<ProgramTrackedEntityAttributeGroup> updates )
    {
        for ( ProgramTrackedEntityAttributeGroup group : new HashSet<>( groups ) )
        {
            if ( !updates.contains( group ) )
            {
                removeGroup( group );
            }
        }

        updates.forEach( this::addGroup );
    }

    @Override
    public String getName()
    {
        return (program != null ? program.getDisplayName() + " " : "") + (attribute != null ? attribute.getDisplayName() : "");
    }

    @JsonProperty
    public String getDisplayShortName()
    {
        return (program != null ? program.getDisplayShortName() + " " : "") + (attribute != null ? attribute.getDisplayShortName() : "");
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ValueType getValueType()
    {
        return attribute != null ? attribute.getValueType() : null;
    }

    @Override
    public String toString()
    {
        return "ProgramTrackedEntityAttribute{" +
            "class=" + getClass() +
            ", program=" + program +
            ", attribute=" + attribute +
            ", displayInList=" + displayInList +
            ", sortOrder=" + sortOrder +
            ", mandatory=" + mandatory +
            ", allowFutureDate=" + allowFutureDate +
            ", renderOptionsAsRadio=" + renderOptionsAsRadio +
            ", renderType=" + renderType +
            ", groups=" + groups +
            ", searchable=" + searchable +
            ", id=" + id +
            ", uid='" + uid + '\'' +
            ", created=" + created +
            ", lastUpdated=" + lastUpdated +
            '}';
    }
// -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Program getProgram()
    {
        return program;
    }

    public void setProgram( Program program )
    {
        this.program = program;
    }

    @JsonProperty( "trackedEntityAttribute" )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( localName = "trackedEntityAttribute", namespace = DxfNamespaces.DXF_2_0 )
    public TrackedEntityAttribute getAttribute()
    {
        return attribute;
    }

    public void setAttribute( TrackedEntityAttribute attribute )
    {
        this.attribute = attribute;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean isMandatory()
    {
        if ( mandatory != null )
        {
            return mandatory;
        }

        return false;
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
    public Boolean getAllowFutureDate()
    {
        return allowFutureDate;
    }

    public void setAllowFutureDate( Boolean allowFutureDate )
    {
        this.allowFutureDate = allowFutureDate;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder( Integer sortOrder )
    {
        this.sortOrder = sortOrder;
    }

    @JsonProperty( "programTrackedEntityAttributeGroups" )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( localName = "programTrackedEntityAttributeGroups", namespace = DxfNamespaces.DXF_2_0 )
    public Set<ProgramTrackedEntityAttributeGroup> getGroups()
    {
        return this.groups;
    }

    public void setGroups( Set<ProgramTrackedEntityAttributeGroup> groups )
    {
        this.groups = groups;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean getRenderOptionsAsRadio()
    {
        return renderOptionsAsRadio;
    }

    public void setRenderOptionsAsRadio( Boolean renderOptionsAsRadio )
    {
        this.renderOptionsAsRadio = renderOptionsAsRadio;
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

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @JsonSerialize( using = DeviceRenderTypeMapSerializer.class )
    public DeviceRenderTypeMap<ValueTypeRenderingObject> getRenderType()
    {
        return renderType;
    }

    public void setRenderType(
        DeviceRenderTypeMap<ValueTypeRenderingObject> renderType )
    {
        this.renderType = renderType;
    }
}
