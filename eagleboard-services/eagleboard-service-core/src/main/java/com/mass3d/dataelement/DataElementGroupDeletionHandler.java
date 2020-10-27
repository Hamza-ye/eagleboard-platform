package com.mass3d.dataelement;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.dataelement.DataElementGroupDeletionHandler" )
public class DataElementGroupDeletionHandler
    extends DeletionHandler
{
    private final IdentifiableObjectManager idObjectManager;

    public DataElementGroupDeletionHandler( IdentifiableObjectManager idObjectManager )
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
        return DataElementGroup.class.getSimpleName();
    }

    @Override
    public void deleteDataElement( DataElement dataElement )
    {
        for ( DataElementGroup group : dataElement.getGroups() )
        {
            group.getMembers().remove( dataElement );
            idObjectManager.updateNoAcl( group );
        }
    }
}
