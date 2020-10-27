package com.mass3d.common;

import java.util.HashMap;
import java.util.List;

public class ListMapMap<T, U, V>
    extends HashMap<T, ListMap<U, V>> {

  /**
   * Determines if a de-serialized file is compatible with this class.
   */
  private static final long serialVersionUID = -8123821997295429997L;

  public ListMapMap() {
    super();
  }

  public ListMap<U, V> putValue(T key1, U key2, V value) {
    ListMap<U, V> listMap = this.get(key1);
    listMap = listMap == null ? new ListMap<>() : listMap;
    listMap.putValue(key2, value);
    super.put(key1, listMap);
    return null;
  }

  public List<V> getValues(T key1, U key2) {
    return this.get(key1) == null ? null : this.get(key1).get(key2);
  }
}
