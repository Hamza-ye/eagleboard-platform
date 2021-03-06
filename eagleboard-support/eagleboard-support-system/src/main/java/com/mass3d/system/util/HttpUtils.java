package com.mass3d.system.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * This class has the utility methods to invoke REST endpoints for various HTTP methods.
 *
 */
@Slf4j
public class HttpUtils
{
    private static final String CONTENT_TYPE_ZIP = "application/gzip";

    /**
     * Method to make an HTTP GET call to a given URL with/without authentication.
     *
     * @throws Exception </pre>
     */
    public static DhisHttpResponse httpGET( String requestURL, boolean authorize, String username, String password,
        Map<String, String> headers, int timeout, boolean processResponse ) throws Exception
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout( timeout )
            .setSocketTimeout( timeout )
            .build();

        DhisHttpResponse dhisHttpResponse;

        try
        {
            HttpGet httpGet = new HttpGet( requestURL );
            httpGet.setConfig( requestConfig );

            if ( headers instanceof Map)
            {
                for ( Map.Entry<String, String> e : headers.entrySet() )
                {
                    httpGet.addHeader( e.getKey(), e.getValue() );
                }
            }

            if ( authorize )
            {
                httpGet.setHeader( "Authorization", CodecUtils.getBasicAuthString( username, password ) );
            }

            HttpResponse response = httpClient.execute( httpGet );

            if ( processResponse )
            {
                dhisHttpResponse = processResponse( requestURL, username, response );
            }
            else
            {
                dhisHttpResponse = new DhisHttpResponse( response, null, response.getStatusLine().getStatusCode() );
            }
        }
        catch ( Exception e )
        {
            log.error( "Exception occurred in the httpGET call with username " + username, e );
            throw e;
        }
        finally
        {
            if ( httpClient != null )
            {
                httpClient.close();
            }
        }

        return dhisHttpResponse;
    }

    /**
     * Method to make an HTTP POST call to a given URL with/without authentication.
     *
     */
    public static DhisHttpResponse httpPOST( String requestURL, Object body, boolean authorize, String username, String password,
        String contentType, int timeout ) throws Exception
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout( timeout )
            .setSocketTimeout( timeout )
            .build();

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        DhisHttpResponse dhisHttpResponse;

        try
        {
            HttpPost httpPost = new HttpPost( requestURL );
            httpPost.setConfig( requestConfig );

            if ( body instanceof Map)
            {
                @SuppressWarnings( "unchecked" )
                Map<String, String> parameters = (Map<String, String>) body;
                for ( Map.Entry<String, String> parameter : parameters.entrySet() )
                {
                    if ( parameter.getValue() != null )
                    {
                        pairs.add( new BasicNameValuePair( parameter.getKey(), parameter.getValue() ) );
                    }
                }
                httpPost.setEntity( new UrlEncodedFormEntity( pairs, "UTF-8" ) );
            }
            else if ( body instanceof String)
            {
                httpPost.setEntity( new StringEntity( (String) body ) );
            }

            if ( !StringUtils.isNotEmpty( contentType ) )
            {
                httpPost.setHeader( "Content-Type", contentType );
            }

            if ( authorize )
            {
                httpPost.setHeader( "Authorization", CodecUtils.getBasicAuthString( username, password ) );
            }

            HttpResponse response = httpClient.execute( httpPost );
            log.info( "Successfully got response from http POST." );
            dhisHttpResponse = processResponse( requestURL, username, response );
        }
        catch ( Exception e )
        {
            log.error( "Exception occurred in httpPOST call with username " + username, e );
            throw e;

        }
        finally
        {
            if ( httpClient != null )
            {
                httpClient.close();
            }
        }

        return dhisHttpResponse;
    }

    /**
     * Method to make an HTTP DELETE call to a given URL with/without authentication.
     *
     * @throws Exception </pre>
     */
    public static DhisHttpResponse httpDELETE( String requestURL, boolean authorize, String username, String password,
        Map<String, String> headers, int timeout ) throws Exception
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout( timeout )
            .setSocketTimeout( timeout )
            .build();

        DhisHttpResponse dhisHttpResponse = null;

        try
        {
            HttpDelete httpDelete = new HttpDelete( requestURL );
            httpDelete.setConfig( requestConfig );

            if ( headers instanceof Map)
            {
                for ( Map.Entry<String, String> e : headers.entrySet() )
                {
                    httpDelete.addHeader( e.getKey(), e.getValue() );
                }
            }

            if ( authorize )
            {
                httpDelete.setHeader( "Authorization", CodecUtils.getBasicAuthString( username, password ) );
            }

            HttpResponse response = httpClient.execute( httpDelete );
            dhisHttpResponse = processResponse( requestURL, username, response );

            return dhisHttpResponse;
        }
        catch ( Exception e )
        {
            log.error( "exception occurred in httpDELETE call with username " + username, e );
            throw e;
        }
        finally
        {
            if ( httpClient != null )
            {
                httpClient.close();
            }
        }
    }


    /**
     * Processes the HttpResponse to create a DHisHttpResponse object.
     *
     * @throws IOException </pre>
     */
    private static DhisHttpResponse processResponse( String requestURL, String username, HttpResponse response )
        throws Exception
    {
        DhisHttpResponse dhisHttpResponse;
        String output;
        int statusCode;
        if ( response != null )
        {
            HttpEntity responseEntity = response.getEntity();

            if ( responseEntity != null && responseEntity.getContent() != null )
            {
                Header contentType = response.getEntity().getContentType();

                if ( contentType != null && checkIfGzipContentType( contentType ) )
                {
                    GzipDecompressingEntity gzipDecompressingEntity = new GzipDecompressingEntity( response.getEntity() );
                    InputStream content = gzipDecompressingEntity.getContent();
                    output = IOUtils.toString( content, StandardCharsets.UTF_8 );
                }
                else
                {
                    output = EntityUtils.toString( response.getEntity() );
                }
                statusCode = response.getStatusLine().getStatusCode();
            }
            else
            {
                throw new Exception( "No content found in the response received from http POST call to " + requestURL + " with username " + username );
            }

            dhisHttpResponse = new DhisHttpResponse( response, output, statusCode );
        }
        else
        {
            throw new Exception( "NULL response received from http POST call to " + requestURL + " with username " + username );
        }

        return dhisHttpResponse;
    }

    private static boolean checkIfGzipContentType( Header contentType )
    {
        return contentType.getValue().contains( CONTENT_TYPE_ZIP );
    }
}
