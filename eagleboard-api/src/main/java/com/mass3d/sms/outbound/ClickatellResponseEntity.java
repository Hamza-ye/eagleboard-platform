package com.mass3d.sms.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import java.util.Map;

@JacksonXmlRootElement( localName = "responseEntity" )
public class ClickatellResponseEntity
{
    private Map<Object, List<Map<Object, Object>>> data;

    @JsonProperty( value = "data" )
    @JacksonXmlProperty( localName = "data" )
    public Map<Object, List<Map<Object, Object>>> getData()
    {
        return data;
    }

    public void setData( Map<Object, List<Map<Object, Object>>> data )
    {
        this.data = data;
    }
}
