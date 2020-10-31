package com.mass3d.category;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Set;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.category.CategoryDeletionHandler" )
public class CategoryDeletionHandler
    extends DeletionHandler
{
    private final IdentifiableObjectManager idObjectManager;

    public CategoryDeletionHandler( IdentifiableObjectManager idObjectManager )
    {
        checkNotNull( idObjectManager );
        this.idObjectManager = idObjectManager;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return Category.class.getSimpleName();
    }

    @Override
    public void deleteCategoryOption( CategoryOption categoryOption )
    {
        Set<Category> categories = categoryOption.getCategories();

        for ( Category category : categories )
        {
            category.getCategoryOptions().remove( categoryOption );
            idObjectManager.updateNoAcl( category );
        }
    }

    @Override
    public void deleteCategoryCombo( CategoryCombo categoryCombo )
    {
        List<Category> categories = categoryCombo.getCategories();

        for ( Category category : categories )
        {
            category.getCategoryCombos().remove( categoryCombo );
            idObjectManager.updateNoAcl( category );
        }
    }
}
