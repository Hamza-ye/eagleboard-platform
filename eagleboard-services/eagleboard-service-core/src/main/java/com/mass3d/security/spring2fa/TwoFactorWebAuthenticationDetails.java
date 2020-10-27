package com.mass3d.security.spring2fa;

import javax.servlet.http.HttpServletRequest;
import com.mass3d.security.ForwardedIpAwareWebAuthenticationDetails;

public class TwoFactorWebAuthenticationDetails
    extends ForwardedIpAwareWebAuthenticationDetails
{

    private static final String TWO_FACTOR_AUTHENTICATION_GETTER = "2fa_code";

    private String code;

    public TwoFactorWebAuthenticationDetails( HttpServletRequest request )
    {
        super( request );
        code = request.getParameter( TWO_FACTOR_AUTHENTICATION_GETTER );
    }

    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }
}

