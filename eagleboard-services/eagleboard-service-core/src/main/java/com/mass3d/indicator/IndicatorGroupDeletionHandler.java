package com.mass3d.indicator;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.indicator.IndicatorGroupDeletionHandler" )
public class IndicatorGroupDeletionHandler
    extends DeletionHandler
{
    private final IdentifiableObjectManager idObjectManager;

    public IndicatorGroupDeletionHandler( IdentifiableObjectManager idObjectManager )
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
        return IndicatorGroup.class.getSimpleName();
    }
    
    @Override
    public void deleteIndicator( Indicator indicator )
    {
        for ( IndicatorGroup group : indicator.getGroups() )
        {
            group.getMembers().remove( indicator );
            idObjectManager.updateNoAcl( group );
        }
    }
}
