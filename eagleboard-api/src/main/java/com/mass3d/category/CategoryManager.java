package com.mass3d.category;

public interface CategoryManager
{
    /**
     * Generates the complete set of category option combos for the given
     * category combo. Removes obsolete category option combos.
     *
     * @param categoryCombo the CategoryCombo.
     */
    void addAndPruneOptionCombos(CategoryCombo categoryCombo);

    /**
     * Generates the complete set of category option combos for all category
     * combos.
     */
    void addAndPruneAllOptionCombos();
}
