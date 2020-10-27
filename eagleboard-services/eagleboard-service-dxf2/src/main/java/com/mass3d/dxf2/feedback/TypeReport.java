package com.mass3d.dxf2.feedback;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.mass3d.common.DxfNamespaces;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JacksonXmlRootElement(localName = "typeReport", namespace = DxfNamespaces.DXF_2_0)
public class TypeReport {

  private Class<?> klass;

  private Stats stats = new Stats();

  private Map<Integer, ObjectReport> objectReportMap = new HashMap<>();

  @JsonCreator
  public TypeReport(@JsonProperty("klass") Class<?> klass) {
    this.klass = klass;
  }

  //-----------------------------------------------------------------------------------
  // Utility Methods
  //-----------------------------------------------------------------------------------

  public void merge(TypeReport typeReport) {
    stats.merge(typeReport.getStats());

    typeReport.getObjectReportMap().forEach((index, objectReport) -> {
      if (!objectReportMap.containsKey(index)) {
        objectReportMap.put(index, objectReport);
        return;
      }

      objectReportMap.get(index).addErrorReports(objectReport.getErrorReports());
    });
  }

  public void addObjectReport(ObjectReport objectReport) {
    if (!objectReportMap.containsKey(objectReport.getIndex())) {
      objectReportMap.put(objectReport.getIndex(), objectReport);
      return;
    }

    objectReportMap.get(objectReport.getIndex()).merge(objectReport);
  }

  //-----------------------------------------------------------------------------------
  // Getters and Setters
  //-----------------------------------------------------------------------------------

  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  public Class<?> getKlass() {
    return klass;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Stats getStats() {
    return stats;
  }

  @JsonProperty
  @JacksonXmlElementWrapper(localName = "objectReports", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "objectReport", namespace = DxfNamespaces.DXF_2_0)
  public List<ObjectReport> getObjectReports() {
    List<ObjectReport> objectReports = new ArrayList<>();
    objectReportMap.values().forEach(objectReports::add);

    return objectReports;
  }

  @JsonProperty
  @JacksonXmlElementWrapper(localName = "objectReports", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "objectReport", namespace = DxfNamespaces.DXF_2_0)
  public void setObjectReports(List<ObjectReport> objectReports) {
    if (objectReports != null) {
      objectReports.forEach(or -> objectReportMap.put(or.getIndex(), or));
    }
  }

  public List<ErrorReport> getErrorReports() {
    List<ErrorReport> errorReports = new ArrayList<>();
    objectReportMap.values()
        .forEach(objectReport -> errorReports.addAll(objectReport.getErrorReports()));

    return errorReports;
  }

  public Map<Integer, ObjectReport> getObjectReportMap() {
    return objectReportMap;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("klass", klass)
        .add("stats", stats)
        .add("objectReports", getObjectReports())
        .toString();
  }
}
