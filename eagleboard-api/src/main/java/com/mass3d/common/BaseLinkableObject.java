package com.mass3d.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.annotation.Property;

@JacksonXmlRootElement(localName = "linkableObject", namespace = DxfNamespaces.DXF_2_0)
public class BaseLinkableObject
    implements LinkableObject {

  /**
   * As part of the serializing process, this field can be set to indicate a link to this
   * identifiable object (will be used on the web layer for navigating the REST API)
   */
  private transient String href;

  @Override
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  @Property(PropertyType.URL)
  public String getHref() {
    return href;
  }

  @Override
  public void setHref(String href) {
    this.href = href;
  }
}
