package com.mass3d.category;

import java.util.List;
import java.util.Set;
import com.mass3d.common.IdentifiableObjectStore;

public interface CategoryOptionComboStore
    extends IdentifiableObjectStore<CategoryOptionCombo>
{
    CategoryOptionCombo getCategoryOptionCombo(CategoryCombo categoryCombo,
        Set<CategoryOption> categoryOptions);

    void updateNames();

    void deleteNoRollBack(CategoryOptionCombo categoryOptionCombo);

    /**
     * Fetch all {@link CategoryOptionCombo} from a given {@link CategoryOptionGroup} uid.
     *
     * A {@link CategoryOptionGroup} is a collection of {@link CategoryOption}. Therefore, this method finds all
     * {@link CategoryOptionCombo} for all the members of the given {@link CategoryOptionGroup}
     *
     * @param groupId a {@link CategoryOptionGroup} uid
     * @return a List of {@link CategoryOptionCombo} or empty List
     */
    List<CategoryOptionCombo> getCategoryOptionCombosByGroupUid(String groupId);
}
