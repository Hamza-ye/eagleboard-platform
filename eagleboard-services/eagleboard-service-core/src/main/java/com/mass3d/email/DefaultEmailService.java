package com.mass3d.email;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Sets;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.message.MessageSender;
import com.mass3d.outboundmessage.OutboundMessageResponse;
import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import com.mass3d.system.util.ValidationUtils;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.user.UserCredentials;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional // TODO do we need transactions at all here?
@Service( "com.mass3d.email.EmailService" )
public class DefaultEmailService
    implements EmailService
{
    private static final String TEST_EMAIL_SUBJECT = "Test email from DHIS 2";
    private static final String TEST_EMAIL_TEXT = "This is an automatically generated email from ";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private MessageSender emailMessageSender;

    private CurrentUserService currentUserService;

    private SystemSettingManager systemSettingManager;

    public DefaultEmailService( MessageSender emailMessageSender, CurrentUserService currentUserService,
        SystemSettingManager systemSettingManager )
    {
        checkNotNull( emailMessageSender );
        checkNotNull( currentUserService );
        checkNotNull( emailMessageSender );

        this.emailMessageSender = emailMessageSender;
        this.currentUserService = currentUserService;
        this.systemSettingManager = systemSettingManager;
    }

    // -------------------------------------------------------------------------
    // EmailService implementation
    // -------------------------------------------------------------------------

    @Override
    public boolean emailConfigured()
    {
        return systemSettingManager.emailConfigured();
    }

    @Override
    public OutboundMessageResponse sendEmail( Email email )
    {
        return emailMessageSender.sendMessage( email.getSubject(), email.getText(), null, email.getSender(), email.getRecipients(), true );
    }

    @Override
    public OutboundMessageResponse sendEmail( String subject, String message, Set<String> recipients )
    {
        return emailMessageSender.sendMessage( subject, message, recipients );
    }

    @Override
    public OutboundMessageResponse sendTestEmail()
    {
        String instanceName = (String) systemSettingManager.getSystemSetting( SettingKey.APPLICATION_TITLE );
        
        Email email = new Email( TEST_EMAIL_SUBJECT, TEST_EMAIL_TEXT + instanceName, null, Sets.newHashSet( currentUserService.getCurrentUser() ) );
        
        return sendEmail( email );
    }

    @Override
    public OutboundMessageResponse sendSystemEmail( Email email )
    {
        OutboundMessageResponse response = new OutboundMessageResponse();

        String recipient = (String) systemSettingManager.getSystemSetting( SettingKey.SYSTEM_NOTIFICATIONS_EMAIL );
        String appTitle = (String) systemSettingManager.getSystemSetting( SettingKey.APPLICATION_TITLE );

        if ( recipient == null || !ValidationUtils.emailIsValid( recipient ) )
        {
            response.setOk( false );
            response.setDescription( "No recipient found" );

            return response;
        }        
        
        User user = new User();
        UserCredentials credentials = new UserCredentials();
        credentials.setUsername( recipient );
        user.setEmail( recipient );
        
        User sender = new User();
        sender.setFirstName( StringUtils.trimToEmpty( appTitle ) );
        sender.setSurname( recipient );
        
        return emailMessageSender.sendMessage( email.getSubject(), email.getText(), null, sender, Sets
            .newHashSet( user ), true );
    }
}
