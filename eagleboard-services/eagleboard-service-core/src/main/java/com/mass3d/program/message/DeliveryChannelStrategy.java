package com.mass3d.program.message;

import java.util.Set;
import com.mass3d.common.DeliveryChannel;
import com.mass3d.common.IllegalQueryException;
import com.mass3d.common.ValueType;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.organisationunit.OrganisationUnitService;
import com.mass3d.trackedentity.TrackedEntityInstance;
import com.mass3d.trackedentity.TrackedEntityInstanceService;
import com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValue;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DeliveryChannelStrategy
{
    @Autowired
    protected OrganisationUnitService organisationUnitService;

    @Autowired
    protected TrackedEntityInstanceService trackedEntityInstanceService;

    // -------------------------------------------------------------------------
    // Abstract methods
    // -------------------------------------------------------------------------

    protected abstract DeliveryChannel getDeliveryChannel();

    protected abstract ProgramMessage setAttributes( ProgramMessage message );

    protected abstract void validate( ProgramMessage message );

    protected abstract String getOrganisationUnitRecipient( OrganisationUnit orgUnit );
    
    // -------------------------------------------------------------------------
    // Public methods
    // -------------------------------------------------------------------------
    
    public String getTrackedEntityInstanceRecipient( TrackedEntityInstance tei, ValueType type )
    {
        Set<TrackedEntityAttributeValue> attributeValues = tei.getTrackedEntityAttributeValues();

        for ( TrackedEntityAttributeValue value : attributeValues )
        {
            if ( value != null && value.getAttribute().getValueType().equals( type ) &&
                value.getPlainValue() != null && !value.getPlainValue().trim().isEmpty() )
            {
                return value.getPlainValue();
            }
        }

        throw new IllegalQueryException( "Tracked entity does not have any attribute of value type: " + type.toString() );
    }

    // -------------------------------------------------------------------------
    // Public methods
    // -------------------------------------------------------------------------
    
    protected TrackedEntityInstance getTrackedEntityInstance( ProgramMessage message )
    {
        if ( message.getRecipients().getTrackedEntityInstance() == null )
        {
            return null;
        }

        String uid = message.getRecipients().getTrackedEntityInstance().getUid();

        TrackedEntityInstance tei = trackedEntityInstanceService.getTrackedEntityInstance( uid );

        message.getRecipients().setTrackedEntityInstance( tei );

        return tei;
    }

    protected OrganisationUnit getOrganisationUnit( ProgramMessage message )
    {
        if ( message.getRecipients().getOrganisationUnit() == null )
        {
            return null;
        }

        String uid = message.getRecipients().getOrganisationUnit().getUid();

        OrganisationUnit orgUnit = organisationUnitService.getOrganisationUnit( uid );

        message.getRecipients().setOrganisationUnit( orgUnit );

        return orgUnit;
    }
}
