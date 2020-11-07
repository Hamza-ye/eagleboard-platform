package com.mass3d.dxf2.metadata.objectbundle.hooks;

import com.google.common.collect.ImmutableMap;
import java.util.function.Consumer;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dataelement.DataElementService;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.sms.command.SMSCommand;
import com.mass3d.sms.parse.ParserType;
import com.mass3d.trackedentity.TrackedEntityAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SmsCommandObjectBundleHook extends AbstractObjectBundleHook
{
    private ImmutableMap<ParserType, Consumer<SMSCommand>> VALUE_POPULATOR = new ImmutableMap.Builder<ParserType, Consumer<SMSCommand>>()
        .put( ParserType.TRACKED_ENTITY_REGISTRATION_PARSER, sc -> { sc.setProgramStage( null ); sc.setUserGroup( null ); sc.setDataset( null ); } )
        .put( ParserType.PROGRAM_STAGE_DATAENTRY_PARSER, sc -> { sc.setDataset( null ); sc.setUserGroup( null ); } )
        .put( ParserType.KEY_VALUE_PARSER, sc -> { sc.setProgram( null ); sc.setProgramStage( null ); } )
        .put( ParserType.ALERT_PARSER, sc -> { sc.setProgram( null ); sc.setProgramStage( null ); } )
        .build();

    @Autowired
    private DataElementService dataElementService;

    @Autowired
    private TrackedEntityAttributeService trackedEntityAttributeService;


    @Override
    public <T extends IdentifiableObject> void preCreate( T object, ObjectBundle bundle )
    {
        if ( !SMSCommand.class.isInstance( object ) )
        {
            return;
        }

        SMSCommand command = (SMSCommand) object;

        process( command );

        getReferences( command );
    }

    @Override
    public <T extends IdentifiableObject> void preUpdate( T object, T persistedObject, ObjectBundle bundle )
    {
        if ( !SMSCommand.class.isInstance( object ) )
        {
            return;
        }

        SMSCommand command = (SMSCommand) object;

        getReferences( command );
    }

    private void process(SMSCommand command )
    {
        VALUE_POPULATOR.getOrDefault( command.getParserType(), sc -> {} ).accept( command );
    }

    private void getReferences( SMSCommand command )
    {
        command.getCodes().stream()
            .filter( c -> c.hasDataElement() )
            .forEach( c -> c.setDataElement( dataElementService.getDataElement( c.getDataElement().getUid() ) ) );

        command.getCodes().stream()
            .filter( c -> c.hasTrackedEntityAttribute() )
            .forEach( c -> c.setTrackedEntityAttribute( trackedEntityAttributeService.getTrackedEntityAttribute( c.getTrackedEntityAttribute().getUid() ) ) );
    }
}
