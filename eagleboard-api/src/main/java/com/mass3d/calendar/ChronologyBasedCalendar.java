package com.mass3d.calendar;

import java.util.Date;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;

public abstract class ChronologyBasedCalendar extends AbstractCalendar {

  protected final Chronology chronology;

  protected ChronologyBasedCalendar(Chronology chronology) {
    this.chronology = chronology;
  }

  @Override
  public DateTimeUnit toIso(DateTimeUnit dateTimeUnit) {
    if (dateTimeUnit.isIso8601()) {
      return dateTimeUnit;
    }

    DateTime dateTime = dateTimeUnit.toJodaDateTime(chronology);
    dateTime = dateTime.withChronology(
        ISOChronology.getInstance(DateTimeZone.forTimeZone(dateTimeUnit.getTimeZone())));

    return new DateTimeUnit(DateTimeUnit.fromJodaDateTime(dateTime), true);
  }

  @Override
  public DateTimeUnit fromIso(Date date) {
    return fromIso(DateTimeUnit.fromJdkDate(date));
  }

  @Override
  public DateTimeUnit fromIso(DateTimeUnit dateTimeUnit) {
    if (!dateTimeUnit.isIso8601()) {
      return dateTimeUnit;
    }

    DateTime dateTime = dateTimeUnit.toJodaDateTime(
        ISOChronology.getInstance(DateTimeZone.forTimeZone(dateTimeUnit.getTimeZone())));
    dateTime = dateTime.withChronology(chronology);

    return DateTimeUnit.fromJodaDateTime(dateTime);
  }

  @Override
  public DateInterval toInterval(DateTimeUnit dateTimeUnit, DateIntervalType type, int offset,
      int length) {
    switch (type) {
      case ISO8601_YEAR:
        return toYearIsoInterval(dateTimeUnit, offset, length);
      case ISO8601_MONTH:
        return toMonthIsoInterval(dateTimeUnit, offset, length);
      case ISO8601_WEEK:
        return toWeekIsoInterval(dateTimeUnit, offset, length);
      case ISO8601_DAY:
        return toDayIsoInterval(dateTimeUnit, offset, length);
    }

    return null;
  }

  private DateInterval toYearIsoInterval(DateTimeUnit dateTimeUnit, int offset, int length) {
    DateTime from = dateTimeUnit.toJodaDateTime(chronology);

    if (offset > 0) {
      from = from.plusYears(offset);
    } else if (offset < 0) {
      from = from.minusYears(-offset);
    }

    DateTime to = new DateTime(from).plusYears(length).minusDays(1);

    DateTimeUnit fromDateTimeUnit = DateTimeUnit.fromJodaDateTime(from);
    DateTimeUnit toDateTimeUnit = DateTimeUnit.fromJodaDateTime(to);

    fromDateTimeUnit.setDayOfWeek(isoWeekday(fromDateTimeUnit));
    toDateTimeUnit.setDayOfWeek(isoWeekday(toDateTimeUnit));

    return new DateInterval(toIso(fromDateTimeUnit), toIso(toDateTimeUnit),
        DateIntervalType.ISO8601_YEAR);
  }

  private DateInterval toMonthIsoInterval(DateTimeUnit dateTimeUnit, int offset, int length) {
    DateTime from = dateTimeUnit.toJodaDateTime(chronology);

    if (offset > 0) {
      from = from.plusMonths(offset);
    } else if (offset < 0) {
      from = from.minusMonths(-offset);
    }

    DateTime to = new DateTime(from).plusMonths(length).minusDays(1);

    DateTimeUnit fromDateTimeUnit = DateTimeUnit.fromJodaDateTime(from);
    DateTimeUnit toDateTimeUnit = DateTimeUnit.fromJodaDateTime(to);

    fromDateTimeUnit.setDayOfWeek(isoWeekday(fromDateTimeUnit));
    toDateTimeUnit.setDayOfWeek(isoWeekday(toDateTimeUnit));

    return new DateInterval(toIso(fromDateTimeUnit), toIso(toDateTimeUnit),
        DateIntervalType.ISO8601_MONTH);
  }

  private DateInterval toWeekIsoInterval(DateTimeUnit dateTimeUnit, int offset, int length) {
    DateTime from = dateTimeUnit.toJodaDateTime(chronology);

    if (offset > 0) {
      from = from.plusWeeks(offset);
    } else if (offset < 0) {
      from = from.minusWeeks(-offset);
    }

    DateTime to = new DateTime(from).plusWeeks(length).minusDays(1);

    DateTimeUnit fromDateTimeUnit = DateTimeUnit.fromJodaDateTime(from);
    DateTimeUnit toDateTimeUnit = DateTimeUnit.fromJodaDateTime(to);

    fromDateTimeUnit.setDayOfWeek(isoWeekday(fromDateTimeUnit));
    toDateTimeUnit.setDayOfWeek(isoWeekday(toDateTimeUnit));

    return new DateInterval(toIso(fromDateTimeUnit), toIso(toDateTimeUnit),
        DateIntervalType.ISO8601_WEEK);
  }

