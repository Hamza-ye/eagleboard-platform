package com.mass3d.indicator;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.indicator.IndicatorGroupSetDeletionHandler" )
public class IndicatorGroupSetDeletionHandler
    extends
    DeletionHandler
{
    private final IdentifiableObjectManager idObjectManager;

    public IndicatorGroupSetDeletionHandler( IdentifiableObjectManager idObjectManager )
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
        return IndicatorGroupSet.class.getSimpleName();
    }

    @Override
    public void deleteIndicatorGroup( IndicatorGroup indicatorGroup )
    {
        IndicatorGroupSet groupSet = indicatorGroup.getGroupSet();

        if ( groupSet != null )
        {
            groupSet.getMembers().remove( indicatorGroup );
            idObjectManager.updateNoAcl( groupSet );
        }
    }
}
