package com.mass3d.dxf2.synch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;
import org.springframework.http.HttpStatus;

@JacksonXmlRootElement( localName = "availabilityStatus", namespace = DxfNamespaces.DXF_2_0 )
public class AvailabilityStatus
{
    private boolean available;

    private String message;
    
    private HttpStatus httpStatus;

    protected AvailabilityStatus()
    {
    }

    public AvailabilityStatus( boolean available, String message, HttpStatus httpStatus )
    {
        this.available = available;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isAvailable()
    {
        return available;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getMessage()
    {
        return message;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getStatusCode()
    {
        return httpStatus != null ? httpStatus.value() : null;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getStatusPhrase()
    {
        return httpStatus != null ? httpStatus.getReasonPhrase() : null;
    }

    @Override
    public String toString()
    {
        return "[Available: " + available + ", message: " + message + ", HTTP status: " + httpStatus + "]";
    }
}
