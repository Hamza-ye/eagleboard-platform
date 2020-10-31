package com.mass3d.category;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

/**
 * @version $Id$
 */
@Component( "com.mass3d.category.CategoryComboDeletionHandler" )
public class CategoryComboDeletionHandler
    extends
    DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    private final IdentifiableObjectManager idObjectManager;

    private final CategoryService categoryService;

    public CategoryComboDeletionHandler( CategoryService categoryService, IdentifiableObjectManager idObjectManager )
    {
        checkNotNull( categoryService );
        checkNotNull( idObjectManager );
        this.categoryService = categoryService;
        this.idObjectManager = idObjectManager;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return CategoryCombo.class.getSimpleName();
    }

    @Override
    public String allowDeleteCategory( Category category )
    {
        for ( CategoryCombo categoryCombo : categoryService.getAllCategoryCombos() )
        {
            if ( categoryCombo.getCategories().contains( category ) )
            {
                return categoryCombo.getName();
            }
        }

        return null;
    }

    @Override
    public void deleteCategoryOptionCombo( CategoryOptionCombo categoryOptionCombo )
    {
        for ( CategoryCombo categoryCombo : categoryService.getAllCategoryCombos() )
        {
            categoryCombo.getOptionCombos().remove( categoryOptionCombo );
            idObjectManager.updateNoAcl( categoryCombo );
        }
    }
}
