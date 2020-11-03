package com.mass3d.sms.listener;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.mass3d.category.CategoryService;
import com.mass3d.message.MessageSender;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramInstanceService;
import com.mass3d.program.ProgramStageInstanceService;
import com.mass3d.program.ProgramStatus;
import com.mass3d.sms.command.SMSCommand;
import com.mass3d.sms.command.SMSCommandService;
import com.mass3d.sms.incoming.IncomingSms;
import com.mass3d.sms.incoming.IncomingSmsService;
import com.mass3d.sms.parse.ParserType;
import com.mass3d.system.util.SmsUtils;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component( "com.mass3d.sms.listener.SingleEventListener" )
@Transactional
public class SingleEventListener
    extends
    CommandSMSListener
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final SMSCommandService smsCommandService;

    private final ProgramInstanceService programInstanceService;

    public SingleEventListener( ProgramInstanceService programInstanceService,
        CategoryService dataElementCategoryService, ProgramStageInstanceService programStageInstanceService,
        UserService userService, CurrentUserService currentUserService, IncomingSmsService incomingSmsService,
        @Qualifier( "smsMessageSender" ) MessageSender smsSender, SMSCommandService smsCommandService,
        ProgramInstanceService programInstanceService1 )
    {
        super( programInstanceService, dataElementCategoryService, programStageInstanceService, userService,
            currentUserService, incomingSmsService, smsSender );

        checkNotNull( smsCommandService );
        checkNotNull( programInstanceService1 );

        this.smsCommandService = smsCommandService;
        this.programInstanceService = programInstanceService1;
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    protected SMSCommand getSMSCommand( IncomingSms sms )
    {
        return smsCommandService.getSMSCommand( SmsUtils.getCommandString( sms ),
            ParserType.EVENT_REGISTRATION_PARSER );
    }

    @Override
    protected void postProcess( IncomingSms sms, SMSCommand smsCommand, Map<String, String> parsedMessage )
    {
        Set<OrganisationUnit> ous = getOrganisationUnits( sms );

        registerEvent( parsedMessage, smsCommand, sms, ous );
    }

    // -------------------------------------------------------------------------
    // Supportive Methods
    // -------------------------------------------------------------------------

    private void registerEvent( Map<String, String> commandValuePairs, SMSCommand smsCommand, IncomingSms sms,
        Set<OrganisationUnit> ous )
    {
        List<ProgramInstance> programInstances = new ArrayList<>(
            programInstanceService.getProgramInstances( smsCommand.getProgram(), ProgramStatus.ACTIVE ) );

        register( programInstances, commandValuePairs, smsCommand, sms, ous );
    }
}
