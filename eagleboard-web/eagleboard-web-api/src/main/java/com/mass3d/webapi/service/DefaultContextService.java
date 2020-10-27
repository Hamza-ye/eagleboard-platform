package com.mass3d.webapi.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DefaultContextService implements ContextService
{
    private static Pattern API_VERSION = Pattern.compile( "(/api/(\\d+)?/)" );

    @Override
    public String getServletPath()
    {
        return getContextPath() + getRequest().getServletPath();
    }

    @Override
    public String getContextPath()
    {
        HttpServletRequest request = getRequest();
        StringBuilder builder = new StringBuilder();
        String xForwardedProto = request.getHeader( "X-Forwarded-Proto" );
        String xForwardedPort = request.getHeader( "X-Forwarded-Port" );

        if ( xForwardedProto != null && (xForwardedProto.equalsIgnoreCase( "http" ) || xForwardedProto.equalsIgnoreCase( "https" )) )
        {
            builder.append( xForwardedProto );
        }
        else
        {
            builder.append( request.getScheme() );
        }

        builder.append( "://" ).append( request.getServerName() );

        int port;

        try
        {
            port = Integer.parseInt( xForwardedPort );
        }
        catch ( NumberFormatException e )
        {
            port = request.getServerPort();
        }

        if ( port != 80 && port != 443 )
        {
            builder.append( ":" ).append( port );
        }

        builder.append( request.getContextPath() );

        return builder.toString();
    }

    @Override
    public String getApiPath()
    {
        HttpServletRequest request = getRequest();
        Matcher matcher = API_VERSION.matcher( request.getRequestURI() );
        String version = "";

        if ( matcher.find() )
        {
            version = "/" + matcher.group( 2 );
        }

        return getServletPath() + version;
    }

    @Override
    public HttpServletRequest getRequest()
    {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    @Override
    public Set<String> getParameterValues( String name )
    {
        if ( getRequest().getParameterValues( name ) == null )
        {
            return Sets.newHashSet();
        }

        Set<String> parameter = Sets.newHashSet();
        String[] parameterValues = getRequest().getParameterValues( name );
        Collections.addAll( parameter, parameterValues );

        return parameter;
    }

    @Override
    public Map<String, List<String>> getParameterValuesMap()
    {
        Map<String, String[]> parameterMap = getRequest().getParameterMap();
        Map<String, List<String>> map = new HashMap<>();

        for ( String key : parameterMap.keySet() )
        {
            map.put( key, Lists.newArrayList( parameterMap.get( key ) ) );
        }

        return map;
    }
}
