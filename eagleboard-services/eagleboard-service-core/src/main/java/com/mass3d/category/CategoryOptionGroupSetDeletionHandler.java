package com.mass3d.category;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.category.CategoryOptionGroupSetDeletionHandler" )
public class CategoryOptionGroupSetDeletionHandler
    extends DeletionHandler
{
    private final IdentifiableObjectManager idObjectManager;

    public CategoryOptionGroupSetDeletionHandler( IdentifiableObjectManager idObjectManager )
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
        return CategoryOptionGroupSet.class.getSimpleName();
    }

    @Override
    public void deleteCategoryOptionGroup( CategoryOptionGroup categoryOptionGroup )
    {
        for ( CategoryOptionGroupSet groupSet : categoryOptionGroup.getGroupSets() )
        {
            groupSet.getMembers().remove( categoryOptionGroup );
            idObjectManager.updateNoAcl( groupSet );
        }
    }
}
