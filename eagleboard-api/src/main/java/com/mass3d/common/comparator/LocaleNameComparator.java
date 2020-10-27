package com.mass3d.common.comparator;

import java.util.Comparator;
import java.util.Locale;

public class LocaleNameComparator
    implements Comparator<Locale> {

  public static final LocaleNameComparator INSTANCE = new LocaleNameComparator();

  @Override
  public int compare(Locale o1, Locale o2) {
    return o1.getDisplayName().compareTo(o2.getDisplayName());
  }
}
