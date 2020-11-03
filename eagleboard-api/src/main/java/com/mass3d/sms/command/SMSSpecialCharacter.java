package com.mass3d.sms.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;

/**
 *
 * @version SMSSpecialCharacter.java 1:57:35 PM Nov 18, 2013 $
 */
@JacksonXmlRootElement( localName = "smsspecialcharacter", namespace = DxfNamespaces.DXF_2_0 )
public class SMSSpecialCharacter
{
    private int id;

    private String name;

    private String value;

    public SMSSpecialCharacter()
    {
    }

    public SMSSpecialCharacter( String name, String value )
    {
        this.name = name;
        this.value = value;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    @JsonProperty
    @JacksonXmlProperty
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @JsonProperty
    @JacksonXmlProperty
    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }
}
