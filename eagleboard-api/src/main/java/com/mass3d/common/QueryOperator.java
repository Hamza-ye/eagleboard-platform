package com.mass3d.common;

public enum QueryOperator {
  EQ("="), GT(">"), GE(">="), LT("<"), LE("<="), NE("!="), LIKE("like"), IN("in");

  private final String value;

  QueryOperator(String value) {
    this.value = value;
  }

  public static QueryOperator fromString(String string) {
    if (string == null || string.isEmpty()) {
      return null;
    }

    return valueOf(string.toUpperCase());
  }

  public String getValue() {
    return value;
  }
}
