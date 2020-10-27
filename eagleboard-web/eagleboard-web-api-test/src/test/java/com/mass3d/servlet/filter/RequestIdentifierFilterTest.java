package com.mass3d.servlet.filter;

import static org.hamcrest.Matchers.is;
import static com.mass3d.external.conf.ConfigurationKey.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.junit.MockitoJUnit.rule;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.webapi.filter.RequestIdentifierFilter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoRule;
import org.slf4j.MDC;

public class RequestIdentifierFilterTest
{
    @Mock
    private DhisConfigurationProvider dhisConfigurationProvider;

    private RequestIdentifierFilter subject;

    @Rule
    public MockitoRule mockitoRule = rule();

    private static final String DEFAULT_HASH_ALGO = MONITORING_LOG_REQUESTID_HASHALGO.getDefaultValue();

    @Before
    public void setUp()
    {
        MDC.clear();
    }

    @Test
    public void testHashSessionIdNoTruncate()
        throws ServletException,
        IOException
    {
        init( -1, DEFAULT_HASH_ALGO, true );

        doFilter();

        assertThat( MDC.get( "sessionId" ), is( "IDJShqHVoTDIhlsHjr7eIvUrMsMM7Gs2LYGjog1W6nQFo=" ) );

    }

    @Test
    public void testHashSessionIdWithTruncate()
        throws ServletException,
        IOException
    {
        init( 5, DEFAULT_HASH_ALGO, true );

        doFilter();

        assertThat( MDC.get( "sessionId" ), is( "IDJShqH" ) );

    }

    @Test
    public void testHashSessionIdWithMd5()
        throws ServletException,
        IOException
    {
        init( -1, "MD5", true );
        doFilter();

        assertThat( MDC.get( "sessionId" ), is( "IDrBKUxIZl6blN7EtczRa7fQ==" ) );

    }

    @Test
    public void testHashSessionIdWithInvalidAlgorithm()
        throws ServletException,
        IOException
    {
        init( -1, "RIJKA", true );
        doFilter();

        assertNull( MDC.get( "sessionId" ) );

    }

    @Test
    public void testisDisabled()
            throws ServletException,
            IOException
    {
        init( -1, "SHA-256", false );
        doFilter();

        assertNull( MDC.get( "sessionId" ) );

    }

    private void doFilter()
        throws ServletException,
        IOException
    {
        HttpServletRequest req = mock( HttpServletRequest.class );
        HttpServletResponse res = mock( HttpServletResponse.class );
        FilterChain filterChain = mock( FilterChain.class );

        HttpSession session = mock( HttpSession.class );

        when( req.getSession() ).thenReturn( session );
        when( session.getId() ).thenReturn( "ABCDEFGHILMNO" );

        subject.doFilter( req, res, filterChain );
    }

    private void init( int maxSize, String hashAlgo, boolean enabled )
    {

        when( dhisConfigurationProvider.getProperty( MONITORING_LOG_REQUESTID_MAXSIZE ) )
            .thenReturn( Integer.toString( maxSize ) );
        when( dhisConfigurationProvider.isEnabled( MONITORING_LOG_REQUESTID_ENABLED ) ).thenReturn( enabled );
        when( dhisConfigurationProvider.getProperty( MONITORING_LOG_REQUESTID_HASHALGO ) ).thenReturn( hashAlgo );

        subject = new RequestIdentifierFilter( dhisConfigurationProvider );
    }
}
