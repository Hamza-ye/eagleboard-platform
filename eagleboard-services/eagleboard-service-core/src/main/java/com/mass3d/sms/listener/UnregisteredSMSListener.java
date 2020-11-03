package com.mass3d.sms.listener;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
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
import com.mass3d.system.util.SmsUtils;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserGroup;
import com.mass3d.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component( "com.mass3d.sms.listener.UnregisteredSMSListener" )
@Transactional
public class UnregisteredSMSListener
    extends
    CommandSMSListener
{

    private static final String USER_NAME = "anonymous";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final SMSCommandService smsCommandService;

    private final UserService userService;

    private final MessageService messageService;

    public UnregisteredSMSListener( ProgramInstanceService programInstanceService,
        CategoryService dataElementCategoryService, ProgramStageInstanceService programStageInstanceService,
        UserService userService, CurrentUserService currentUserService, IncomingSmsService incomingSmsService,
        @Qualifier( "smsMessageSender" ) MessageSender smsSender, SMSCommandService smsCommandService,
        UserService userService1, MessageService messageService )
    {
        super( programInstanceService, dataElementCategoryService, programStageInstanceService, userService,
            currentUserService, incomingSmsService, smsSender );

        checkNotNull( smsCommandService );
        checkNotNull( userService );
        checkNotNull( messageService );

        this.smsCommandService = smsCommandService;
        this.userService = userService1;
        this.messageService = messageService;
    }

    // -------------------------------------------------------------------------
    // IncomingSmsListener implementation
    // -------------------------------------------------------------------------

    @Override
    protected SMSCommand getSMSCommand( IncomingSms sms )
    {
        return smsCommandService.getSMSCommand( SmsUtils.getCommandString( sms ), ParserType.UNREGISTERED_PARSER );
    }

    @Override
    protected void postProcess( IncomingSms sms, SMSCommand smsCommand, Map<String, String> parsedMessage )
    {
        UserGroup userGroup = smsCommand.getUserGroup();

        String userName = sms.getOriginator();

        if ( userGroup != null )
        {
            UserCredentials anonymousUser = userService.getUserCredentialsByUsername( userName );

            if ( anonymousUser == null )
            {
                User user = new User();

                UserCredentials usercredential = new UserCredentials();
                usercredential.setUsername( userName );
                usercredential.setPassword( USER_NAME );
                usercredential.setUserInfo( user );

                user.setSurname( userName );
                user.setFirstName( "" );
                user.setUserCredentials( usercredential );
                user.setAutoFields();

                userService.addUserCredentials( usercredential );
                userService.addUser( user );

                anonymousUser = userService.getUserCredentialsByUsername( userName );
            }

            messageService.sendMessage( new MessageConversationParams.Builder( userGroup.getMembers(),
                anonymousUser.getUserInfo(), smsCommand.getName(), sms.getText(), MessageType.SYSTEM ).build() );

            sendFeedback( smsCommand.getReceivedMessage(), sms.getOriginator(), INFO );

            update( sms, SmsMessageStatus.PROCESSED, true );
        }
    }
}
