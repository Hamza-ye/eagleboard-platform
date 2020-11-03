package com.mass3d.reservedvalue;

import com.mass3d.system.deletion.DeletionHandler;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.reservedvalue.SequentialNumberCounterDeletionHandler" )
public class SequentialNumberCounterDeletionHandler
    extends DeletionHandler
{

    private final SequentialNumberCounterStore sequentialNumberCounterStore;

    public SequentialNumberCounterDeletionHandler(
        SequentialNumberCounterStore sequentialNumberCounterStore )
    {
        this.sequentialNumberCounterStore = sequentialNumberCounterStore;
    }

    @Override
    protected String getClassName()
    {
        return SequentialNumberCounter.class.getSimpleName();
    }

    @Override
    public void deleteTrackedEntityAttribute( TrackedEntityAttribute attribute )
    {
        sequentialNumberCounterStore.deleteCounter( attribute.getUid() );
    }
}
