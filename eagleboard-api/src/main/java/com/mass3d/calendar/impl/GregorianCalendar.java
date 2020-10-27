package com.mass3d.calendar.impl;

import com.mass3d.calendar.Calendar;
import com.mass3d.calendar.ChronologyBasedCalendar;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.GregorianChronology;
import org.springframework.stereotype.Component;

@Component
public class GregorianCalendar extends ChronologyBasedCalendar {

  private static final Calendar SELF = new GregorianCalendar();

  protected GregorianCalendar() {
    super(GregorianChronology.getInstance(DateTimeZone.getDefault()));
  }

  public static Calendar getInstance() {
    return SELF;
  }

  @Override
  public String name() {
    return "gregorian";
  }

  @Override
  public boolean isIso8601() {
    return true;
  }
}
