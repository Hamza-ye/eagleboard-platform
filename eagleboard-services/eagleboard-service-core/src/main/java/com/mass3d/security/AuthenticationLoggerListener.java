package com.mass3d.security;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.util.ClassUtils;

@Slf4j
public class AuthenticationLoggerListener
    implements ApplicationListener<AbstractAuthenticationEvent>
{
    public void onApplicationEvent( AbstractAuthenticationEvent event )
    {
        if ( log.isWarnEnabled() )
        {
            final StringBuilder builder = new StringBuilder();
            builder.append( "Authentication event " );
            builder.append( ClassUtils.getShortName( event.getClass() ) );
            builder.append( ": " );
            builder.append( event.getAuthentication().getName() );

            Object details = event.getAuthentication().getDetails();

            if ( details != null &&
                ForwardedIpAwareWebAuthenticationDetails.class.isAssignableFrom( details.getClass() ) )
            {
                ForwardedIpAwareWebAuthenticationDetails authDetails = (ForwardedIpAwareWebAuthenticationDetails) details;
                String ip = authDetails.getIp();

                builder.append( "; ip: " );
                builder.append( ip );

                String sessionId = authDetails.getSessionId();
                if ( sessionId != null )
                {
                    HashCode hash = Hashing.sha256().newHasher().putString( sessionId, Charsets.UTF_8 ).hash();
                    builder.append( " sessionId: " );
                    builder.append( hash.toString() );
                }

            }

            if ( event instanceof AbstractAuthenticationFailureEvent )
            {
                builder.append( "; exception: " );
                builder.append( ((AbstractAuthenticationFailureEvent) event).getException().getMessage() );
            }

            log.warn( builder.toString() );
        }
    }
}
