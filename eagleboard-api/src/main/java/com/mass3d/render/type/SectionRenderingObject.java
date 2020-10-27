package com.mass3d.render.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;

/**
 * This class represents the renderingType of a ProgramStageSection
 */
@JacksonXmlRootElement( namespace = DxfNamespaces.DXF_2_0 )
public class SectionRenderingObject implements RenderingObject<SectionRenderingType>
{
    /**
     * The renderingType of the ProgramStageSection
     */
    private SectionRenderingType type;

    @Override
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public SectionRenderingType getType()
    {
        return type;
    }

    @Override
    public void setType( SectionRenderingType type )
    {
        this.type = type;
    }

    @Override
    @JsonIgnore
    public Class<SectionRenderingType> getRenderTypeClass()
    {
        return SectionRenderingType.class;
    }
}
