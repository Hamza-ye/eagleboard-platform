package com.mass3d.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "deliveryChannel", namespace = DxfNamespaces.DXF_2_0)
public enum DeliveryChannel {
  SMS, EMAIL
}
