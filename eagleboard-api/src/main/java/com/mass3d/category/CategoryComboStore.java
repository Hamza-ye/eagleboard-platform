package com.mass3d.category;

import java.util.List;
import com.mass3d.common.DataDimensionType;
import com.mass3d.common.IdentifiableObjectStore;

public interface CategoryComboStore
    extends IdentifiableObjectStore<CategoryCombo>
{
    List<CategoryCombo> getCategoryCombosByDimensionType(DataDimensionType dataDimensionType);
}
