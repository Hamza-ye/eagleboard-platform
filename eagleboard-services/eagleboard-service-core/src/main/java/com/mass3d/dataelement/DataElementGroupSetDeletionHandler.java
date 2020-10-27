package com.mass3d.dataelement;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.dataelement.DataElementGroupSetDeletionHandler" )
public class DataElementGroupSetDeletionHandler
    extends DeletionHandler
{
    private final IdentifiableObjectManager idObjectManager;

    public DataElementGroupSetDeletionHandler( IdentifiableObjectManager idObjectManager )
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
        return DataElementGroupSet.class.getSimpleName();
    }

    @Override
    public void deleteDataElementGroup( DataElementGroup dataElementGroup )
    {
        for ( DataElementGroupSet groupSet : dataElementGroup.getGroupSets() )
        {
            groupSet.getMembers().remove( dataElementGroup );
            idObjectManager.updateNoAcl( groupSet );
        }
    }
}
