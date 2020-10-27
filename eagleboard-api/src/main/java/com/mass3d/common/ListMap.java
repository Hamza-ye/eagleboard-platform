package com.mass3d.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ListMap<T, V>
    extends HashMap<T, List<V>> {

  /**
   * Determines if a de-serialized file is compatible with this class.
   */
  private static final long serialVersionUID = 4880664228933342003L;

  public ListMap() {
    super();
  }

  public ListMap(ListMap<T, V> listMap) {
    super(listMap);
  }

  /**
   * Produces a ListMap based on the given list of values. The key for each entry is produced by
   * applying the given keyMapper function.
   *
   * @param values the values of the map.
   * @param keyMapper the function producing the key for each entry.
   * @return a ListMap.
   */
  public static <T, V> ListMap<T, V> getListMap(List<V> values, Function<V, T> keyMapper) {
    ListMap<T, V> map = new ListMap<>();

    for (V value : values) {
      T key = keyMapper.apply(value);

      map.putValue(key, value);
    }

    return map;
  }

  /**
   * Produces a ListMap based on the given list of values. The key for each entry is produced by
   * applying the given keyMapper function. The value for each entry is produced by applying the
   * given valueMapper function.
   *
   * @param values the values of the map.
   * @param keyMapper the function producing the key for each entry.
   * @param valueMapper the function producing the value for each entry.
   * @return a ListMap.
   */
  public static <T, U, V> ListMap<T, U> getListMap(List<V> values, Function<V, T> keyMapper,
      Function<V, U> valueMapper) {
    ListMap<T, U> map = new ListMap<>();

    for (V value : values) {
      T key = keyMapper.apply(value);
      U val = valueMapper.apply(value);

      map.putValue(key, val);
    }

    return map;
  }

  /**
   * Returns a union of two same-type ListMaps. Either or both of the input ListMaps may be null.
   * The returned ListMap never is.
   *
   * @param a one ListMap.
   * @param b the other ListMap.
   * @return union of the two ListMaps.
   */
  public static <T, V> ListMap<T, V> union(ListMap<T, V> a, ListMap<T, V> b) {
    if (a == null || a.isEmpty()) {
      if (b == null || b.isEmpty()) {
        return new ListMap<T, V>();
      }

      return b;
    } else if (b == null || b.isEmpty()) {
      return a;
    }

    ListMap<T, V> c = new ListMap<T, V>(a);

    for (Entry<T, List<V>> entry : b.entrySet()) {
      for (V value : entry.getValue()) {
        c.putValue(entry.getKey(), value);
      }
    }

    return c;
  }

  public List<V> putValue(T key, V value) {
    List<V> list = this.get(key);
    list = list == null ? new ArrayList<>() : list;
    list.add(value);
    super.put(key, list);
    return null;
  }

  public List<V> putValues(T key, Collection<V> values) {
    for (V value : values) {
      putValue(key, value);
    }

    return null;
  }

  public void putValueMap(Map<T, V> map) {
    for (Entry<T, V> entry : map.entrySet()) {
      putValue(entry.getKey(), entry.getValue());
    }
  }

  public Collection<V> allValues() {
    Collection<V> results = new ArrayList<>();

    for (Entry<T, List<V>> entry : entrySet()) {
      results.addAll(entry.getValue());
    }

    return results;
  }

  public Set<V> uniqueValues() {
    Set<V> results = new HashSet<>();

    for (Entry<T, List<V>> entry : entrySet()) {
      results.addAll(entry.getValue());
    }

    return results;
  }

  public boolean containsValue(T key, V value) {
    List<V> list = this.get(key);

    if (list == null) {
      return false;
    }

    return list.contains(value);

  }
}
