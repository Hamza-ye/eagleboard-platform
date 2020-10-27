package com.mass3d.calendar.impl;

import com.mass3d.calendar.Calendar;
import com.mass3d.calendar.ChronologyBasedCalendar;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.springframework.stereotype.Component;

@Component
public class Iso8601Calendar extends ChronologyBasedCalendar {

  private static final Calendar SELF = new Iso8601Calendar();

  protected Iso8601Calendar() {
    super(ISOChronology.getInstance(DateTimeZone.getDefault()));
  }

  public static Calendar getInstance() {
    return SELF;
  }

  @Override
  public String name() {
    return "iso8601";
  }

  @Override
  public boolean isIso8601() {
    return true;
  }
}
