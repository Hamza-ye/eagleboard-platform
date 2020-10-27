package com.mass3d.patch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement(localName = "patch", namespace = DxfNamespaces.DXF_2_0)
public class Patch {

  private List<Mutation> mutations = new ArrayList<>();

  public Patch() {
  }

  public Patch(List<Mutation> mutations) {
    this.mutations = mutations;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public List<Mutation> getMutations() {
    return mutations;
  }

  public void setMutations(List<Mutation> mutations) {
    this.mutations = mutations;
  }

  public Patch addMutation(Mutation mutation) {
    if (mutation != null) {
      mutations.add(mutation);
    }

    return this;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("mutations", mutations)
        .toString();
  }
}
