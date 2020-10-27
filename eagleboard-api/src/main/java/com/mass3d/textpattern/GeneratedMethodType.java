package com.mass3d.textpattern;

import java.util.regex.Pattern;

public class GeneratedMethodType
    extends BaseMethodType {

  GeneratedMethodType(Pattern pattern) {
    super(pattern);
  }

  @Override
  public String getValueRegex(String format) {
    format = format.replaceAll("#", "[0-9]");
    format = format.replaceAll("X", "[A-Z]");
    format = format.replaceAll("x", "[a-z]");
    format = format.replaceAll("\\*", "[a-zA-Z0-9]");
    return format;
  }
}
