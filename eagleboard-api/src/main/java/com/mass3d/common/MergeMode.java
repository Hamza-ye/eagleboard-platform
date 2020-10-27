package com.mass3d.common;

public enum MergeMode {
  MERGE_ALWAYS, MERGE_IF_NOT_NULL,
  MERGE, REPLACE, NONE;

  public boolean isMerge() {
    return this == MERGE_IF_NOT_NULL || this == MERGE;
  }

  public boolean isReplace() {
    return this == MERGE_ALWAYS || this == REPLACE;
  }

  public boolean isNone() {
    return this == NONE;
  }
}
