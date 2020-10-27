package com.mass3d.textpattern;

import java.util.regex.Pattern;

public class StringMethodType
    extends BaseMethodType {

  StringMethodType(Pattern pattern) {
    super(pattern);
  }

  @Override
  public String getValueRegex(String format) {
    if (format.isEmpty()) {
      format = ".*";
    }

    format = format.replaceAll("\\^", "");
    format = format.replaceAll("\\$", "");
    return format;
  }

  @Override
  public boolean validateText(String format, String text) {
    if (format.isEmpty()) {
      return true;
    }

    return super.validateText(format, text);
  }

  @Override
  public String getFormattedText(String format, String value) {
    if (format.isEmpty()) {
      return value;
    }

    return TextPatternMethodUtils.formatText(format, value);
  }
}
