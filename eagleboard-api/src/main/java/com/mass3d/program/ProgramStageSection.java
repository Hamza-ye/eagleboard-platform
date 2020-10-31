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
import com.mass3d.dataelement.DataElement;
import com.mass3d.render.DeviceRenderTypeMap;
import com.mass3d.render.type.SectionRenderingObject;

@JacksonXmlRootElement( localName = "programStageSection", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramStageSection
    extends BaseNameableObject
    implements MetadataObject
{
    private String description;

    private ProgramStage programStage;

    private List<DataElement> dataElements = new ArrayList<>();

    private List<ProgramIndicator> programIndicators = new ArrayList<>();

    private Integer sortOrder;

//    /**
//     * The style represents how the ProgramStageSection should be presented on the client
//     */
//    private ObjectStyle style;

    private String formName;

    /**
     * The renderType defines how the ProgramStageSection should be rendered on the client
     */
    private DeviceRenderTypeMap<SectionRenderingObject> renderType;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramStageSection()
    {
    }

    public ProgramStageSection( String name, List<DataElement> dataElements )
    {
        this.name = name;
        this.dataElements = dataElements;
    }

    public ProgramStageSection( String name, List<DataElement> dataElements, Integer sortOrder )
    {
        this( name, dataElements );
        this.sortOrder = sortOrder;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean hasProgramStage()
    {
        return programStage != null;
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
    public ProgramStage getProgramStage()
    {
        return programStage;
    }

    public void setProgramStage( ProgramStage programStage )
    {
        this.programStage = programStage;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "dataElements", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "dataElement", namespace = DxfNamespaces.DXF_2_0 )
    public List<DataElement> getDataElements()
    {
        return dataElements;
    }

    public void setDataElements( List<DataElement> dataElements )
    {
        this.dataElements = dataElements;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "programIndicators", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "programIndicator", namespace = DxfNamespaces.DXF_2_0 )
    public List<ProgramIndicator> getProgramIndicators()
    {
        return programIndicators;
    }

    public void setProgramIndicators( List<ProgramIndicator> programIndicators )
    {
        this.programIndicators = programIndicators;
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

    public void setRenderType( DeviceRenderTypeMap<SectionRenderingObject> renderType )
    {
        this.renderType = renderType;
    }
}