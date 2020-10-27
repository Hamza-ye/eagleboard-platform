package com.mass3d.translation;

public enum TranslationProperty {
  NAME("name"),
  SHORT_NAME("shortName"),
  DESCRIPTION("description"),
  FORM_NAME("formName");

  private String name;

  TranslationProperty(String name) {
    this.name = name;
  }

  public static TranslationProperty fromValue(String value) {
    for (TranslationProperty type : TranslationProperty.values()) {
      if (type.name().equalsIgnoreCase(value)) {
        return type;
      }
    }

    return null;
  }

  public String getName() {
    return name;
  }
}
