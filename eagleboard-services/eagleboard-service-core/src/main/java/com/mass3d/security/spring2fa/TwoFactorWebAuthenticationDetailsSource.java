package com.mass3d.security.spring2fa;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class TwoFactorWebAuthenticationDetailsSource
    implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails>
{
    @Override
    public WebAuthenticationDetails buildDetails( HttpServletRequest request )
    {
        return new TwoFactorWebAuthenticationDetails( request );
    }
}

