package com.mass3d.analytics;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "userOrgUnitType", namespace = DxfNamespaces.DXF_2_0 )
public enum UserOrgUnitType
{
    DATA_CAPTURE, DATA_OUTPUT, TEI_SEARCH
}
