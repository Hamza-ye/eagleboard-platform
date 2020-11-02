package com.mass3d.organisationunit;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.organisationunit.OrganisationUnitGroupDeletionHandler" )
public class OrganisationUnitGroupDeletionHandler
    extends DeletionHandler
{
    private final IdentifiableObjectManager idObjectManager;

    public OrganisationUnitGroupDeletionHandler( IdentifiableObjectManager idObjectManager )
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
        return OrganisationUnitGroup.class.getSimpleName();
    }

    @Override
    public void deleteOrganisationUnit( OrganisationUnit unit )
    {
        for ( OrganisationUnitGroup group : unit.getGroups() )
        {
            group.getMembers().remove( unit );
            idObjectManager.updateNoAcl( group );
        }
    }
}
