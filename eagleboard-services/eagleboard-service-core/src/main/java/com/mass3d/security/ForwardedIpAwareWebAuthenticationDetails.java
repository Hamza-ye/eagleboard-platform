package com.mass3d.security;

import javax.servlet.http.HttpServletRequest;
import com.mass3d.util.ObjectUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class ForwardedIpAwareWebAuthenticationDetails
    extends WebAuthenticationDetails
{
    private static final String HEADER_FORWARDED_FOR = "X-Forwarded-For";

    private String ip;

    public ForwardedIpAwareWebAuthenticationDetails( HttpServletRequest request )
    {
        super( request );
        this.ip = ObjectUtils.firstNonNull( request.getHeader( HEADER_FORWARDED_FOR ), request.getRemoteAddr() );
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp( String ip )
    {
        this.ip = ip;
    }
}
