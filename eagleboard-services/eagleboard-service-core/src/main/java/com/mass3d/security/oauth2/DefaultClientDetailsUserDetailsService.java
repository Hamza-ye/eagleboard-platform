package com.mass3d.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

@Service( "defaultClientDetailsUserDetailsService" )
public class DefaultClientDetailsUserDetailsService implements UserDetailsService
{

    private final DefaultClientDetailsService clientDetailsService;

    private String emptyPassword = "";

    @Autowired
    public DefaultClientDetailsUserDetailsService(
        @Qualifier( "defaultClientDetailsService" ) DefaultClientDetailsService clientDetailsService )
    {
        this.clientDetailsService = clientDetailsService;
    }

    /**
     * @param passwordEncoder the password encoder to set
     */
    public void setPasswordEncoder( PasswordEncoder passwordEncoder )
    {
        this.emptyPassword = passwordEncoder.encode( "" );
    }

    public UserDetails loadUserByUsername( String username )
    {
        ClientDetails clientDetails;
        try
        {
            clientDetails = clientDetailsService.loadClientByClientId( username );
        }
        catch ( NoSuchClientException e )
        {
            throw new UsernameNotFoundException( e.getMessage(), e );
        }
        String clientSecret = clientDetails.getClientSecret();
        if ( clientSecret == null || clientSecret.trim().length() == 0 )
        {
            clientSecret = emptyPassword;
        }
        return new User( username, clientSecret, clientDetails.getAuthorities() );
    }
}
