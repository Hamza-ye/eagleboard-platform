package com.mass3d.sms.listener;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.category.CategoryService;
import com.mass3d.message.MessageConversationParams;
import com.mass3d.message.MessageSender;
import com.mass3d.message.MessageService;
import com.mass3d.message.MessageType;
import com.mass3d.program.ProgramInstanceService;
import com.mass3d.program.ProgramStageInstanceService;
import com.mass3d.sms.command.SMSCommand;
import com.mass3d.sms.command.SMSCommandService;
import com.mass3d.sms.incoming.IncomingSms;
import com.mass3d.sms.incoming.IncomingSmsService;
import com.mass3d.sms.incoming.SmsMessageStatus;
import com.mass3d.sms.parse.ParserType;
import com.mass3d.sms.parse.SMSParserException;
import com.mass3d.system.util.SmsUtils;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.user.UserGroup;
import com.mass3d.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component( "com.mass3d.sms.listener.DhisMessageAlertListener" )
@Transactional
public class DhisMessageAlertListener
    extends
    CommandSMSListener
{
    private final SMSCommandService smsCommandService;

    private final MessageService messageService;

    public DhisMessageAlertListener( ProgramInstanceService programInstanceService,
        CategoryService dataElementCategoryService, ProgramStageInstanceService programStageInstanceService,
        UserService userService, CurrentUserService currentUserService, IncomingSmsService incomingSmsService,
        @Qualifier( "smsMessageSender" ) MessageSender smsSender, SMSCommandService smsCommandService,
        MessageService messageService )
    {
        super( programInstanceService, dataElementCategoryService, programStageInstanceService, userService,
            currentUserService, incomingSmsService, smsSender );

        checkNotNull( smsCommandService );
        checkNotNull( messageService );

        this.smsCommandService = smsCommandService;
        this.messageService = messageService;
    }

    @Override
    protected SMSCommand getSMSCommand( IncomingSms sms )
    {
        return smsCommandService.getSMSCommand( SmsUtils.getCommandString( sms ), ParserType.ALERT_PARSER );
    }

    @Override
    protected void postProcess( IncomingSms sms, SMSCommand smsCommand, Map<String, String> parsedMessage )
    {
        String message = sms.getText();

        UserGroup userGroup = smsCommand.getUserGroup();

        if ( userGroup != null )
        {
            Collection<User> users = Collections.singleton( sms.getUser() );

            if ( users != null && users.size() > 1 )
            {
                String messageMoreThanOneUser = smsCommand.getMoreThanOneOrgUnitMessage();

                if ( messageMoreThanOneUser.trim().isEmpty() )
                {
                    messageMoreThanOneUser = SMSCommand.MORE_THAN_ONE_ORGUNIT_MESSAGE;
                }

                for ( Iterator<User> i = users.iterator(); i.hasNext(); )
                {
                    User user = i.next();
                    messageMoreThanOneUser += " " + user.getName();
                    if ( i.hasNext() )
                    {
                        messageMoreThanOneUser += ",";
                    }
                }

                throw new SMSParserException( messageMoreThanOneUser );
            }
            else if ( users != null && users.size() == 1 )
            {
                User sender = users.iterator().next();

                Set<User> receivers = new HashSet<>( userGroup.getMembers() );
                messageService.sendMessage( new MessageConversationParams.Builder( receivers, sender,
                    smsCommand.getName(), message, MessageType.SYSTEM ).build() );

                Set<User> feedbackList = new HashSet<>();
                feedbackList.add( sender );

                String confirmMessage = smsCommand.getReceivedMessage();

                if ( confirmMessage == null )
                {
                    confirmMessage = SMSCommand.ALERT_FEEDBACK;
                }

                if ( smsSender.isConfigured() )
                {
                    smsSender.sendMessage( smsCommand.getName(), confirmMessage, null, null, feedbackList, false );
                }
                else
                {
                    log.info( "No sms configuration found." );
                }

                update( sms, SmsMessageStatus.PROCESSED, true );
            }
            else if ( users == null || users.size() == 0 )
            {
                throw new SMSParserException(
                    "No user associated with this phone number. Please contact your supervisor." );
            }
        }
    }
}
