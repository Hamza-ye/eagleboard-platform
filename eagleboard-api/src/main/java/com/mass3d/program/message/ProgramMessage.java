package com.mass3d.program.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DeliveryChannel;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramStageInstance;

@JacksonXmlRootElement( localName = "programMessage", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramMessage
    extends BaseIdentifiableObject
    implements Serializable
{
    private static final long serialVersionUID = -5882823752156937730L;

    private ProgramInstance programInstance;

    private ProgramStageInstance programStageInstance;

    private ProgramMessageRecipients recipients;

    private Set<DeliveryChannel> deliveryChannels = new HashSet<>();

    private ProgramMessageStatus messageStatus;

    private String subject;

    private String text;

    private Date processedDate;

    private transient boolean storeCopy = true;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramMessage()
    {
    }

    public ProgramMessage( String subject, String text, ProgramMessageRecipients recipients )
    {
        this.subject = subject;
        this.text = text;
        this.recipients = recipients;
    }

    public ProgramMessage( String subject, String text, ProgramMessageRecipients recipients, Set<DeliveryChannel> deliveryChannels,
        ProgramInstance programInstance )
    {
        this( subject, text, recipients );
        this.deliveryChannels = deliveryChannels;
        this.programInstance = programInstance;
    }

    public ProgramMessage( String subject, String text, ProgramMessageRecipients recipients, Set<DeliveryChannel> deliveryChannels,
        ProgramStageInstance programStageInstance )
    {
        this( subject, text, recipients );
        this.deliveryChannels = deliveryChannels;
        this.programStageInstance = programStageInstance;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean hasProgramInstance()
    {
        return this.programInstance != null;
    }

    public boolean hasProgramStageInstance()
    {
        return this.programStageInstance != null;
    }

    // -------------------------------------------------------------------------
    // Setters and getters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ProgramInstance getProgramInstance()
    {
        return programInstance;
    }

    public void setProgramInstance( ProgramInstance programInstance )
    {
        this.programInstance = programInstance;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ProgramStageInstance getProgramStageInstance()
    {
        return programStageInstance;
    }

    public void setProgramStageInstance( ProgramStageInstance programStageInstance )
    {
        this.programStageInstance = programStageInstance;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ProgramMessageRecipients getRecipients()
    {
        return recipients;
    }

    public void setRecipients( ProgramMessageRecipients programMessagerecipients )
    {
        this.recipients = programMessagerecipients;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "deliveryChannels", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "deliveryChannel", namespace = DxfNamespaces.DXF_2_0 )
    public Set<DeliveryChannel> getDeliveryChannels()
    {
        return deliveryChannels;
    }

    public void setDeliveryChannels( Set<DeliveryChannel> deliveryChannels )
    {
        this.deliveryChannels = deliveryChannels;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ProgramMessageStatus getMessageStatus()
    {
        return messageStatus;
    }

    public void setMessageStatus( ProgramMessageStatus messageStatus )
    {
        this.messageStatus = messageStatus;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getSubject()
    {
        return subject;
    }

    public void setSubject( String messageSubject )
    {
        this.subject = messageSubject;
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
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getProcessedDate()
    {
        return processedDate;
    }

    public void setProcessedDate( Date processedDate )
    {
        this.processedDate = processedDate;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isStoreCopy()
    {
        return storeCopy;
    }

    public void setStoreCopy( boolean storeCopy )
    {
        this.storeCopy = storeCopy;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "uid", uid )
            .add( "program stage instance", programStageInstance )
            .add( "program instance", programInstance )
            .add( "recipients", recipients )
            .add( "delivery channels", deliveryChannels )
            .add( "subject", subject )
            .add( "text", text )
            .toString();
    }
}
