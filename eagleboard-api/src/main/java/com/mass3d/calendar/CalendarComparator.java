package com.mass3d.calendar;

import java.util.Comparator;

public class CalendarComparator implements Comparator<Calendar> {

  public static final CalendarComparator INSTANCE = new CalendarComparator();

  @Override
  public int compare(Calendar o1, Calendar o2) {
    return o1.name().compareTo(o2.name());
  }
}
