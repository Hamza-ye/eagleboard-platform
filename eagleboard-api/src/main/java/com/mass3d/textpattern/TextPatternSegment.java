package com.mass3d.textpattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.Objects;
import com.mass3d.common.DxfNamespaces;

public class TextPatternSegment {

  private TextPatternMethod method;

  private String parameter;

  public TextPatternSegment() {
    this.parameter = "";
  }

  public TextPatternSegment(TextPatternMethod method, String segment) {
    this.method = method;
    this.parameter = method.getType().getParam(segment);
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public TextPatternMethod getMethod() {
    return method;
  }

  public void setMethod(TextPatternMethod method) {
    this.method = method;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }


  /* Helper methods */

  /**
   * Recreates the original segment text from the method name and segment parameter.
   *
   * @return The original segment based on method and parameter
   */
  @JsonIgnore
  public String getRawSegment() {
    if (method.equals(TextPatternMethod.TEXT)) {
      return "\"" + parameter + "\"";
    } else {
      return String.format("%s(%s)", method.name(), parameter);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TextPatternSegment that = (TextPatternSegment) o;

    return method == that.method &&
        Objects.equals(parameter, that.parameter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(method, parameter);
  }
}
