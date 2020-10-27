package com.mass3d.common.comparator;

import java.util.Comparator;
import java.util.Objects;

/**
 * Converts the given objects based on their string value. Null objects are interpreted as empty
 * strings.
 *
 */
public class ObjectStringValueComparator
    implements Comparator<Object> {

  public static final ObjectStringValueComparator INSTANCE = new ObjectStringValueComparator();

  private static final String NULL_REPLACEMENT = "";

  @Override
  public int compare(Object o1, Object o2) {
    return Objects.toString(o1, NULL_REPLACEMENT).compareTo(Objects.toString(o2, NULL_REPLACEMENT));
  }
}
