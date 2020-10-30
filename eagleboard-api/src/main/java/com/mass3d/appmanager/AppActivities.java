package com.mass3d.appmanager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "appActivities", namespace = DxfNamespaces.DXF_2_0 )
public class AppActivities
    implements Serializable
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 7530768303537807631L;

    @JsonProperty( "dhis" )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    private AppDhis dhis;

    public AppDhis getDhis()
    {
        return dhis;
    }

    public void setDhis( AppDhis dhis )
    {
        this.dhis = dhis;
    }
}