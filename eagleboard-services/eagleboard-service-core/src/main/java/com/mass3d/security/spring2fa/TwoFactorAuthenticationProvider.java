package com.mass3d.security.spring2fa;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.LongValidator;
import com.mass3d.security.SecurityService;
import com.mass3d.security.SecurityUtils;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwoFactorAuthenticationProvider extends DaoAuthenticationProvider
{
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    public TwoFactorAuthenticationProvider( @Qualifier( "userDetailsService" ) UserDetailsService detailsService,
        PasswordEncoder passwordEncoder )
    {
        setUserDetailsService( detailsService );
        setPasswordEncoder( passwordEncoder );
    }

    @Override
    public Authentication authenticate( Authentication auth )
        throws AuthenticationException
    {
        log.info( String.format( "Login attempt: %s", auth.getName() ) );

        String username = auth.getName();

        UserCredentials userCredentials = userService.getUserCredentialsWithEagerFetchAuthorities( username );

        if ( userCredentials == null )
        {
            throw new BadCredentialsException( "Invalid username or password" );
        }

        // Initialize all required properties of user credentials since these will become detached

        userCredentials.getAllAuthorities();

        // -------------------------------------------------------------------------
        // Check two-factor authentication
        // -------------------------------------------------------------------------

        if ( userCredentials.isTwoFA() )
        {
            TwoFactorWebAuthenticationDetails authDetails =
                (TwoFactorWebAuthenticationDetails) auth.getDetails();

            // -------------------------------------------------------------------------
            // Check whether account is locked due to multiple failed login attempts
            // -------------------------------------------------------------------------

            if ( authDetails == null )
            {
                log.info( "Missing authentication details in authentication request." );
                throw new PreAuthenticatedCredentialsNotFoundException(
                    "Missing authentication details in authentication request." );
            }

            String ip = authDetails.getIp();
            String code = StringUtils.deleteWhitespace( authDetails.getCode() );

            if ( securityService.isLocked( username ) )
            {
                log.info( String.format( "Temporary lockout for user: %s and IP: %s", username, ip ) );

                throw new LockedException( String.format( "IP is temporarily locked: %s", ip ) );
            }

            if ( !LongValidator.getInstance().isValid( code ) || !SecurityUtils.verify( userCredentials, code ) )
            {
                log.info(
                    String.format( "Two-factor authentication failure for user: %s", userCredentials.getUsername() ) );

                throw new BadCredentialsException( "Invalid verification code" );
            }
        }

        // -------------------------------------------------------------------------
        // Delegate authentication downstream, using UserCredentials as principal
        // -------------------------------------------------------------------------

        Authentication result = super.authenticate( auth );

        // Put detached state of the user credentials into the session as user
        // credentials must not be updated during session execution

        userCredentials = SerializationUtils.clone( userCredentials );

        // Initialize cached authorities

        userCredentials.isSuper();
        userCredentials.getAllAuthorities();

        return new UsernamePasswordAuthenticationToken( userCredentials, result.getCredentials(),
            result.getAuthorities() );
    }

    @Override
    public boolean supports( Class<?> authentication )
    {
        return authentication.equals( UsernamePasswordAuthenticationToken.class );
    }
}
