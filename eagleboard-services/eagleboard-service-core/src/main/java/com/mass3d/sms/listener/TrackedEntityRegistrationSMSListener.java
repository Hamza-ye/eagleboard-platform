package com.mass3d.sms.listener;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.category.CategoryService;
import com.mass3d.message.MessageSender;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramInstanceService;
import com.mass3d.program.ProgramStageInstanceService;
import com.mass3d.sms.command.SMSCommand;
import com.mass3d.sms.command.SMSCommandService;
import com.mass3d.sms.command.code.SMSCode;
import com.mass3d.sms.incoming.IncomingSms;
import com.mass3d.sms.incoming.IncomingSmsService;
import com.mass3d.sms.incoming.SmsMessageStatus;
import com.mass3d.sms.parse.ParserType;
import com.mass3d.sms.parse.SMSParserException;
import com.mass3d.system.util.SmsUtils;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.trackedentity.TrackedEntityInstance;
import com.mass3d.trackedentity.TrackedEntityInstanceService;
import com.mass3d.trackedentity.TrackedEntityTypeService;
import com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValue;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component( "com.mass3d.sms.listener.TrackedEntityRegistrationSMSListener" )
@Transactional
public class TrackedEntityRegistrationSMSListener
    extends
    CommandSMSListener
{
    private static final String SUCCESS_MESSAGE = "Tracked Entity Registered Successfully with uid. ";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final SMSCommandService smsCommandService;

    private final TrackedEntityTypeService trackedEntityTypeService;

    private final TrackedEntityInstanceService trackedEntityInstanceService;

    private final ProgramInstanceService programInstanceService;

    public TrackedEntityRegistrationSMSListener( ProgramInstanceService programInstanceService,
        CategoryService dataElementCategoryService, ProgramStageInstanceService programStageInstanceService,
        UserService userService, CurrentUserService currentUserService, IncomingSmsService incomingSmsService,
        @Qualifier( "smsMessageSender" ) MessageSender smsSender, SMSCommandService smsCommandService,
        TrackedEntityTypeService trackedEntityTypeService, TrackedEntityInstanceService trackedEntityInstanceService,
        ProgramInstanceService programInstanceService1 )
    {
        super( programInstanceService, dataElementCategoryService, programStageInstanceService, userService,
            currentUserService, incomingSmsService, smsSender );

        checkNotNull( smsCommandService );
        checkNotNull( trackedEntityTypeService );
        checkNotNull( trackedEntityInstanceService );
        checkNotNull( programInstanceService );

        this.smsCommandService = smsCommandService;
        this.trackedEntityTypeService = trackedEntityTypeService;
        this.trackedEntityInstanceService = trackedEntityInstanceService;
        this.programInstanceService = programInstanceService1;
    }

    // -------------------------------------------------------------------------
    // IncomingSmsListener implementation
    // -------------------------------------------------------------------------

    @Override
    protected void postProcess( IncomingSms sms, SMSCommand smsCommand, Map<String, String> parsedMessage )
    {
        String message = sms.getText();

        Date date = SmsUtils.lookForDate( message );
        String senderPhoneNumber = StringUtils.replace( sms.getOriginator(), "+", "" );
        Collection<OrganisationUnit> orgUnits = getOrganisationUnits( sms );

        Program program = smsCommand.getProgram();

        OrganisationUnit orgUnit = SmsUtils.selectOrganisationUnit( orgUnits, parsedMessage, smsCommand );

        if ( !program.hasOrganisationUnit( orgUnit ) )
        {
            sendFeedback( SMSCommand.NO_OU_FOR_PROGRAM, senderPhoneNumber, WARNING );

            throw new SMSParserException( SMSCommand.NO_OU_FOR_PROGRAM );
        }

        TrackedEntityInstance trackedEntityInstance = new TrackedEntityInstance();
        trackedEntityInstance.setOrganisationUnit( orgUnit );
        trackedEntityInstance.setTrackedEntityType( trackedEntityTypeService
            .getTrackedEntityByName( smsCommand.getProgram().getTrackedEntityType().getName() ) );
        Set<TrackedEntityAttributeValue> patientAttributeValues = new HashSet<>();

        smsCommand.getCodes().stream().filter( code -> parsedMessage.containsKey( code.getCode() ) ).forEach( code -> {
            TrackedEntityAttributeValue trackedEntityAttributeValue = this
                .createTrackedEntityAttributeValue( parsedMessage, code, trackedEntityInstance );
            patientAttributeValues.add( trackedEntityAttributeValue );
        } );

        long trackedEntityInstanceId = 0;
        if ( patientAttributeValues.size() > 0 )
        {
            trackedEntityInstanceId = trackedEntityInstanceService.createTrackedEntityInstance( trackedEntityInstance,
                patientAttributeValues );
        }
        else
        {
            sendFeedback( "No TrackedEntityAttribute found", senderPhoneNumber, WARNING );
        }

        TrackedEntityInstance tei = trackedEntityInstanceService.getTrackedEntityInstance( trackedEntityInstanceId );

        programInstanceService.enrollTrackedEntityInstance( tei, smsCommand.getProgram(), new Date(), date, orgUnit );

        sendFeedback( StringUtils.defaultIfBlank( smsCommand.getSuccessMessage(), SUCCESS_MESSAGE + tei.getUid() ),
            senderPhoneNumber, INFO );

        update( sms, SmsMessageStatus.PROCESSED, true );
    }

    @Override
    protected SMSCommand getSMSCommand( IncomingSms sms )
    {
        return smsCommandService.getSMSCommand( SmsUtils.getCommandString( sms ),
            ParserType.TRACKED_ENTITY_REGISTRATION_PARSER );
    }

    private TrackedEntityAttributeValue createTrackedEntityAttributeValue( Map<String, String> parsedMessage,
        SMSCode code, TrackedEntityInstance trackedEntityInstance )
    {
        String value = parsedMessage.get( code.getCode() );
        TrackedEntityAttribute trackedEntityAttribute = code.getTrackedEntityAttribute();

        TrackedEntityAttributeValue trackedEntityAttributeValue = new TrackedEntityAttributeValue();
        trackedEntityAttributeValue.setAttribute( trackedEntityAttribute );
        trackedEntityAttributeValue.setEntityInstance( trackedEntityInstance );
        trackedEntityAttributeValue.setValue( value );
        return trackedEntityAttributeValue;
    }
}
