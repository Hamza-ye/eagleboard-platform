package com.mass3d.common;

import static com.mass3d.common.DimensionalObjectUtils.COMPOSITE_DIM_OBJECT_PLAIN_SEP;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import com.mass3d.dataset.DataSet;

@JacksonXmlRootElement(localName = "reportingRate", namespace = DxfNamespaces.DXF_2_0)
public class ReportingRate
    extends BaseDimensionalItemObject implements EmbeddedObject {

  private DataSet dataSet;

  private ReportingRateMetric metric;

  public ReportingRate() {
  }

  public ReportingRate(DataSet dataSet) {
    this.dataSet = dataSet;
    this.metric = ReportingRateMetric.REPORTING_RATE;
  }

  public ReportingRate(DataSet dataSet, ReportingRateMetric metric) {
    this.dataSet = dataSet;
    this.metric = metric;
  }

  // -------------------------------------------------------------------------
  // DimensionalItemObject
  // -------------------------------------------------------------------------

  @Override
  public String getUid() {
    return dataSet.getUid();
  }

  @Override
  public String getName() {
    String metricName =
        metric != null ? metric.displayName() : ReportingRateMetric.REPORTING_RATE.displayName();

    return dataSet.getName() + " " + metricName;
  }

  @Override
  public String getShortName() {
    String metricName =
        metric != null ? metric.displayName() : ReportingRateMetric.REPORTING_RATE.displayName();

    return dataSet.getShortName() + " " + metricName;
  }

  @Override
  public String getDimensionItem() {
    return dataSet.getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + metric.name();
  }

  @Override
  public DimensionItemType getDimensionItemType() {
    return DimensionItemType.REPORTING_RATE;
  }

//  @Override
//  public List<LegendSet> getLegendSets() {
//    return dataSet.getLegendSets();
//  }

//    @Override
//    public TotalAggregationType getTotalAggregationType()
//    {
//        return TotalAggregationType.AVERAGE;
//    }
//
  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  @JsonProperty
  @JsonSerialize(as = BaseNameableObject.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public DataSet getDataSet() {
    return dataSet;
  }

  public void setDataSet(DataSet dataSet) {
    this.dataSet = dataSet;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public ReportingRateMetric getMetric() {
    return metric;
  }

  public void setMetric(ReportingRateMetric metric) {
    this.metric = metric;
  }
}
