package com.mass3d.category;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.category.CategoryOptionGroupDeletionHandler" )
public class CategoryOptionGroupDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final CategoryService categoryService;

    public CategoryOptionGroupDeletionHandler( CategoryService categoryService )
    {
        checkNotNull( categoryService );

        this.categoryService = categoryService;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return CategoryOptionGroup.class.getName();
    }

    @Override
    public void deleteCategoryOption( CategoryOption categoryOption )
    {
        for ( CategoryOptionGroup group : categoryOption.getGroups() )
        {
            group.getMembers().remove( categoryOption );
            categoryService.updateCategoryOptionGroup( group );
        }
    }
}
