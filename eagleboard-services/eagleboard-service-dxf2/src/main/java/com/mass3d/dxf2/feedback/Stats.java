package com.mass3d.dxf2.feedback;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement(localName = "stats", namespace = DxfNamespaces.DXF_2_0)
public class Stats {

  private int created;

  private int updated;

  private int deleted;

  private int ignored;

  public Stats() {
  }

  public void merge(Stats stats) {
    created += stats.getCreated();
    updated += stats.getUpdated();
    deleted += stats.getDeleted();
    ignored += stats.getIgnored();
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getTotal() {
    return created + updated + deleted + ignored;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getCreated() {
    return created;
  }

  public void incCreated() {
    created++;
  }

  public void incCreated(int n) {
    created += n;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getUpdated() {
    return updated;
  }

  public void incUpdated() {
    updated++;
  }

  public void incUpdated(int n) {
    updated += n;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getDeleted() {
    return deleted;
  }

  public void incDeleted() {
    deleted++;
  }

  public void incDeleted(int n) {
    deleted += n;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getIgnored() {
    return ignored;
  }

  public void incIgnored() {
    ignored++;
  }

  public void incIgnored(int n) {
    ignored += n;
  }

  public void ignored() {
    ignored += created;
    ignored += updated;
    ignored += deleted;

    created = 0;
    updated = 0;
    deleted = 0;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("created", created)
        .add("updated", updated)
        .add("deleted", deleted)
        .add("ignored", ignored)
        .toString();
  }
}
