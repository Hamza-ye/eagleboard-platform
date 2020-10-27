package com.mass3d.patch;

import com.fasterxml.jackson.databind.JsonNode;

public class PatchParams {

  private final Object source;

  private final Object target;

  private final JsonNode jsonNode;

  /**
   * Ignore properties that are not owned by the source class.
   */
  private boolean ignoreTransient;

  public PatchParams(Object source, Object target) {
    this.source = source;
    this.target = target;
    this.jsonNode = null;
  }

  public PatchParams(JsonNode jsonNode) {
    this.source = null;
    this.target = null;
    this.jsonNode = jsonNode;
  }

  public Object getSource() {
    return source;
  }

  public Object getTarget() {
    return target;
  }

  public JsonNode getJsonNode() {
    return jsonNode;
  }

  public boolean haveJsonNode() {
    return jsonNode != null;
  }

  public boolean isIgnoreTransient() {
    return ignoreTransient;
  }

  public PatchParams setIgnoreTransient(boolean ignoreTransient) {
    this.ignoreTransient = ignoreTransient;
    return this;
  }
}
