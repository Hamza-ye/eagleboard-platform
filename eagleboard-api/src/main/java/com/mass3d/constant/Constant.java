package com.mass3d.constant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.BaseNameableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;

@JacksonXmlRootElement( localName = "constant", namespace = DxfNamespaces.DXF_2_0 )
public class Constant
    extends BaseNameableObject implements MetadataObject
{
    // -------------------------------------------------------------------------
    // Variables
    // -------------------------------------------------------------------------

    private double value;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Constant()
    {
    }

    public Constant( String name )
    {
        this.name = name;
    }

    public Constant( String name, double value )
    {
        this.name = name;
        this.value = value;
    }

    // -------------------------------------------------------------------------
    // Getter & Setter
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public double getValue()
    {
        return value;
    }

    public void setValue( double value )
    {
        this.value = value;
    }
}
