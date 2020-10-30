package com.mass3d.appmanager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "appIcons", namespace = DxfNamespaces.DXF_2_0 )
public class AppIcons
    implements Serializable
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 5041924160867190242L;

    /**
     * Optional.
     */
    @JsonProperty( "16" )
    @JacksonXmlProperty( localName = "icon_16", namespace = DxfNamespaces.DXF_2_0 )
    private String icon16;

    @JsonProperty( "48" )
    @JacksonXmlProperty( localName = "icon_48", namespace = DxfNamespaces.DXF_2_0 )
    private String icon48;

    @JsonProperty( "128" )
    @JacksonXmlProperty( localName = "icon_128", namespace = DxfNamespaces.DXF_2_0 )
    private String icon128;

    public String getIcon16()
    {
        return icon16;
    }

    public void setIcon16( String icon16 )
    {
        this.icon16 = icon16;
    }

    public String getIcon48()
    {
        return icon48;
    }

    public void setIcon48( String icon48 )
    {
        this.icon48 = icon48;
    }

    public String getIcon128()
    {
        return icon128;
    }

    public void setIcon128( String icon128 )
    {
        this.icon128 = icon128;
    }
}