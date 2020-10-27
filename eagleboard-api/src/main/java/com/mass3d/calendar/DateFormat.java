package com.mass3d.calendar;

public class DateFormat {

  private final String name;

  private final String jdkDateFormat;

  private final String jodaDateFormat;

  private final String jsDateFormat;

  public DateFormat(String name, String jdkDateFormat, String jodaDateFormat, String jsDateFormat) {
    this.name = name;
    this.jdkDateFormat = jdkDateFormat;
    this.jodaDateFormat = jodaDateFormat;
    this.jsDateFormat = jsDateFormat;
  }

  public String name() {
    return name;
  }

  public String getJdk() {
    return jdkDateFormat;
  }

  public String getJoda() {
    return jodaDateFormat;
  }

  public String getJs() {
    return jsDateFormat;
  }
}
