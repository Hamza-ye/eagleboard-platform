package com.mass3d.trackedentityfilter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import com.mass3d.common.DxfNamespaces;

public class FilterPeriod implements Serializable
{
    private int periodFrom;

    private int periodTo;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public FilterPeriod()
    {

    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getPeriodFrom()
    {
        return periodFrom;
    }

    public void setPeriodFrom( int periodFrom )
    {
        this.periodFrom = periodFrom;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getPeriodTo()
    {
        return periodTo;
    }

    public void setPeriodTo( int periodTo )
    {
        this.periodTo = periodTo;
    }

}
