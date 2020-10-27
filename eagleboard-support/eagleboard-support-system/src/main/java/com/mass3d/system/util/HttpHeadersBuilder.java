package com.mass3d.system.util;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Builder of Spring {@link HttpHeaders} instances.
 * 
 */
public class HttpHeadersBuilder
{
    private HttpHeaders headers;
    
    public HttpHeadersBuilder()
    {
        this.headers = new HttpHeaders();
    }
    
    /**
     * Builds the {@link HttpHeaders} instance.
     */
    public HttpHeaders build()
    {
        return headers;
    }
    
    public HttpHeadersBuilder withContentTypeJson()
    {
        this.headers.set( HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE );
        return this;
    }
    
    public HttpHeadersBuilder withContentTypeXml()
    {
        this.headers.set( HttpHeaders.CONTENT_TYPE,  MediaType.APPLICATION_XML_VALUE );
        return this;
    }
    
    public HttpHeadersBuilder withAcceptJson()
    {
        this.headers.set( HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE );
        return this;
    }
    
    public HttpHeadersBuilder withAcceptXml()
    {
        this.headers.set( HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE );
        return this;
    }
    
    public HttpHeadersBuilder withNoCache()
    {
        this.headers.set( HttpHeaders.CACHE_CONTROL, CacheControl.noCache().getHeaderValue() );
        return this;
    }
    
    public HttpHeadersBuilder withBasicAuth( String username, String password )
    {
        this.headers.set( HttpHeaders.AUTHORIZATION, CodecUtils.getBasicAuthString( username, password ) );
        return this;
    }
}
