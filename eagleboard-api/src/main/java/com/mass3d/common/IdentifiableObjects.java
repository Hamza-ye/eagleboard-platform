package com.mass3d.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "identifiableObjects", namespace = DxfNamespaces.DXF_2_0)
public class IdentifiableObjects {

  private List<BaseIdentifiableObject> additions = new ArrayList<>();

  private List<BaseIdentifiableObject> deletions = new ArrayList<>();

  public IdentifiableObjects() {
  }

  @JsonProperty
  @JacksonXmlElementWrapper(localName = "additions", useWrapping = false, namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "addition", namespace = DxfNamespaces.DXF_2_0)
  public List<BaseIdentifiableObject> getAdditions() {
    return additions;
  }

  public void setAdditions(List<BaseIdentifiableObject> additions) {
    this.additions = additions;
  }

  @JsonProperty
  @JacksonXmlElementWrapper(localName = "deletions", useWrapping = false, namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "deletion", namespace = DxfNamespaces.DXF_2_0)
  public List<BaseIdentifiableObject> getDeletions() {
    return deletions;
  }

  public void setDeletions(List<BaseIdentifiableObject> deletions) {
    this.deletions = deletions;
  }

  @JsonProperty
  @JacksonXmlElementWrapper(localName = "identifiableObjects", useWrapping = false, namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "identifiableObject", namespace = DxfNamespaces.DXF_2_0)
  public List<BaseIdentifiableObject> getIdentifiableObjects() {
    return additions;
  }

  public void setIdentifiableObjects(List<BaseIdentifiableObject> identifiableObjects) {
    this.additions = identifiableObjects;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("additions", additions)
        .add("deletions", deletions)
        .toString();
  }
}
