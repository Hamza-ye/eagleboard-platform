package com.mass3d.security.basic;

import javax.servlet.http.HttpServletRequest;
import com.mass3d.security.ForwardedIpAwareWebAuthenticationDetails;

public class HttpBasicWebAuthenticationDetails
    extends ForwardedIpAwareWebAuthenticationDetails
{

    public HttpBasicWebAuthenticationDetails( HttpServletRequest request )
    {
        super( request );
    }

}

