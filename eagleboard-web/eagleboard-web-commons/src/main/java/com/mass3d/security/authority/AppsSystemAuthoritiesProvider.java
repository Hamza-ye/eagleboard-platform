package com.mass3d.security.authority;

import org.apache.commons.lang3.StringUtils;
import com.mass3d.appmanager.AppManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AppsSystemAuthoritiesProvider implements SystemAuthoritiesProvider
{
    private AppManager appManager;

    public AppsSystemAuthoritiesProvider( AppManager appManager )
    {
        this.appManager = appManager;
    }

    @Override
    public Collection<String> getSystemAuthorities()
    {
        Set<String> authorities = new HashSet<>();

        appManager.getApps( null ).stream()
            .filter( app -> !StringUtils.isEmpty( app.getShortName() ) && !app.getIsBundledApp() )
            .forEach( app -> {
                authorities.add( app.getSeeAppAuthority() );
                authorities.addAll( app.getAuthorities() );
            } );

        return authorities;
    }
}
