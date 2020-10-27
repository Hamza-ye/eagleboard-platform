package com.mass3d.option;

import java.util.List;
import com.mass3d.common.DataDimensionType;
import com.mass3d.common.GenericDimensionalObjectStore;

public interface OptionGroupStore
    extends GenericDimensionalObjectStore<OptionGroup> {

  List<OptionGroup> getOptionGroups(OptionGroupSet groupSet);

  List<OptionGroup> getOptionGroupsNoAcl(DataDimensionType dataDimensionType,
      boolean dataDimension);
}

