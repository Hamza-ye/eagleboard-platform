package com.mass3d.category;

import java.util.List;
import com.mass3d.common.DataDimensionType;
import com.mass3d.common.GenericDimensionalObjectStore;

public interface CategoryStore
    extends GenericDimensionalObjectStore<Category>
{
    List<Category> getCategoriesByDimensionType(DataDimensionType dataDimensionType);

    List<Category> getCategories(DataDimensionType dataDimensionType, boolean dataDimension);

    List<Category> getCategoriesNoAcl(DataDimensionType dataDimensionType, boolean dataDimension);
}
