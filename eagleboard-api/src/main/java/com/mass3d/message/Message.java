package com.mass3d.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Date;
import java.util.Set;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.CodeGenerator;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.fileresource.FileResource;
import com.mass3d.user.User;

@JacksonXmlRootElement( localName = "message", namespace = DxfNamespaces.DXF_2_0 )
public class Message
    extends BaseIdentifiableObject
{
    /**
     * The message text.
     */
    private String text;

    /**
     * The message meta data, like user agent and OS of sender.
     */
    private String metaData;

    /**
     * The message sender.
     */
    private User sender;

    /**
     * Internal message flag. Can only be seen by users in "FeedbackRecipients" group.
     */
    private Boolean internal;

    /**
     * Attached files
     */
    private Set<FileResource> attachments;

    public Message()
    {
        this.uid = CodeGenerator.generateUid();
        this.lastUpdated = new Date();
        this.internal = false;
    }

    public Message( String text, String metaData, User sender )
    {
        this.uid = CodeGenerator.generateUid();
        this.lastUpdated = new Date();
        this.text = text;
        this.metaData = metaData;
        this.sender = sender;
        this.internal = false;
    }

    public Message( String text, String metaData, User sender, boolean internal )
    {
        this.uid = CodeGenerator.generateUid();
        this.lastUpdated = new Date();
        this.text = text;
        this.metaData = metaData;
        this.sender = sender;
        this.internal = internal;
    }

    @Override
    public String getName()
    {
        return text;
    }

    @JsonProperty
    @JacksonXmlProperty
    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        this.text = text;
    }

    @JsonProperty
    @JacksonXmlProperty
    public String getMetaData()
    {
        return metaData;
    }

    public void setMetaData( String metaData )
    {
        this.metaData = metaData;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty
    public User getSender()
    {
        return sender;
    }

    public void setSender( User sender )
    {
        this.sender = sender;
    }

    @Override
    public String toString()
    {
        return "[" + text + "]";
    }

    @JsonProperty
    @JacksonXmlProperty
    public boolean isInternal()
    {
        return internal;
    }

    public void setInternal( boolean internal )
    {
        this.internal = internal;
    }

    @JsonProperty
    @JacksonXmlProperty
    public Set<FileResource> getAttachments()
    {
        return attachments;
    }

    public void setAttachments( Set<FileResource> attachments )
    {
        this.attachments = attachments;
    }
}
