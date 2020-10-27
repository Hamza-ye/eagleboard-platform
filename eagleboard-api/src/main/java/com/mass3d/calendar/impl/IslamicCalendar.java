package com.mass3d.calendar.impl;

import com.mass3d.calendar.Calendar;
import com.mass3d.calendar.ChronologyBasedCalendar;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.IslamicChronology;
import org.springframework.stereotype.Component;

@Component
public class IslamicCalendar extends ChronologyBasedCalendar {

  private static final Calendar SELF = new IslamicCalendar();

  protected IslamicCalendar() {
    super(IslamicChronology.getInstance(DateTimeZone.getDefault()));
  }

  public static Calendar getInstance() {
    return SELF;
  }

  @Override
  public String name() {
    return "islamic";
  }
}
