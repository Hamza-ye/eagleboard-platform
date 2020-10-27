package com.mass3d.common;

public interface NameableObject
    extends IdentifiableObject {

  String getShortName();

  String getDisplayShortName();

  String getDescription();

  String getDisplayDescription();
}
