package com.mass3d.webapi.webdomain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.Pager;
import com.mass3d.dxf2.metadata.Metadata;

public class WebMetadata
    extends Metadata
{
    private Pager pager;

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Pager getPager()
    {
        return pager;
    }

    public void setPager( Pager pager )
    {
        this.pager = pager;
    }
}
