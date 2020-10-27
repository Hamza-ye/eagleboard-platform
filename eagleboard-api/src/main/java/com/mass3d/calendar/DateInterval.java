package com.mass3d.calendar;

import javax.validation.constraints.NotNull;

/**
 * Class representing a date interval.
 *
 * @see DateTimeUnit
 * @see Calendar
 */
public class DateInterval {

  /**
   * Start of interval. Required.
   */
  @NotNull
  private final DateTimeUnit from;

  /**
   * End of interval. Required.
   */
  @NotNull
  private final DateTimeUnit to;

  /**
   * Interval type this interval represents.
   */
  private DateIntervalType type;

  public DateInterval(DateTimeUnit from, DateTimeUnit to) {
    this.from = from;
    this.to = to;
  }

  public DateInterval(DateTimeUnit from, DateTimeUnit to, DateIntervalType type) {
    this.from = from;
    this.to = to;
    this.type = type;
  }

  public DateTimeUnit getFrom() {
    return from;
  }

  public DateTimeUnit getTo() {
    return to;
  }

  public DateIntervalType getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DateInterval that = (DateInterval) o;

    if (from != null ? !from.equals(that.from) : that.from != null) {
      return false;
    }
    if (to != null ? !to.equals(that.to) : that.to != null) {
      return false;
    }
    return type == that.type;
  }

  @Override
  public int hashCode() {
    int result = from != null ? from.hashCode() : 0;
    result = 31 * result + (to != null ? to.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "DateInterval{" +
        "from=" + from +
        ", to=" + to +
        ", type=" + type +
        '}';
  }
}
