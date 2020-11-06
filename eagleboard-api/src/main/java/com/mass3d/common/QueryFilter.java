package com.mass3d.common;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.List;

public class QueryFilter {

  public static final String OPTION_SEP = ";";

  public static final ImmutableMap<QueryOperator, String> OPERATOR_MAP = ImmutableMap.<QueryOperator, String>builder()
      .put(QueryOperator.EQ, "=")
      .put(QueryOperator.GT, ">")
      .put(QueryOperator.GE, ">=")
      .put(QueryOperator.LT, "<")
      .put(QueryOperator.LE, "<=")
      .put(QueryOperator.NE, "!=")
      .put(QueryOperator.LIKE, "like")
      .put(QueryOperator.IN, "in").build();

  protected QueryOperator operator;

  protected String filter;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public QueryFilter() {
  }

  public QueryFilter(QueryOperator operator, String filter) {
    this.operator = operator;
    this.filter = filter;
  }

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  /**
   * Returns the items of the filter.
   *
   * @param encodedFilter the encoded filter.
   */
  public static List<String> getFilterItems(String encodedFilter) {
    return Lists.newArrayList(encodedFilter.split(OPTION_SEP));
  }

  public boolean isFilter() {
    return operator != null && filter != null && !filter.isEmpty();
  }

  public boolean isOperator(QueryOperator op) {
    return operator != null && operator.equals(op);
  }

  public String getSqlOperator() {
    if (operator == null) {
      return null;
    }

    return OPERATOR_MAP.get(operator);
  }

  public String getJavaOperator() {
    if (operator == null || operator == QueryOperator.LIKE || operator == QueryOperator.IN) {
      return null;
    }

    if (operator == QueryOperator.EQ) //TODO why special case?
    {
      return "==";
    }

    return OPERATOR_MAP.get(operator);
  }

  public String getSqlFilter(String encodedFilter) {
    if (operator == null || encodedFilter == null) {
      return null;
    }

    if (QueryOperator.LIKE.equals(operator)) {
      return "'%" + encodedFilter + "%'";
    } else if (QueryOperator.IN.equals(operator)) {
      List<String> filterItems = getFilterItems(encodedFilter);

      final StringBuffer buffer = new StringBuffer("(");

      for (String filterItem : filterItems) {
        buffer.append("'").append(filterItem).append("',");
      }

      return buffer.deleteCharAt(buffer.length() - 1).append(")").toString();
    }

    return "'" + encodedFilter + "'";
  }

  /**
   * Returns a string representation of the query operator and filter.
   */
  public String getFilterAsString() {
    return operator.getValue() + " " + filter;
  }

  // -------------------------------------------------------------------------
  // hashCode, equals and toString
  // -------------------------------------------------------------------------

  @Override
  public String toString() {
    return "[Operator: " + operator + ", filter: " + filter + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((filter == null) ? 0 : filter.hashCode());
    result = prime * result + ((operator == null) ? 0 : operator.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    QueryFilter other = (QueryFilter) object;

    if (filter == null) {
      if (other.filter != null) {
        return false;
      }
    } else if (!filter.equals(other.filter)) {
      return false;
    }

    if (operator == null) {
      return other.operator == null;
    } else {
      return operator.equals(other.operator);
    }

  }

  // -------------------------------------------------------------------------
  // Getters and setters
  // -------------------------------------------------------------------------

  public QueryOperator getOperator() {
    return operator;
  }

  public void setOperator(QueryOperator operator) {
    this.operator = operator;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }
}
