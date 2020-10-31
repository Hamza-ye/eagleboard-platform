package com.mass3d.program;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.*;
import com.mass3d.common.adapter.DeviceRenderTypeMapSerializer;
import com.mass3d.render.DeviceRenderTypeMap;
import com.mass3d.render.type.SectionRenderingObject;
import com.mass3d.trackedentity.TrackedEntityAttribute;

@JacksonXmlRootElement( localName = "programSection", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramSection
    extends BaseNameableObject
    implements MetadataObject
{
    private String description;

    private Program program;

    private List<TrackedEntityAttribute> trackedEntityAttributes = new ArrayList<TrackedEntityAttribute>();

    private Integer sortOrder;

//    private ObjectStyle style;

    private String formName;

    /**
     * The renderType defines how the ProgramStageSection should be rendered on the client
     */
    private DeviceRenderTypeMap<SectionRenderingObject> renderType;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramSection()
    {
    }

    public ProgramSection( String name, List<TrackedEntityAttribute> trackedEntityAttributes )
    {
        this.name = name;
        this.trackedEntityAttributes = trackedEntityAttributes;
    }

    public ProgramSection( String name, List<TrackedEntityAttribute> trackedEntityAttributes, Integer sortOrder )
    {
        this( name, trackedEntityAttributes );
        this.sortOrder = sortOrder;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

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

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "trackedEntityAttributes", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "trackedEntityAttributes", namespace = DxfNamespaces.DXF_2_0 )
    public List<TrackedEntityAttribute> getTrackedEntityAttributes()
    {
        return trackedEntityAttributes;
    }

    public void setTrackedEntityAttributes( List<TrackedEntityAttribute> trackedEntityAttributes )
    {
        this.trackedEntityAttributes = trackedEntityAttributes;
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
    @JsonSerialize( using = DeviceRenderTypeMapSerializer.class )
    public DeviceRenderTypeMap<SectionRenderingObject> getRenderType()
    {
        return renderType;
    }

    public void setRenderType(
        DeviceRenderTypeMap<SectionRenderingObject> renderType )
    {
        this.renderType = renderType;
    }
}
