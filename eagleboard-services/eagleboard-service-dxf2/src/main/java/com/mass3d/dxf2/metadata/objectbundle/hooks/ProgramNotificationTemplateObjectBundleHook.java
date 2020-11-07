package com.mass3d.dxf2.metadata.objectbundle.hooks;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.Set;
import com.mass3d.common.DeliveryChannel;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.ValueType;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.program.notification.ProgramNotificationRecipient;
import com.mass3d.program.notification.ProgramNotificationTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProgramNotificationTemplateObjectBundleHook
    extends AbstractObjectBundleHook
{
    private ImmutableMap<ProgramNotificationRecipient, Function<ProgramNotificationTemplate, ValueType>>
        RECIPIENT_TO_VALUETYPE_RESOLVER = new ImmutableMap.Builder<ProgramNotificationRecipient, Function<ProgramNotificationTemplate, ValueType>>()
        .put( ProgramNotificationRecipient.PROGRAM_ATTRIBUTE, template -> template.getRecipientProgramAttribute().getValueType() )
        .put( ProgramNotificationRecipient.DATA_ELEMENT, template -> template.getRecipientDataElement().getValueType() )
        .build();

    private static  final ImmutableMap<ValueType, Set<DeliveryChannel>>
        CHANNEL_MAPPER = new ImmutableMap.Builder<ValueType, Set<DeliveryChannel>>()
        .put( ValueType.PHONE_NUMBER, Sets.newHashSet( DeliveryChannel.SMS ) )
        .put( ValueType.EMAIL, Sets.newHashSet( DeliveryChannel.EMAIL ) )
        .build();

    @Override
    public <T extends IdentifiableObject> void preCreate( T object, ObjectBundle bundle )
    {
        if ( !ProgramNotificationTemplate.class.isInstance( object ) ) return;
        ProgramNotificationTemplate template = (ProgramNotificationTemplate) object;

        preProcess( template );
    }

    @Override
    public <T extends IdentifiableObject> void preUpdate( T object, T persistedObject, ObjectBundle bundle )
    {
        if ( !ProgramNotificationTemplate.class.isInstance( object ) ) return;
        ProgramNotificationTemplate template = (ProgramNotificationTemplate) object;

        preProcess( template );
    }

    @Override
    public <T extends IdentifiableObject> void postCreate( T persistedObject, ObjectBundle bundle )
    {
        if ( !ProgramNotificationTemplate.class.isInstance( persistedObject ) ) return;
        ProgramNotificationTemplate template = (ProgramNotificationTemplate) persistedObject;

        postProcess( template );
    }

    @Override
    public <T extends IdentifiableObject> void postUpdate( T persistedObject, ObjectBundle bundle )
    {
        if ( !ProgramNotificationTemplate.class.isInstance( persistedObject ) ) return;
        ProgramNotificationTemplate template = (ProgramNotificationTemplate) persistedObject;

        postProcess( template );
    }

    /**
     * Removes any non-valid combinations of properties on the template object.
     */
    private void preProcess( ProgramNotificationTemplate template )
    {
        if ( template.getNotificationTrigger().isImmediate() )
        {
            template.setRelativeScheduledDays( null );
        }

        if ( ProgramNotificationRecipient.USER_GROUP != template.getNotificationRecipient() )
        {
            template.setRecipientUserGroup( null );
        }

        if ( ProgramNotificationRecipient.PROGRAM_ATTRIBUTE != template.getNotificationRecipient() )
        {
            template.setRecipientProgramAttribute( null );
        }

        if ( ProgramNotificationRecipient.DATA_ELEMENT != template.getNotificationRecipient() )
        {
            template.setRecipientDataElement( null );
        }

        if ( ! ( template.getNotificationRecipient().isExternalRecipient() ) )
        {
            template.setDeliveryChannels( Sets.newHashSet() );
        }
    }

    private void postProcess( ProgramNotificationTemplate template )
    {
        if ( ProgramNotificationRecipient.PROGRAM_ATTRIBUTE == template.getNotificationRecipient() )
        {
            resolveTemplateRecipients( template, ProgramNotificationRecipient.PROGRAM_ATTRIBUTE );
        }

        if ( ProgramNotificationRecipient.DATA_ELEMENT == template.getNotificationRecipient() )
        {
            resolveTemplateRecipients( template, ProgramNotificationRecipient.DATA_ELEMENT );
        }
    }

    private void resolveTemplateRecipients( ProgramNotificationTemplate pnt, ProgramNotificationRecipient pnr )
    {
        Function<ProgramNotificationTemplate,ValueType> resolver = RECIPIENT_TO_VALUETYPE_RESOLVER.get( pnr );

        ValueType valueType = null;

        if ( resolver != null && ( pnt.getRecipientProgramAttribute() != null || pnt.getRecipientDataElement() != null ) )
        {
            valueType = resolver.apply( pnt );
        }

        pnt.setDeliveryChannels( CHANNEL_MAPPER.getOrDefault( valueType, Sets.newHashSet() ) );
    }
}
