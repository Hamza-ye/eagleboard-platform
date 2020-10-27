package com.mass3d.webapi.filter;

import lombok.extern.slf4j.Slf4j;
import com.mass3d.external.conf.DhisConfigurationProvider;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.mass3d.external.conf.ConfigurationKey.MONITORING_LOG_REQUESTID_ENABLED;
import static com.mass3d.external.conf.ConfigurationKey.MONITORING_LOG_REQUESTID_HASHALGO;
import static com.mass3d.external.conf.ConfigurationKey.MONITORING_LOG_REQUESTID_MAXSIZE;

/**
 * This filter places an hashed version of the Session ID in the Log4j Mapped
 * Diagnostic Context (MDC) of log4j. The session id is then logged in all log
 * statements and can be used to correlate different requests.
 *
 */
@Slf4j
@Component
public class RequestIdentifierFilter
    extends
    OncePerRequestFilter
{
    private final static String SESSION_ID_KEY = "sessionId";

    /**
     * The hash algorithm to use (default is SHA-256)
     */
    private final String hashAlgo;

    /**
     * Set the maximum length of the String used as request id
     */
    private final int maxSize;

    private final static String IDENTIFIER_PREFIX = "ID";

    private final boolean enabled;

    public RequestIdentifierFilter( DhisConfigurationProvider dhisConfig )
    {
        this.hashAlgo = dhisConfig.getProperty( MONITORING_LOG_REQUESTID_HASHALGO );
        this.maxSize = Integer.parseInt( dhisConfig.getProperty( MONITORING_LOG_REQUESTID_MAXSIZE ) );
        this.enabled = dhisConfig.isEnabled( MONITORING_LOG_REQUESTID_ENABLED );
    }

    @Override
    protected void doFilterInternal( HttpServletRequest req, HttpServletResponse res, FilterChain chain )
        throws ServletException, IOException
    {
        if ( enabled )
        {
            try
            {
                MDC.put( SESSION_ID_KEY, IDENTIFIER_PREFIX + truncate( hashToBase64( req.getSession().getId() ) ) );

            }
            catch ( NoSuchAlgorithmException e )
            {
                log.error( String.format( "Invalid Hash algorithm provided (%s)", hashAlgo ), e );
            }
        }

        chain.doFilter( req, res );
    }

    private String truncate( String id )
    {
        // only truncate if MAX SIZE <> -1
        return id.substring( 0, (this.maxSize == -1 ? id.length() : this.maxSize) );
    }

    private String hashToBase64( String sessionId )
        throws NoSuchAlgorithmException
    {
        byte[] data = sessionId.getBytes();
        MessageDigest digester = MessageDigest.getInstance( hashAlgo );
        digester.update( data );
        return Base64.getEncoder().encodeToString( digester.digest() );
    }
}
