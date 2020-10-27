package com.mass3d.webapi.oprovider;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import com.mass3d.security.SecurityService;
import com.mass3d.security.oauth2.DefaultClientDetailsUserDetailsService;
import com.mass3d.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DhisOauthAuthenticationProvider extends DaoAuthenticationProvider
{
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    public DhisOauthAuthenticationProvider(
        @Qualifier( "defaultClientDetailsUserDetailsService" ) DefaultClientDetailsUserDetailsService detailsService )
    {
        setUserDetailsService( detailsService );
        setPasswordEncoder( NoOpPasswordEncoder.getInstance() );
    }

    @Override
    public Authentication authenticate( Authentication auth )
        throws AuthenticationException
    {
        log.info( String.format( "DhisOauthAuthenticationProvider authenticate attempt Authentication.getName(): %s",
            auth.getName() ) );

        String username = auth.getName();

        UserDetails userCredentials = getUserDetailsService().loadUserByUsername( username );

        if ( userCredentials == null )
        {
            throw new BadCredentialsException( "Invalid username or password" );
        }

        // -------------------------------------------------------------------------
        // Delegate authentication downstream, using UserCredentials as principal
        // -------------------------------------------------------------------------

        Authentication result = super.authenticate( auth );

        // Put detached state of the user credentials into the session as user
        // credentials must not be updated during session execution

        userCredentials = SerializationUtils.clone( userCredentials );

        return new UsernamePasswordAuthenticationToken( userCredentials, result.getCredentials(),
            result.getAuthorities() );
    }

    @Override
    public boolean supports( Class<?> authentication )
    {
        return authentication.equals( UsernamePasswordAuthenticationToken.class );
    }
}
