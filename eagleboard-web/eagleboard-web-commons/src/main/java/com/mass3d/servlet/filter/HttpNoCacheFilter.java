package com.mass3d.servlet.filter;

import com.mass3d.webapi.utils.ContextUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;

/**
 * Filter which enforces no cache for HTML pages like
 * index pages to prevent stale versions being rendered
 * in clients.
 *
 */
@WebFilter( urlPatterns = {
    "/*"
},
    initParams = {
        @WebInitParam( name = "urlPattern", value = "index\\.html|/$" )
    } )
public class HttpNoCacheFilter
    extends HttpUrlPatternFilter
{
    @Override
    public final void doHttpFilter( HttpServletRequest request, HttpServletResponse response, FilterChain chain )
        throws IOException, ServletException
    {
        if ( HttpMethod.GET == HttpMethod.resolve( request.getMethod() ) )
        {
            ContextUtils.setNoStore( response );
        }

        chain.doFilter( request, response );
    }
}
