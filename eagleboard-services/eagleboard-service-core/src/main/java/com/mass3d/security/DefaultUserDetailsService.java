package com.mass3d.security;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.system.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserService;
import com.mass3d.util.ObjectUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service( "userDetailsService" )
public class DefaultUserDetailsService
    implements UserDetailsService
{
    public static final String ID = UserDetailsService.class.getName();
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final UserService userService;
    
    private final SecurityService securityService;

    public DefaultUserDetailsService( UserService userService, SecurityService securityService )
    {
        checkNotNull( userService );
        checkNotNull( securityService );

        this.userService = userService;
        this.securityService = securityService;
    }

    // -------------------------------------------------------------------------
    // UserDetailsService implementation
    // -------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername( String username )
        throws UsernameNotFoundException, DataAccessException
    {
        UserCredentials credentials = userService.getUserCredentialsByUsername( username );

        // ---------------------------------------------------------------------
        // OpenId
        // ---------------------------------------------------------------------

        if ( credentials == null )
        {
            credentials = userService.getUserCredentialsByOpenId( username );

            if ( credentials == null )
            {
                throw new UsernameNotFoundException( "Username does not exist" );
            }
        }

        boolean enabled = !credentials.isDisabled();
        boolean credentialsNonExpired = userService.credentialsNonExpired( credentials );
        boolean accountNonLocked = !securityService.isLocked( credentials.getUsername() );

        if ( ObjectUtils.anyIsFalse( enabled, credentialsNonExpired, accountNonLocked ) )
        {
            log.info( String.format( "Login attempt for disabled/locked user: '%s', enabled: %b, credentials non-expired: %b, account non-locked: %b", 
                username, enabled, credentialsNonExpired, accountNonLocked ) );
        }
        
        return new User( credentials.getUsername(), credentials.getPassword(),
            enabled, true, credentialsNonExpired, accountNonLocked, SecurityUtils.getGrantedAuthorities( credentials ) );
    }
}
