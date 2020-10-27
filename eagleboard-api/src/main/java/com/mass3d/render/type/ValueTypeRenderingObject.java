package com.mass3d.render.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mass3d.common.DxfNamespaces;

/**
 * This class represents the DataElement/TrackedEntityAttribute ValueType based rendering type
 *
 * The min, max, step and decimal properties in this class does not represent the data validation, it only serves as
 * a guideline on how form elements should be defined (IE: Sliders, spinners, etc)
 */
public class ValueTypeRenderingObject implements RenderingObject<ValueTypeRenderingType>
{
    /**
     * The renderingType
     */
    private ValueTypeRenderingType type;

    // For numerical types

    /**
     * The minimum value the numerical type can be
     */
    private Integer min;

    /**
     * The maximum value the numerical type an be
     */
    private Integer max;

    /**
     * The size of each step in the form element
     */
    private Integer step;

    /**
     * The number of decimal points that should be considered
     */
    private Integer decimalPoints;

    //------------------------------------------
    // Constructors
    //------------------------------------------

    public ValueTypeRenderingObject()
    {
        this.type = ValueTypeRenderingType.DEFAULT;
    }

    public ValueTypeRenderingObject( ValueTypeRenderingType type )
    {
        this.type =  type;
    }

    //------------------------------------------
    // Getters & Setters
    //------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Integer getDecimalPoints()
    {
        return decimalPoints;
    }

    public void setDecimalPoints( Integer decimalPoints )
    {
        this.decimalPoints = decimalPoints;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Integer getStep()
    {
        return step;
    }

    public void setStep( Integer step )
    {
        this.step = step;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Integer getMax()
    {
        return max;
    }

    public void setMax( Integer max )
    {
        this.max = max;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Integer getMin()
    {
        return min;
    }

    public void setMin( Integer min )
    {
        this.min = min;
    }

    @Override
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ValueTypeRenderingType getType()
    {
        return type;
    }

    @Override
    public void setType( ValueTypeRenderingType renderingType )
    {
        this.type = renderingType;
    }

    @Override
    @JsonIgnore
    public Class<ValueTypeRenderingType> getRenderTypeClass()
    {
        return ValueTypeRenderingType.class;
    }
}
