package com.mass3d.organisationunit;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.stream.Collectors;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dataset.DataSet;
import com.mass3d.program.Program;
import com.mass3d.system.deletion.DeletionHandler;
import com.mass3d.user.User;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.organisationunit.OrganisationUnitDeletionHandler" )
public class OrganisationUnitDeletionHandler
    extends
    DeletionHandler
{
    private final IdentifiableObjectManager idObjectManager;

    public OrganisationUnitDeletionHandler( IdentifiableObjectManager idObjectManager )
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
        return OrganisationUnit.class.getSimpleName();
    }

    @Override
    public void deleteDataSet( DataSet dataSet )
    {
        for ( OrganisationUnit unit : dataSet.getSources() )
        {
            unit.getDataSets().remove( dataSet );
            idObjectManager.updateNoAcl( unit );
        }
    }

    @Override
    public void deleteUser( User user )
    {
        for ( OrganisationUnit unit : user.getOrganisationUnits() )
        {
            unit.removeUser( user );
            idObjectManager.updateNoAcl( unit );
        }
    }

    @Override
    public void deleteProgram( Program program )
    {
        for ( OrganisationUnit unit : program.getOrganisationUnits() )
        {
            unit.removeProgram( program );
            idObjectManager.updateNoAcl( unit );
        }
    }

    @Override
    public void deleteOrganisationUnitGroup( OrganisationUnitGroup group )
    {
        for ( OrganisationUnit unit : group.getMembers() )
        {
            unit.getGroups().remove( group );
            idObjectManager.updateNoAcl( unit );
        }
    }

    @Override
    public void deleteOrganisationUnit( OrganisationUnit unit )
    {
        if ( unit.getParent() != null )
        {
            unit.getParent().getChildren().remove( unit );
            idObjectManager.updateNoAcl( unit.getParent() );
        }
    }

    @Override
    public String allowDeleteOrganisationUnit( OrganisationUnit unit )
    {
        return unit.getChildren().isEmpty() ? null
            : unit.getChildren().stream().map( BaseIdentifiableObject::getName ).collect( Collectors
                .joining( "," ) );
    }
}
