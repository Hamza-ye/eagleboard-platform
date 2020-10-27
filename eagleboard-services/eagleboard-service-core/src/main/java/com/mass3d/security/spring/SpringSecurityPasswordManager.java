package com.mass3d.security.spring;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.security.PasswordManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.security.PasswordManager" )
public class SpringSecurityPasswordManager
    implements PasswordManager
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final PasswordEncoder passwordEncoder;

    public SpringSecurityPasswordManager( PasswordEncoder passwordEncoder )
    {
        checkNotNull( passwordEncoder );

        this.passwordEncoder = passwordEncoder;
    }

    // -------------------------------------------------------------------------
    // PasswordManager implementation
    // -------------------------------------------------------------------------

    @Override
    public final String encode( String password )
    {
        return passwordEncoder.encode( password );
    }

    @Override
    public boolean matches( String rawPassword, String encodedPassword )
    {
        return passwordEncoder.matches( rawPassword, encodedPassword );
    }

    @Override
    public String getPasswordEncoderClassName()
    {
        return passwordEncoder.getClass().getName();
    }
}
