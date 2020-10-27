package com.mass3d.security.spring;

import java.util.Set;
import java.util.stream.Collectors;
import com.mass3d.user.CurrentUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class AbstractSpringSecurityCurrentUserService implements CurrentUserService
{
    @Override
    public String getCurrentUsername()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if ( authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null )
        {
            return null;
        }

        Object principal = authentication.getPrincipal();

        // Principal being a string implies anonymous authentication
        // This is the state before the user is authenticated.
        if ( principal instanceof String)
        {
            if ( !"anonymousUser".equals( (String) principal ) )
            {
                return null;
            }

            return (String) principal;
        }

        if ( principal instanceof UserDetails )
        {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }

//        if ( principal instanceof DhisOidcUser )
//        {
//            DhisOidcUser dhisOidcUser = (DhisOidcUser) authentication.getPrincipal();
//            return dhisOidcUser.getUserCredentials().getUsername();
//        }

        throw new RuntimeException( "Authentication principal is not supported; principal:" + principal );
    }

    public Set<String> getCurrentUserAuthorities()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if ( principal instanceof UserDetails )
        {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getAuthorities().stream().map( GrantedAuthority::getAuthority )
                .collect( Collectors.toSet() );
        }

//        if ( principal instanceof DhisOidcUser )
//        {
//            DhisOidcUser dhisOidcUser = (DhisOidcUser) authentication.getPrincipal();
//            return dhisOidcUser.getAuthorities().stream().map( GrantedAuthority::getAuthority )
//                .collect( Collectors.toSet() );
//        }

        throw new RuntimeException( "Authentication principal is not supported; principal:" + principal );
    }
}
