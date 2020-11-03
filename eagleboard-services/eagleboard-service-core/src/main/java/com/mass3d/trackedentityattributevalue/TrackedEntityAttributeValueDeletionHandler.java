package com.mass3d.trackedentityattributevalue;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import com.mass3d.system.deletion.DeletionHandler;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.trackedentity.TrackedEntityInstance;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValueDeletionHandler" )
public class TrackedEntityAttributeValueDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final TrackedEntityAttributeValueService attributeValueService;

    public TrackedEntityAttributeValueDeletionHandler( TrackedEntityAttributeValueService attributeValueService )
    {
        checkNotNull( attributeValueService );
        this.attributeValueService = attributeValueService;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return TrackedEntityAttributeValue.class.getSimpleName();
    }

    @Override
    public void deleteTrackedEntityInstance( TrackedEntityInstance instance )
    {
        Collection<TrackedEntityAttributeValue> attributeValues = attributeValueService
            .getTrackedEntityAttributeValues( instance );

        for ( TrackedEntityAttributeValue attributeValue : attributeValues )
        {
            attributeValueService.deleteTrackedEntityAttributeValue( attributeValue );
        }
    }

    @Override
    public String allowDeleteTrackedEntityAttribute( TrackedEntityAttribute attribute )
    {
        return attributeValueService.getCountOfAssignedTrackedEntityAttributeValues( attribute ) == 0 ? null : "Some values are still assigned to this attribute";
    }
}
