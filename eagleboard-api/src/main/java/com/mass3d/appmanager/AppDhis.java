package com.mass3d.appmanager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "appDhis", namespace = DxfNamespaces.DXF_2_0 )
public class AppDhis
    implements Serializable
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = -8854371580010728182L;

    @JsonProperty( "href" )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    private String href;

    @JsonProperty( "namespace" )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    private String namespace;

    public String getHref()
    {
        return href;
    }

    public void setHref( String href )
    {
        this.href = href;
    }

    public String getNamespace()
    {
        return namespace;
    }

    public void setNamespace( String namespace )
    {
        this.namespace = namespace;
    }
}