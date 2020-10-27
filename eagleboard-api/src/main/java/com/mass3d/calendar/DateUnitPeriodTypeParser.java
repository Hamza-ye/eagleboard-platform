package com.mass3d.calendar;

import com.google.common.collect.Maps;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import com.mass3d.calendar.impl.Iso8601Calendar;
import com.mass3d.period.BiWeeklyPeriodType;
import com.mass3d.period.PeriodType;
import com.mass3d.period.WeeklyAbstractPeriodType;

public class DateUnitPeriodTypeParser implements PeriodTypeParser {

  private static CalendarService calendarService;
  private final Map<String, Pattern> compileCache = Maps.newHashMap();

  public static CalendarService getCalendarService() {
    return calendarService;
  }

  public static void setCalendarService(CalendarService calendarService) {
    DateUnitPeriodTypeParser.calendarService = calendarService;
  }

  public static com.mass3d.calendar.Calendar getCalendar() {
    if (calendarService != null) {
      return calendarService.getSystemCalendar();
    }

    return Iso8601Calendar.getInstance();
  }

  @Override
  public DateInterval parse(String period) {
    return parse(getCalendar(), period);
  }

  @Override
  public DateInterval parse(Calendar calendar, String period) {
    DateUnitType dateUnitType = DateUnitType.find(period);

    if (dateUnitType == null) {
      return null;
    }

    if (compileCache.get(dateUnitType.getName()) == null) {
      try {
        Pattern pattern = Pattern.compile(dateUnitType.getPattern());
        compileCache.put(dateUnitType.getName(), pattern);
      } catch (PatternSyntaxException ex) {
        return null;
      }
    }

    Pattern pattern = compileCache.get(dateUnitType.getName());
    Matcher matcher = pattern.matcher(period);
    boolean match = matcher.find();

    if (!match) {
      return null;
    }

    if (DateUnitType.DAILY == dateUnitType) {
      int year = Integer.parseInt(matcher.group(1));
      int month = Integer.parseInt(matcher.group(2));
      int day = Integer.parseInt(matcher.group(3));

      DateTimeUnit dateTimeUnit = new DateTimeUnit(year, month, day, calendar.isIso8601());
      dateTimeUnit.setDayOfWeek(calendar.weekday(dateTimeUnit));

      return new DateInterval(dateTimeUnit, dateTimeUnit);
    } else if (DateUnitType.WEEKLY == dateUnitType || DateUnitType.WEEKLY_WEDNESDAY == dateUnitType
        ||
        DateUnitType.WEEKLY_THURSDAY == dateUnitType
        || DateUnitType.WEEKLY_SATURDAY == dateUnitType
        || DateUnitType.WEEKLY_SUNDAY == dateUnitType) {
      DateTimeUnit start;
      DateTimeUnit end;
      int year = Integer.parseInt(matcher.group(1));
      int week = Integer.parseInt(matcher.group(2));

      WeeklyAbstractPeriodType periodType = (WeeklyAbstractPeriodType) PeriodType
          .getByNameIgnoreCase(dateUnitType.getName());

      if (periodType == null || week < 1 || week > calendar.weeksInYear(year)) {
        return null;
      }

      start = getDateTimeFromWeek(year, week, calendar,
          PeriodType.MAP_WEEK_TYPE.get(periodType.getName()),
          new DateTimeUnit(year, 1, 1));

      end = calendar.plusWeeks(start, 1);
      end = calendar.minusDays(end, 1);

      return new DateInterval(start, end);
    } else if (DateUnitType.BI_WEEKLY == dateUnitType) {
      int year = Integer.parseInt(matcher.group(1));
      int week = Integer.parseInt(matcher.group(2)) * 2 - 1;

      BiWeeklyPeriodType periodType = (BiWeeklyPeriodType) PeriodType
          .getByNameIgnoreCase(dateUnitType.getName());

      if (periodType == null || week < 1 || week > calendar.weeksInYear(year)) {
        return null;
      }

      DateTimeUnit start = getDateTimeFromWeek(year, week, calendar, DayOfWeek.MONDAY,
          new DateTimeUnit(year, 1, 1));
      DateTimeUnit end = calendar.plusWeeks(start, 2);
      end = calendar.minusDays(end, 1);

      return new DateInterval(start, end);
    } else if (DateUnitType.MONTHLY == dateUnitType) {
      int year = Integer.parseInt(matcher.group(1));
      int month = Integer.parseInt(matcher.group(2));

      DateTimeUnit start = new DateTimeUnit(year, month, 1, calendar.isIso8601());
      DateTimeUnit end = new DateTimeUnit(year, month,
          calendar.daysInMonth(start.getYear(), start.getMonth()),
          calendar.isIso8601());

      start.setDayOfWeek(calendar.weekday(start));
      end.setDayOfWeek(calendar.weekday(end));

      return new DateInterval(start, end);
    } else if (DateUnitType.BI_MONTHLY == dateUnitType) {
      int year = Integer.parseInt(matcher.group(1));
      int month = Integer.parseInt(matcher.group(2));

      if (month < 1 || month > 6) {
        return null;
      }

      DateTimeUnit start = new DateTimeUnit(year, (month * 2) - 1, 1, calendar.isIso8601());
      DateTimeUnit end = new DateTimeUnit(start);
      end = calendar.plusMonths(end, 2);
      end = calendar.minusDays(end, 1);

      start.setDayOfWeek(calendar.weekday(start));
      end.setDayOfWeek(calendar.weekday(end));

      return new DateInterval(start, end);
    } else if (DateUnitType.QUARTERLY == dateUnitType) {
      int year = Integer.parseInt(matcher.group(1));
      int quarter = Integer.parseInt(matcher.group(2));

      // valid quarters are from 1 - 4
      if (quarter < 1 || quarter > 4) {
        return null;
      }

      DateTimeUnit start = new DateTimeUnit(year, ((quarter - 1) * 3) + 1, 1, calendar.isIso8601());
      DateTimeUnit end = new DateTimeUnit(start);
      end = calendar.plusMonths(end, 3);
      end = calendar.minusDays(end, 1);

      start.setDayOfWeek(calendar.weekday(start));
      end.setDayOfWeek(calendar.weekday(end));

      return new DateInterval(start, end);
    } else if (DateUnitType.SIX_MONTHLY == dateUnitType) {
      int year = Integer.parseInt(matcher.group(1));
      int semester = Integer.parseInt(matcher.group(2));

      // valid six-monthly are from 1 - 2
      if (semester < 1 || semester > 2) {
        return null;
      }

      DateTimeUnit start = new DateTimeUnit(year, semester == 1 ? 1 : 7, 1, calendar.isIso8601());
      DateTimeUnit end = new DateTimeUnit(start);
      end = calendar.plusMonths(end, 6);
      end = calendar.minusDays(end, 1);

      start.setDayOfWeek(calendar.weekday(start));
      end.setDayOfWeek(calendar.weekday(end));

      return new DateInterval(start, end);
    } else if (DateUnitType.SIX_MONTHLY_APRIL == dateUnitType) {
      int year = Integer.parseInt(matcher.group(1));
      int semester = Integer.parseInt(matcher.group(2));

      // valid six-monthly are from 1 - 2
      if (semester < 1 || semester > 2) {
        return null;
      }

      DateTimeUnit start = new DateTimeUnit(year, semester == 1 ? 4 : 10, 1, calendar.isIso8601());
      DateTimeUnit end = new DateTimeUnit(start);
      end = calendar.plusMonths(end, 6);
      end = calendar.minusDays(end, 1);

      start.setDayOfWeek(calendar.weekday(start));
      end.setDayOfWeek(calendar.weekday(end));

      return new DateInterval(start, end);
    } else if (DateUnitType.YEARLY == dateUnitType) {
      int year = Integer.parseInt(matcher.group(1));

      DateTimeUnit start = new DateTimeUnit(year, 1, 1, calendar.isIso8601());
      DateTimeUnit end = new DateTimeUnit(year, calendar.monthsInYear(),
          calendar.daysInMonth(start.getYear(), calendar.monthsInYear()), calendar.isIso8601());

      start.setDayOfWeek(calendar.weekday(start));
      end.setDayOfWeek(calendar.weekday(end));

      return new DateInterval(start, end);
    } else if (DateUnitType.FINANCIAL_APRIL == dateUnitType) {
      int year = Integer.parseInt(matcher.group(1));

      DateTimeUnit start = new DateTimeUnit(year, 4, 1, calendar.isIso8601());
      DateTimeUnit end = new DateTimeUnit(start);
      end = calendar.plusYears(end, 1);
      end = calendar.minusDays(end, 1);

      start.setDayOfWeek(calendar.weekday(start));
      end.setDayOfWeek(calendar.weekday(end));

      return new DateInterval(start, end);
    } else if (DateUnitType.FINANCIAL_JULY == dateUnitType) {
      int year = Integer.parseInt(matcher.group(1));

      DateTimeUnit start = new DateTimeUnit(year, 7, 1, calendar.isIso8601());
      DateTimeUnit end = new DateTimeUnit(start);
      end = calendar.plusYears(end, 1);
      end = calendar.minusDays(end, 1);

      start.setDayOfWeek(calendar.weekday(start));
      end.setDayOfWeek(calendar.weekday(end));

      return new DateInterval(start, end);
    } else if (DateUnitType.FINANCIAL_OCTOBER == dateUnitType) {
      int year = Integer.parseInt(matcher.group(1));

      DateTimeUnit start = new DateTimeUnit(year, 10, 1, calendar.isIso8601());
      DateTimeUnit end = new DateTimeUnit(start);
      end = calendar.plusYears(end, 1);
      end = calendar.minusDays(end, 1);

      start.setDayOfWeek(calendar.weekday(start));
      end.setDayOfWeek(calendar.weekday(end));

      return new DateInterval(start, end);
    }

    return null;
  }

