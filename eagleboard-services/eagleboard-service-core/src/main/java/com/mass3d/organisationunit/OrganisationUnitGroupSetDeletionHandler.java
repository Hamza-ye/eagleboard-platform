package com.mass3d.organisationunit;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.organisationunit.OrganisationUnitGroupSetDeletionHandler" )
public class OrganisationUnitGroupSetDeletionHandler
    extends
    DeletionHandler
{
    private final IdentifiableObjectManager idObjectManager;

    public OrganisationUnitGroupSetDeletionHandler( IdentifiableObjectManager idObjectManager )
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
        return OrganisationUnitGroupSet.class.getSimpleName();
    }

    @Override
    public void deleteOrganisationUnitGroup( OrganisationUnitGroup group )
    {
        for ( OrganisationUnitGroupSet groupSet : group.getGroupSets() )
        {
            groupSet.getOrganisationUnitGroups().remove( group );
            idObjectManager.updateNoAcl( groupSet );
        }
    }
}
