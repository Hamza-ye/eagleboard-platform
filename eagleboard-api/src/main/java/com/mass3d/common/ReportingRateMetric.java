package com.mass3d.common;

public enum ReportingRateMetric {
  REPORTING_RATE("reporting_rate", "Reporting rate"),
  REPORTING_RATE_ON_TIME("reporting_rate_on_time", "Reporting rate on time"),
  ACTUAL_REPORTS("actual_reports", "Actual reports"),
  ACTUAL_REPORTS_ON_TIME("actual_reports_on_time", "Actual reports on time"),
  EXPECTED_REPORTS("expected_reports", "Expected reports");

  private String key;

  private String displayName;

  ReportingRateMetric(String key, String displayName) {
    this.displayName = displayName;
  }

  public String key() {
    return key;
  }

  public String displayName() {
    return displayName;
  }
}
