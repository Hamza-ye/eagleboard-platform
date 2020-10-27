package com.mass3d.calendar.impl;

import com.mass3d.calendar.Calendar;
import com.mass3d.calendar.ChronologyBasedCalendar;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.BuddhistChronology;
import org.springframework.stereotype.Component;

@Component
public class ThaiCalendar extends ChronologyBasedCalendar {

  private static final Calendar SELF = new ThaiCalendar();

  protected ThaiCalendar() {
    super(BuddhistChronology.getInstance(DateTimeZone.getDefault()));
  }

  public static Calendar getInstance() {
    return SELF;
  }

  @Override
  public String name() {
    return "thai";
  }
}
