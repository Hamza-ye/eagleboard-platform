package com.mass3d.common;

public enum Compression {
  NONE,
  GZIP,
  ZIP;

  public static Compression fromValue(String compression) {
    for (Compression comp : Compression.values()) {
      if (comp.name().equalsIgnoreCase(compression)) {
        return comp;
      }
    }
    return null;
  }
}
