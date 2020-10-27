package com.mass3d.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Set;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.user.User;

@JacksonXmlRootElement( localName = "email", namespace = DxfNamespaces.DXF_2_0 )
public class Email
{
    private String subject;

    private String text;

    private User sender;

    private Set<User> recipients;

    // -------------------------------------------------------------------------
    // Contructors
    // -------------------------------------------------------------------------

    public Email()
    {
    }

    public Email( String subject, String text )
    {
        this.subject = subject;
        this.text = text;
    }

    public Email( String subject, String text, User sender, Set<User> recipients )
    {
        this.subject = subject;
        this.text = text;
        this.sender = sender;
        this.recipients = recipients;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getSubject()
    {
        return subject;
    }

    public void setSubject( String subject )
    {
        this.subject = subject;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        this.text = text;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public User getSender()
    {
        return sender;
    }

    public void setSender( User sender )
    {
        this.sender = sender;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "recipients", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "recipient", namespace = DxfNamespaces.DXF_2_0 )
    public Set<User> getRecipients()
    {
        return recipients;
    }

    public void setRecipients( Set<User> recipients )
    {
        this.recipients = recipients;
    }
}
