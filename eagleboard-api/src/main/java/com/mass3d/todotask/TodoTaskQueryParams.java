package com.mass3d.todotask;

import com.google.common.base.MoreObjects;
import java.util.HashSet;
import java.util.Set;

public class TodoTaskQueryParams {

  /**
   * Query string to match like name and exactly on UID and code.
   */
  private String query;

  /**
   * The levels of organisation units to include.
   */
  private Set<Integer> levels = new HashSet<>();

  /**
   * The maximum number of organisation unit levels to include, relative to the real root of the
   * hierarchy.
   */
  private Integer maxLevels;

  /**
   * The first result to include.
   */
  private Integer first;

  /**
   * The max number of results to include.
   */
  private Integer max;

  // -------------------------------------------------------------------------
  // Constructor
  // -------------------------------------------------------------------------

  public TodoTaskQueryParams() {
  }

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  public boolean hasQuery() {
    return query != null && !query.isEmpty();
  }

  public boolean hasLevels() {
    return levels != null && !levels.isEmpty();
  }

  public void setLevel(Integer level) {
    if (level != null) {
      levels.add(level);
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).
        add("query", query).
    add("levels", levels).
            add("maxLevels", maxLevels).
            add("first", first).
            add("max", max).toString();
  }

  // -------------------------------------------------------------------------
  // Getters and setters
  // -------------------------------------------------------------------------

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public Set<Integer> getLevels() {
    return levels;
  }

  public void setLevels(Set<Integer> levels) {
    this.levels = levels;
  }

  public Integer getMaxLevels() {
    return maxLevels;
  }

  public void setMaxLevels(Integer maxLevels) {
    this.maxLevels = maxLevels;
  }

  public Integer getFirst() {
    return first;
  }

  public void setFirst(Integer first) {
    this.first = first;
  }

  public Integer getMax() {
    return max;
  }

  public void setMax(Integer max) {
    this.max = max;
  }
}
