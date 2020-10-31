package com.mass3d.program.message;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "programMessageStatus", namespace = DxfNamespaces.DXF_2_0 )
public enum ProgramMessageStatus
{
    SENT, FAILED, SCHEDULED, OUTBOUND
}
