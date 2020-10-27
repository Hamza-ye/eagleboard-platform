package com.mass3d.textpattern;

import java.util.regex.Pattern;

public class TextMethodType
    extends BaseMethodType {

  TextMethodType(Pattern pattern) {
    super(pattern);
  }

  @Override
  public boolean validateText(String format, String text) {
    format = format.replaceAll("\\\\d", "[0-9]");
    format = format.replaceAll("\\\\x", "[a-z]");
    format = format.replaceAll("\\\\X", "[A-Z]");
    format = format.replaceAll("\\\\w", "[0-9a-zA-Z]");

    return Pattern.compile(format).matcher(text).matches();
  }

  @Override
  public String getValueRegex(String format) {
    return format;
  }
}
