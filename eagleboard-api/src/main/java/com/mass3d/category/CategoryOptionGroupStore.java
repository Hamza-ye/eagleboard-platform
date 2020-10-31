package com.mass3d.category;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

public interface CategoryOptionGroupStore
    extends IdentifiableObjectStore<CategoryOptionGroup>
{
    List<CategoryOptionGroup> getCategoryOptionGroups(CategoryOptionGroupSet groupSet);
}
