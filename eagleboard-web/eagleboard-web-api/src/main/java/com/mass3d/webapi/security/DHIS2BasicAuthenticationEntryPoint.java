package com.mass3d.webapi.security;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang.exception.ExceptionUtils;
import com.mass3d.dxf2.webmessage.WebMessageUtils;
import com.mass3d.render.RenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DHIS2BasicAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint
{
    @Autowired
    private RenderService renderService;

    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public DHIS2BasicAuthenticationEntryPoint( String loginFormUrl )
    {
        super( loginFormUrl );
    }

    @Override
    public void commence( HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException )
        throws IOException, ServletException
    {
        String acceptHeader = MoreObjects.firstNonNull( request.getHeader( "Accept" ), "" );
        String requestWithHeader = MoreObjects.firstNonNull( request.getHeader( "X-Requested-With" ), "" );
        String authorizationHeader = MoreObjects.firstNonNull( request.getHeader( "Authorization" ), "" );

        if ( "XMLHttpRequest".equals( requestWithHeader ) || authorizationHeader.contains( "Basic" ) )
        {
            String message = "Unauthorized";

            if ( ExceptionUtils.indexOfThrowable( authException, LockedException.class ) != -1 )
            {
                message = "Account locked";
            }

            response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );

            if ( acceptHeader.contains( MediaType.APPLICATION_XML_VALUE ) )
            {
                response.setContentType( MediaType.APPLICATION_XML_VALUE );
                renderService.toXml( response.getOutputStream(), WebMessageUtils.unathorized( message ) );
            }
            else
            {
                response.setContentType( MediaType.APPLICATION_JSON_VALUE );
                renderService.toJson( response.getOutputStream(), WebMessageUtils.unathorized( message ) );
            }

            return;
        }

        super.commence( request, response, authException );
    }
}
