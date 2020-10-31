package com.mass3d.program.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "programMessageBatch", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramMessageBatch
{
    private List<ProgramMessage> programMessages = new ArrayList<>();

    public ProgramMessageBatch()
    {
    }

    public ProgramMessageBatch( List<ProgramMessage> programMessages )
    {
        this.programMessages = programMessages;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "programMessages", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "programMessage", namespace = DxfNamespaces.DXF_2_0 )
    public List<ProgramMessage> getProgramMessages()
    {
        return programMessages;
    }

    public void setProgramMessages( List<ProgramMessage> programMessages )
    {
        this.programMessages = programMessages;
    }
}
