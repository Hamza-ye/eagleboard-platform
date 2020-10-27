package com.mass3d.system.util;

import org.apache.http.HttpResponse;

public class DhisHttpResponse
{
    private HttpResponse httpResponse;

    private String response;

    private int statusCode;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public DhisHttpResponse( HttpResponse httpResponse, String response, int statusCode )
    {
        super();
        this.httpResponse = httpResponse;
        this.response = response;
        this.statusCode = statusCode;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public HttpResponse getHttpResponse()
    {
        return httpResponse;
    }

    public void setHttpResponse( HttpResponse httpResponse )
    {
        this.httpResponse = httpResponse;
    }

    public String getResponse()
    {
        return response;
    }

    public void setResponse( String response )
    {
        this.response = response;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode( int statusCode )
    {
        this.statusCode = statusCode;
    }

    @Override
    public String toString()
    {
        return "DhisHttpResponse [httpResponse=" + httpResponse + ", response=" + response + " statusCode=" + statusCode + "]";
    }
}