  /**
   * returns a date based on a week number
   *
   * @param year The year of the date
   * @param week The week of the date
   * @param calendar The calendar used to calculate the daate
   * @param firstDayOfWeek The first day of the week
   * @param adjustedDate The first day of the year adjusted to the first day of the week it belongs
   * to
   * @return The Date of the week
   */
  private DateTimeUnit getDateTimeFromWeek(int year, int week, Calendar calendar,
      DayOfWeek firstDayOfWeek, DateTimeUnit adjustedDate) {
    if (calendar.isIso8601()) {
      WeekFields weekFields = WeekFields.of(firstDayOfWeek, 4);

      LocalDate date = LocalDate.now()
          .with(weekFields.weekBasedYear(), year)
          .with(weekFields.weekOfWeekBasedYear(), week)
          .with(weekFields.dayOfWeek(), 1);

      return new DateTimeUnit(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
          calendar.isIso8601());
    } else {
      DateTimeUnit date = new DateTimeUnit(year, adjustedDate.getMonth(), adjustedDate.getDay(),
          calendar.isIso8601());

      // since we rewind to start of week, we might end up in the previous years weeks, so we check and forward if needed
      if (calendar.isoWeek(date) == calendar.weeksInYear(year)) {
        date = calendar.plusWeeks(date, 1);
      }

      date = calendar.plusWeeks(date, week - 1);
      return date;
    }

  }
}
