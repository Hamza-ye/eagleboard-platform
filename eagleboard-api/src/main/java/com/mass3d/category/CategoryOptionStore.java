package com.mass3d.category;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

public interface CategoryOptionStore
    extends IdentifiableObjectStore<CategoryOption>
{
    List<CategoryOption> getCategoryOptions(Category category);
}
