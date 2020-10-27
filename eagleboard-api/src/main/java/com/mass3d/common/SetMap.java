package com.mass3d.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class SetMap<T, V>
    extends HashMap<T, Set<V>> {

  public SetMap() {
    super();
  }

  public SetMap(SetMap<T, V> setMap) {
    super(setMap);
  }

  /**
   * Produces a SetMap based on the given set of values. The key for each entry is produced by
   * applying the given keyMapper function.
   *
   * @param values the values of the map.
   * @param keyMapper the function producing the key for each entry.
   * @return a SetMap.
   */
  public static <T, V> SetMap<T, V> getSetMap(Set<V> values, Function<V, T> keyMapper) {
    SetMap<T, V> map = new SetMap<>();

    for (V value : values) {
      T key = keyMapper.apply(value);

      map.putValue(key, value);
    }

    return map;
  }

  public Set<V> putValue(T key, V value) {
    Set<V> set = this.get(key);
    set = set == null ? new HashSet<>() : set;
    set.add(value);
    return super.put(key, set);
  }

  public Set<V> putValues(T key, Set<V> values) {
    Set<V> set = this.get(key);
    set = set == null ? new HashSet<>() : set;
    set.addAll(values);
    return super.put(key, set);
  }

  public void putValues(SetMap<T, V> setMap) {
    setMap.forEach((k, v) -> putValues(k, v));
  }
}
