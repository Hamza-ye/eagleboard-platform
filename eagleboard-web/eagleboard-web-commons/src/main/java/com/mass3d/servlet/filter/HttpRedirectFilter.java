package com.mass3d.servlet.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @version $Id: HttpRedirectFilter.java 2869 2007-02-20 14:26:09Z andegje $
 */
@WebFilter( urlPatterns = {
    "/"
},
    initParams = {
        @WebInitParam( name = "redirectPath", value = "dhis-web-commons-about/redirect.action" ),
        @WebInitParam( name = "urlPattern", value = "index\\.html|/$" )
    } )
public class HttpRedirectFilter
    implements Filter
{
    public static final String REDIRECT_PATH_KEY = "redirectPath";

    private static final Log log = LogFactory.getLog( HttpRedirectFilter.class );
    
    private String redirectPath;

    // -------------------------------------------------------------------------
    // Filter implementation
    // -------------------------------------------------------------------------

    @Override
    public void init( FilterConfig config )
        throws ServletException
    {
        redirectPath = config.getInitParameter( REDIRECT_PATH_KEY );
    }

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
        throws IOException, ServletException
    {
        log.debug( "Redirecting to: " + redirectPath );
        
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if ( redirectPath == null )
        {
            String msg = "HttpRedirectFilter was not properly initialised. \"" + REDIRECT_PATH_KEY + "\" must be specified.";
            
            httpResponse.setContentType( "text/plain" );
            httpResponse.getWriter().print( msg );

            log.warn( msg );
            
            return;
        }

        httpResponse.sendRedirect( redirectPath );

        return;
    }

    @Override
    public void destroy()
    {
    }
}
