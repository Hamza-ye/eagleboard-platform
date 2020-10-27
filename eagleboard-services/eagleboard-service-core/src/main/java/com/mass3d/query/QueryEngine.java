package com.mass3d.query;

import java.util.List;

public interface QueryEngine<T> {

  /**
   * Return objects matching given query, T typed according to QueryEngine implementation.
   *
   * @param query Query instance to use
   * @return Matching objects
   */
  List<T> query(Query query);

  /**
   * Returns how many objects matches the given query.
   *
   * @param query Query instance to use
   * @return N number of matching objects
   */
  int count(Query query);
}
