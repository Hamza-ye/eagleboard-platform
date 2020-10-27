package com.mass3d.outboundmessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement(localName = "messageResponseStatus", namespace = DxfNamespaces.DXF_2_0)
public class OutboundMessageResponse {

  private String description;

  private boolean ok;

  private Enum<?> responseObject;

  public OutboundMessageResponse() {
  }

  public OutboundMessageResponse(String description, Enum<?> response, boolean ok) {
    this.ok = ok;
    this.responseObject = response;
    this.description = description;
  }

  @JsonProperty(value = "status")
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Enum<?> getResponseObject() {
    return responseObject;
  }

  public void setResponseObject(Enum<?> response) {
    this.responseObject = response;
  }

  @JsonProperty(value = "description")
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isOk() {
    return ok;
  }

  public void setOk(boolean ok) {
    this.ok = ok;
  }
}
