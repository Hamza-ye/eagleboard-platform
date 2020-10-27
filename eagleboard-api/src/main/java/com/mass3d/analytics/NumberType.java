package com.mass3d.analytics;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "numberType", namespace = DxfNamespaces.DXF_2_0 )
public enum NumberType
{
    VALUE, 
    ROW_PERCENTAGE, 
    COLUMN_PERCENTAGE;
}
