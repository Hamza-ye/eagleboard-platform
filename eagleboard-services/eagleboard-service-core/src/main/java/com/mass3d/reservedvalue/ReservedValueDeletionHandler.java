package com.mass3d.reservedvalue;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.system.deletion.DeletionHandler;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.reservedvalue.ReservedValueDeletionHandler" )
public class ReservedValueDeletionHandler extends DeletionHandler
{

    private final ReservedValueService reservedValueService;

    public ReservedValueDeletionHandler( ReservedValueService reservedValueService )
    {
        checkNotNull( reservedValueService );
        this.reservedValueService = reservedValueService;
    }

    @Override
    protected String getClassName()
    {
        return ReservedValue.class.getSimpleName();
    }

    @Override
    public void deleteTrackedEntityAttribute( TrackedEntityAttribute attribute )
    {
        reservedValueService.deleteReservedValueByUid( attribute.getUid() );
    }
}
