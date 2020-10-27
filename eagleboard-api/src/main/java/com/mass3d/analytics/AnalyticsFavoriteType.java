package com.mass3d.analytics;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement(localName = "analyticsFavoriteType", namespace = DxfNamespaces.DXF_2_0)
public enum AnalyticsFavoriteType {
  REPORT_TABLE,
  CHART,
  MAP,
  EVENT_REPORT,
  EVENT_CHART,
  DATASET_REPORT,
  FIELDSET_REPORT,
  TODOTASK_REPORT,
  ACTIVITY_REPORT,
  PROJECT_REPORT
}
