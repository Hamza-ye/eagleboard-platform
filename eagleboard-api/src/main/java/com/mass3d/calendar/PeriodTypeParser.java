package com.mass3d.calendar;

public interface PeriodTypeParser {

  DateInterval parse(String period);

  DateInterval parse(Calendar calendar, String period);
}
