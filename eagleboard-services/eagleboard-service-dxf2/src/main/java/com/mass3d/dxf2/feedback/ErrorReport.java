package com.mass3d.dxf2.feedback;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement(localName = "errorReport", namespace = DxfNamespaces.DXF_2_0)
public class ErrorReport {

  protected final ErrorMessage message;

  protected final Class<?> mainKlass;

  protected String mainId;

  protected Class<?> errorKlass;

  protected String errorProperty;

  protected Object value;

  public ErrorReport(Class<?> mainKlass, ErrorCode errorCode, Object... args) {
    this.mainKlass = mainKlass;
    this.message = new ErrorMessage(errorCode, args);
  }

  public ErrorReport(Class<?> mainKlass, ErrorMessage message) {
    this.mainKlass = mainKlass;
    this.message = message;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public ErrorCode getErrorCode() {
    return message.getErrorCode();
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getMessage() {
    return message.getMessage();
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Class<?> getMainKlass() {
    return mainKlass;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getMainId() {
    return mainId;
  }

  public ErrorReport setMainId(String mainId) {
    this.mainId = mainId;
    return this;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Class<?> getErrorKlass() {
    return errorKlass;
  }

  public ErrorReport setErrorKlass(Class<?> errorKlass) {
    this.errorKlass = errorKlass;
    return this;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getErrorProperty() {
    return errorProperty;
  }

  public ErrorReport setErrorProperty(String errorProperty) {
    this.errorProperty = errorProperty;
    return this;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Object getValue() {
    return value;
  }

  public ErrorReport setValue(Object value) {
    this.value = value;
    return this;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("message", getMessage())
        .add("errorCode", message.getErrorCode())
        .add("mainKlass", mainKlass)
        .add("errorKlass", errorKlass)
        .add("value", value)
        .toString();
  }
}
