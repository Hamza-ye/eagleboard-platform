package com.mass3d.calendar.impl;

import com.mass3d.calendar.Calendar;
import com.mass3d.calendar.ChronologyBasedCalendar;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.CopticChronology;
import org.springframework.stereotype.Component;

@Component
public class CopticCalendar extends ChronologyBasedCalendar {

  private static final Calendar SELF = new CopticCalendar();

  protected CopticCalendar() {
    super(CopticChronology.getInstance(DateTimeZone.getDefault()));
  }

  public static Calendar getInstance() {
    return SELF;
  }

  @Override
  public String name() {
    return "coptic";
  }
}
