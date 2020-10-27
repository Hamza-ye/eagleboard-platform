package com.mass3d.dxf2.webmessage.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dxf2.webmessage.AbstractWebMessageResponse;
import com.mass3d.fileresource.FileResource;

public class FileResourceWebMessageResponse
    extends AbstractWebMessageResponse
{
    private FileResource fileResource;

    public FileResourceWebMessageResponse( FileResource fileResource )
    {
        this.setResponseType( FileResource.class.getSimpleName() );
        this.fileResource = fileResource;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public FileResource getFileResource()
    {
        return fileResource;
    }
}
