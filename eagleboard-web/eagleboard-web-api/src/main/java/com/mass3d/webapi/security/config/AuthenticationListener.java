package com.mass3d.webapi.security.config;

import java.util.Objects;

import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.security.SecurityService;
import com.mass3d.security.spring2fa.TwoFactorWebAuthenticationDetails;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationListener
{
    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @Autowired
    private DhisConfigurationProvider config;

    @EventListener
    public void handleAuthenticationFailure( AbstractAuthenticationFailureEvent event )
    {
        Authentication auth = event.getAuthentication();

        if ( TwoFactorWebAuthenticationDetails.class.isAssignableFrom( auth.getDetails().getClass() ) )
        {
            TwoFactorWebAuthenticationDetails authDetails =
                ( TwoFactorWebAuthenticationDetails ) auth.getDetails();

            log.info( String.format( "Login attempt failed for remote IP: %s", authDetails.getIp() ) );
        }

        securityService.registerFailedLogin( auth.getName() );
    }

    @EventListener({ InteractiveAuthenticationSuccessEvent.class, AuthenticationSuccessEvent.class })
    public void handleAuthenticationSuccess( AbstractAuthenticationEvent event )
    {
        Authentication auth = event.getAuthentication();

        if ( TwoFactorWebAuthenticationDetails.class.isAssignableFrom( auth.getDetails().getClass() ) )
        {
            TwoFactorWebAuthenticationDetails authDetails =
                ( TwoFactorWebAuthenticationDetails ) auth.getDetails();

            log.debug( String.format( "Login attempt succeeded for remote IP: %s", authDetails.getIp() ) );
        }

        final String username = event.getAuthentication().getName();

        UserCredentials credentials = userService.getUserCredentialsByUsername( username );

        boolean readOnly = config.isReadOnlyMode();

        if ( Objects.nonNull( credentials ) && !readOnly )
        {
            credentials.updateLastLogin();
            userService.updateUserCredentials( credentials );
        }

        securityService.registerSuccessfulLogin( username );
    }
}
