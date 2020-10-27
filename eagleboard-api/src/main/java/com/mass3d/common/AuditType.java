package com.mass3d.common;

public enum AuditType {
  CREATE("create"), UPDATE("update"), DELETE("delete"), READ("read"), SEARCH("search");

  private final String value;

  AuditType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
