package com.mass3d.common;


import com.mass3d.option.OptionSet;

public interface ValueTypedDimensionalItemObject
    extends DimensionalItemObject {

  boolean hasOptionSet();

  OptionSet getOptionSet();

  ValueType getValueType();
}
