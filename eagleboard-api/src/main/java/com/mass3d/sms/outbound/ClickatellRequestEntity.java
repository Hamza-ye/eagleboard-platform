package com.mass3d.sms.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import java.util.Set;

@JacksonXmlRootElement( localName = "requestEntityClickatell" )
public class ClickatellRequestEntity
{
    private String content;

    private Set<String> to;

    @JsonProperty( value = "content" )
    @JacksonXmlProperty( localName = "content" )
    public String getContent()
    {
        return content;
    }

    public void setContent( String content )
    {
        this.content = content;
    }

    @JsonProperty( value = "to" )
    @JacksonXmlProperty( localName = "to" )
    public Set<String> getTo()
    {
        return to;
    }

    public void setTo( Set<String> to )
    {
        this.to = to;
    }

    public String toString()
    {
        return MoreObjects.toStringHelper( this ).
            add( "content", content ).add( "to", to ).toString();
    }
}
