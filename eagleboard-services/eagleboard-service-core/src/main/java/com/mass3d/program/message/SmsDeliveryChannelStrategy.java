package com.mass3d.program.message;

import lombok.extern.slf4j.Slf4j;
import com.mass3d.common.DeliveryChannel;
import com.mass3d.common.IllegalQueryException;
import com.mass3d.common.ValueType;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.trackedentity.TrackedEntityInstance;
import org.springframework.stereotype.Component;

@Slf4j
@Component( "com.mass3d.program.message.SmsDeliveryChannelStrategy" )
public class SmsDeliveryChannelStrategy
    extends DeliveryChannelStrategy
{
    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public DeliveryChannel getDeliveryChannel()
    {
        return DeliveryChannel.SMS;
    }

    @Override
    public ProgramMessage setAttributes( ProgramMessage message )
    {
        validate( message );

        OrganisationUnit orgUnit = getOrganisationUnit( message );

        TrackedEntityInstance tei = getTrackedEntityInstance( message );

        if ( orgUnit != null )
        {
            message.getRecipients().getPhoneNumbers().add( getOrganisationUnitRecipient( orgUnit ) );
        }

        if ( tei != null )
        {
            message.getRecipients().getPhoneNumbers()
                .add( getTrackedEntityInstanceRecipient( tei, ValueType.PHONE_NUMBER ) );
        }

        return message;
    }

    @Override
    public void validate( ProgramMessage message )
    {
        String violation = null;

        ProgramMessageRecipients recipient = message.getRecipients();

        if ( message.getDeliveryChannels().contains( DeliveryChannel.SMS ) )
        {
            if ( !recipient.hasOrganisationUnit() && !recipient.hasTrackedEntityInstance()
                && recipient.getPhoneNumbers().isEmpty() )
            {
                violation = "No destination found for SMS";
            }
        }

        if ( violation != null )
        {
            log.info( "Message validation failed: " + violation );

            throw new IllegalQueryException( violation );
        }
    }

    @Override
    public String getOrganisationUnitRecipient( OrganisationUnit orgUnit )
    {
        if ( orgUnit.getPhoneNumber() == null )
        {
            log.error( "Organisation unit does not have phone number" );

            throw new IllegalQueryException( "Organisation unit does not have phone number" );
        }

        return orgUnit.getPhoneNumber();
    }
}
