package com.mass3d.cache;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public interface Cache<V> {

  /**
   * Returns the value associated with the {@code key} in this cache instance,
   * or {@code Optional.empty()} if there is no cached value.Note: This method
   * will NOT return the defaultValue in case of absence of associated cache
   * value.
   *
   * @param key the key whose associated value is to be retrieved
   * @return the value wrapped in Optional, or {@code Optional.empty()}
   */
  Optional<V> getIfPresent( String key );

  /**
   * Returns the value associated with the {@code key} in this cache instance,
   * or {@code defaultValue} if there is no cached value.Note: This method
   * will return the defaultValue in case of absence of associated cache
   * value. But will not store the default value into the cache.
   *
   * @param key the key whose associated value is to be retrieved
   * @return the value wrapped in Optional, or
   *         {@code Optional of defaultValue}
   */
  Optional<V> get( String key );

  /**
   * Returns the value mapped to {@code key} in this cache instance, obtaining
   * that value from the {@code mappingFunction} if necessary. This method
   * provides a simple substitute for the conventional "if cached, return;
   * otherwise create, cache and return" pattern. If value is null, the given
   * mapping function is evaluated and inserted into this cache unless
   * {@code null}. Note: This method will return the defaultValue in case of
   * absence of associated cache value. But will not store the default value
   * into the cache.
   *
   * @param key the key for retrieving the value
   * @param mappingFunction the function to compute a value.
   * @return an optional containing current (existing or computed) value, or
   *         Optional.empty() if the computed value is null
   * @throws IllegalArgumentException if the specified mappingFunction is null
   */
  Optional<V> get( String key, Function<String, V> mappingFunction );

  /**
   * Returns a collection of all the values in the cache
   *
   * @return collection with all cached values
   */
  Collection<V> getAll();

  /**
   * Associates the {@code value} with the {@code key} in this cache. If the
   * cache previously contained a value associated with the {@code key}, the
   * old value is replaced by the new {@code value}.Prefer
   * {@link #get(String, Function)} when using the conventional "if cached,
   * return; otherwise create, cache and return" pattern.
   *
   * @param key the key for the value
   * @param value value to be mapped to the key
   * @throws IllegalArgumentException if the specified value is null
   */
  void put( String key, V value );

  /**
   * Associates the {@code value} with the {@code key} in this cache. If the
   * cache previously contained a value associated with the {@code key}, the
   * old value is replaced by the new {@code value}. It also sets a custom
   * time to live for the given key, which overrides the cache's default.
   *
   * @param key the key for the value
   * @param value value to be mapped to the key
   * @param ttlInSeconds the time to live for the key, in seconds
   * @throws IllegalArgumentException if the specified value is null
   */
  void put( String key, V value, long ttlInSeconds );

  /**
   * Discards any cached value for the {@code key}. The behavior of this
   * operation is undefined for an entry that is being loaded and is otherwise
   * not present.
   *
   * @param key the key whose mapping is to be removed from the cache
   */
  void invalidate( String key );

  /**
   * Discards all entries in this cache instance. If a shared cache is used,
   * this method does not clear anything.
   */
  void invalidateAll();

  /**
   * Returns the type of the cache. IN_MEMORY or REDIS or NONE.
   *
   * @return
   */
  CacheType getCacheType();
}
