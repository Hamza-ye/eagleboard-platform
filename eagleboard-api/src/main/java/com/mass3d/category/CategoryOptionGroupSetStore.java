package com.mass3d.category;

import java.util.List;
import com.mass3d.common.DataDimensionType;
import com.mass3d.common.GenericDimensionalObjectStore;

public interface CategoryOptionGroupSetStore
    extends GenericDimensionalObjectStore<CategoryOptionGroupSet>
{
    List<CategoryOptionGroupSet> getCategoryOptionGroupSetsNoAcl(
        DataDimensionType dataDimensionType, boolean dataDimension);
}
