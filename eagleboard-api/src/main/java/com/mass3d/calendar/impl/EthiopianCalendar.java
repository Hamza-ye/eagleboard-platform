package com.mass3d.calendar.impl;

import java.util.Date;
import com.mass3d.calendar.Calendar;
import com.mass3d.calendar.ChronologyBasedCalendar;
import com.mass3d.calendar.DateTimeUnit;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.EthiopicChronology;
import org.springframework.stereotype.Component;

@Component
public class EthiopianCalendar extends ChronologyBasedCalendar {

  private static final Calendar SELF = new EthiopianCalendar();

  protected EthiopianCalendar() {
    super(EthiopicChronology.getInstance(DateTimeZone.getDefault()));
  }

  public static Calendar getInstance() {
    return SELF;
  }

  @Override
  public String name() {
    return "ethiopian";
  }

  @Override
  public DateTimeUnit toIso(DateTimeUnit dateTimeUnit) {
    if (dateTimeUnit.getMonth() > 12) {
      throw new RuntimeException(
          "Illegal month, must be between 1 and 12, was given " + dateTimeUnit.getMonth());
    }

    return super.toIso(dateTimeUnit);
  }

  @Override
  public DateTimeUnit fromIso(Date date) {
    DateTimeUnit dateTimeUnit = super.fromIso(date);

    if (dateTimeUnit.getMonth() > 12) {
      throw new RuntimeException(
          "Illegal month, must be between 1 and 12, was given " + dateTimeUnit.getMonth());
    }

    return dateTimeUnit;
  }

  @Override
  public DateTimeUnit fromIso(DateTimeUnit dateTimeUnit) {
    return super.fromIso(dateTimeUnit);
  }

  @Override
  public DateTimeUnit plusDays(DateTimeUnit dateTimeUnit, int days) {
    if (days < 0) {
      return minusDays(dateTimeUnit, Math.abs(days));
    }

    int curYear = dateTimeUnit.getYear();
    int curMonth = dateTimeUnit.getMonth();
    int curDay = dateTimeUnit.getDay();
    int dayOfWeek = dateTimeUnit.getDayOfWeek();

    while (days != 0) {
      curDay++;

      if (curDay > 30) {
        curMonth++;
        curDay = 1;
      }

      if (curMonth > 12) {
        curYear++;
        curMonth = 1;
      }

      dayOfWeek++;

      if (dayOfWeek > 7) {
        dayOfWeek = 1;
      }

      days--;
    }

    return new DateTimeUnit(curYear, curMonth, curDay, dayOfWeek);
  }

  @Override
  public DateTimeUnit minusDays(DateTimeUnit dateTimeUnit, int days) {
    int curYear = dateTimeUnit.getYear();
    int curMonth = dateTimeUnit.getMonth();
    int curDay = dateTimeUnit.getDay();
    int dayOfWeek = dateTimeUnit.getDayOfWeek();

    while (days != 0) {
      curDay--;

      if (curDay == 0) {
        curMonth--;

        if (curMonth == 0) {
          curYear--;
          curMonth = 12;
        }

        curDay = 30;
      }

      dayOfWeek--;

      if (dayOfWeek == 0) {
        dayOfWeek = 7;
      }

      days--;
    }

    return new DateTimeUnit(curYear, curMonth, curDay, dayOfWeek);
  }

  @Override
  public DateTimeUnit plusWeeks(DateTimeUnit dateTimeUnit, int weeks) {
    return plusDays(dateTimeUnit, weeks * 7);
  }

  @Override
  public DateTimeUnit minusWeeks(DateTimeUnit dateTimeUnit, int weeks) {
    return minusDays(dateTimeUnit, weeks * 7);
  }

  @Override
  public DateTimeUnit plusMonths(DateTimeUnit dateTimeUnit, int months) {
    return plusDays(dateTimeUnit, months * 30);
  }

  @Override
  public DateTimeUnit minusMonths(DateTimeUnit dateTimeUnit, int months) {
    return minusDays(dateTimeUnit, months * 30);
  }

  @Override
  public DateTimeUnit plusYears(DateTimeUnit dateTimeUnit, int years) {
    return plusDays(dateTimeUnit, years * (12 * 30));
  }

  @Override
  public DateTimeUnit minusYears(DateTimeUnit dateTimeUnit, int years) {
    return minusDays(dateTimeUnit, years * (12 * 30));
  }

  @Override
  public int daysInYear(int year) {
    return 12 * 30;
  }

  @Override
  public int daysInMonth(int year, int month) {
    if (month > 12) {
      throw new RuntimeException("Illegal month, must be between 1 and 12, was given " + month);
    }

    return 30;
  }

  @Override
  public int daysInWeek() {
    return 7;
  }

  @Override
  public DateTimeUnit isoStartOfYear(int year) {
    return fromIso(super.isoStartOfYear(year));
  }
}
