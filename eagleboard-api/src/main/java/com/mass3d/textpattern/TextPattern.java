package com.mass3d.textpattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.Objects;

/**
 * This class represents a TextPattern - A String that is used to generate and validate a
 * user-defined patterns. Example pattern: "Current date: " + CURRENT_DATE("DD-MM-yyyy")
 * <p>
 * Read more about patterns in TextPatternMethod.
 *
 */
public class TextPattern
    implements Serializable {

  private ImmutableList<TextPatternSegment> segments;

  private Objects ownerObject;

  private String ownerUid;

  public TextPattern() {
    this.segments = ImmutableList.of();
  }

  public TextPattern(List<TextPatternSegment> segments) {
    this.segments = ImmutableList.copyOf(segments);
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getOwnerUid() {
    return ownerUid;
  }

  public void setOwnerUid(String ownerUid) {
    this.ownerUid = ownerUid;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Objects getOwnerObject() {
    return ownerObject;
  }

  public void setOwnerObject(Objects ownerObject) {
    this.ownerObject = ownerObject;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public List<TextPatternSegment> getSegments() {
    return this.segments;
  }

  public void setSegments(ArrayList<TextPatternSegment> segments) {
    this.segments = ImmutableList.copyOf(segments);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TextPattern that = (TextPattern) o;

    return java.util.Objects.equals(segments, that.segments) &&
        ownerObject == that.ownerObject &&
        java.util.Objects.equals(ownerUid, that.ownerUid);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(segments, ownerObject, ownerUid);
  }
}
