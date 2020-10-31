package com.mass3d.category;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

/**
 * @version $Id$
 */
@Component( "com.mass3d.category.CategoryOptionDeletionHandler" )
public class CategoryOptionDeletionHandler
    extends DeletionHandler
{
    private final IdentifiableObjectManager idObjectManager;

    public CategoryOptionDeletionHandler( IdentifiableObjectManager idObjectManager )
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
        return CategoryOption.class.getSimpleName();
    }
    
    @Override
    public void deleteCategory( Category category )
    {
        for ( CategoryOption categoryOption : category.getCategoryOptions() )
        {
            categoryOption.getCategories().remove( category );
            idObjectManager.updateNoAcl( categoryOption );
        }
    }
}
