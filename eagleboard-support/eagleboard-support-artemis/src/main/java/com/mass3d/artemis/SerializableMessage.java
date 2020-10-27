package com.mass3d.artemis;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import com.mass3d.common.DxfNamespaces;

public interface SerializableMessage extends Serializable, Message
{
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    MessageType getMessageType();
}
