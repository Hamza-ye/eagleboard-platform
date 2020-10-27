package com.mass3d.patch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.EmbeddedObject;
import com.mass3d.common.IdentifiableObject;

@JacksonXmlRootElement(localName = "mutation", namespace = DxfNamespaces.DXF_2_0)
public class Mutation {

  /**
   * Full dot separated path of this mutation (e.g a.b.c).
   */
  private final String path;

  /**
   * New value for given path.
   */
  private final Object value;

  /**
   * Is the mutation an ADD or DEL, this mainly applies to collections.
   */
  private Operation operation = Operation.ADDITION;

  public Mutation(String path) {
    this.path = path;
    this.value = null;
  }

  public Mutation(String path, Object value) {
    this.path = path;
    this.value = value;
  }

  public Mutation(String path, Object value, Operation operation) {
    this.path = path;
    this.value = value;
    this.operation = operation;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getPath() {
    return path;
  }

  public Object getValue() {
    return value;
  }

  @JsonProperty("value")
  @JacksonXmlProperty(localName = "value", namespace = DxfNamespaces.DXF_2_0)
  public Object getLogValue() {
    if (IdentifiableObject.class.isInstance(value) && !EmbeddedObject.class.isInstance(value)) {
      return ((IdentifiableObject) value).getUid();
    }

    return value;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Operation getOperation() {
    return operation;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("path", path)
        .add("operation", operation)
        .add("value", getLogValue())
        .toString();
  }

  enum Operation {
    ADDITION, DELETION
  }
}
