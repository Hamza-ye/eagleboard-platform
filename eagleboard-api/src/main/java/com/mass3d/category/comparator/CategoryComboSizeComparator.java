package com.mass3d.category.comparator;

/**
 * @version $Id$
 */
import java.util.Comparator;
import com.mass3d.category.CategoryCombo;

public class CategoryComboSizeComparator
    implements Comparator<CategoryCombo>
{
    @Override
    public int compare( CategoryCombo o1, CategoryCombo o2 )
    {
        return o1.getOptionCombos().size() - o2.getOptionCombos().size();
    }
}
