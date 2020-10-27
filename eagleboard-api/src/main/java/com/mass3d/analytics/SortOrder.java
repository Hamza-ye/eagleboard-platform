package com.mass3d.analytics;

public enum SortOrder {
  ASC("asc"),
  DESC("desc");

  private String value;

  SortOrder(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