  private DateInterval toDayIsoInterval(DateTimeUnit dateTimeUnit, int offset, int length) {
    DateTime from = dateTimeUnit.toJodaDateTime(chronology);

    if (offset > 0) {
      from = from.plusDays(offset);
    } else if (offset < 0) {
      from = from.minusDays(-offset);
    }

    DateTime to = new DateTime(from).plusDays(length);

    DateTimeUnit fromDateTimeUnit = DateTimeUnit.fromJodaDateTime(from);
    DateTimeUnit toDateTimeUnit = DateTimeUnit.fromJodaDateTime(to);

    fromDateTimeUnit.setDayOfWeek(isoWeekday(fromDateTimeUnit));
    toDateTimeUnit.setDayOfWeek(isoWeekday(toDateTimeUnit));

    return new DateInterval(toIso(fromDateTimeUnit), toIso(toDateTimeUnit),
        DateIntervalType.ISO8601_DAY);
  }

  @Override
  public int daysInWeek() {
    LocalDate localDate = new LocalDate(1, 1, 1, chronology);
    return localDate.toDateTimeAtStartOfDay().dayOfWeek().getMaximumValue();
  }

  @Override
  public int daysInYear(int year) {
    LocalDate localDate = new LocalDate(year, 1, 1, chronology);
    return (int) localDate.toDateTimeAtStartOfDay().year().toInterval().toDuration()
        .getStandardDays();
  }

  @Override
  public int daysInMonth(int year, int month) {
    LocalDate localDate = new LocalDate(year, month, 1, chronology);
    return localDate.toDateTimeAtStartOfDay().dayOfMonth().getMaximumValue();
  }

  @Override
  public int weeksInYear(int year) {
    LocalDate localDate = new LocalDate(year, 1, 1, chronology);
    return localDate.toDateTimeAtStartOfDay().weekOfWeekyear().getMaximumValue();
  }

  @Override
  public int isoWeek(DateTimeUnit dateTimeUnit) {
    DateTime dateTime = dateTimeUnit.toJodaDateTime(chronology);
    return dateTime.getWeekOfWeekyear();
  }

  @Override
  public int week(DateTimeUnit dateTimeUnit) {
    return isoWeek(dateTimeUnit);
  }

  @Override
  public int isoWeekday(DateTimeUnit dateTimeUnit) {
    DateTime dateTime = dateTimeUnit.toJodaDateTime(chronology);
    dateTime = dateTime.withChronology(ISOChronology.getInstance(DateTimeZone.getDefault()));
    return dateTime.getDayOfWeek();
  }

  @Override
  public int weekday(DateTimeUnit dateTimeUnit) {
    return dateTimeUnit.toJodaDateTime(chronology).getDayOfWeek();
  }

  @Override
  public DateTimeUnit plusDays(DateTimeUnit dateTimeUnit, int days) {
    DateTime dateTime = dateTimeUnit.toJodaDateTime(chronology);
    return DateTimeUnit.fromJodaDateTime(dateTime.plusDays(days), dateTimeUnit.isIso8601());
  }

  @Override
  public DateTimeUnit minusDays(DateTimeUnit dateTimeUnit, int days) {
    DateTime dateTime = dateTimeUnit.toJodaDateTime(chronology);
    return DateTimeUnit.fromJodaDateTime(dateTime.minusDays(days), dateTimeUnit.isIso8601());
  }

  @Override
  public DateTimeUnit plusWeeks(DateTimeUnit dateTimeUnit, int weeks) {
    DateTime dateTime = dateTimeUnit.toJodaDateTime(chronology);
    return DateTimeUnit.fromJodaDateTime(dateTime.plusWeeks(weeks), dateTimeUnit.isIso8601());
  }

  @Override
  public DateTimeUnit minusWeeks(DateTimeUnit dateTimeUnit, int weeks) {
    DateTime dateTime = dateTimeUnit.toJodaDateTime(chronology);
    return DateTimeUnit.fromJodaDateTime(dateTime.minusWeeks(weeks), dateTimeUnit.isIso8601());
  }

  @Override
  public DateTimeUnit plusMonths(DateTimeUnit dateTimeUnit, int months) {
    DateTime dateTime = dateTimeUnit.toJodaDateTime(chronology);
    return DateTimeUnit.fromJodaDateTime(dateTime.plusMonths(months), dateTimeUnit.isIso8601());
  }

  @Override
  public DateTimeUnit minusMonths(DateTimeUnit dateTimeUnit, int months) {
    DateTime dateTime = dateTimeUnit.toJodaDateTime(chronology);
    return DateTimeUnit.fromJodaDateTime(dateTime.minusMonths(months), dateTimeUnit.isIso8601());
  }

  @Override
  public DateTimeUnit plusYears(DateTimeUnit dateTimeUnit, int years) {
    DateTime dateTime = dateTimeUnit.toJodaDateTime(chronology);
    return DateTimeUnit.fromJodaDateTime(dateTime.plusYears(years), dateTimeUnit.isIso8601());
  }

  @Override
  public DateTimeUnit minusYears(DateTimeUnit dateTimeUnit, int years) {
    DateTime dateTime = dateTimeUnit.toJodaDateTime(chronology);
    return DateTimeUnit.fromJodaDateTime(dateTime.minusYears(years), dateTimeUnit.isIso8601());
  }

  @Override
  public DateTimeUnit isoStartOfYear(int year) {
    DateTime dateTime = new DateTime(year, 1, 1, 11, 0, chronology).withChronology(ISOChronology
        .getInstance());
    return DateTimeUnit.fromJodaDateTime(dateTime);
  }
}
