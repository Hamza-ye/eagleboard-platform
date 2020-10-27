package com.mass3d.indicator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;

@JacksonXmlRootElement( localName = "indicatorType", namespace = DxfNamespaces.DXF_2_0 )
public class IndicatorType
    extends BaseIdentifiableObject implements MetadataObject
{
    private int factor;

    private boolean number;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public IndicatorType()
    {

    }

    public IndicatorType( String name, int factor, Boolean number )
    {
        this.name = name;
        this.factor = factor;
        this.number = number;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getFactor()
    {
        return factor;
    }

    public void setFactor( int factor )
    {
        this.factor = factor;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isNumber()
    {
        return number;
    }

    public void setNumber( boolean number )
    {
        this.number = number;
    }
}
