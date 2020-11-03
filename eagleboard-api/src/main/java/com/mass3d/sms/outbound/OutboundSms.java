package com.mass3d.sms.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Date;
import java.util.Set;
import com.mass3d.common.BaseIdentifiableObject;

@JacksonXmlRootElement( localName = "outboundsms" )
public class OutboundSms
    extends BaseIdentifiableObject
{
    public static final String DHIS_SYSTEM_SENDER = "DHIS-System";

    private String sender;

    private Set<String> recipients;

    private Date date;

    private String subject;

    private String message;

    private OutboundSmsStatus status = OutboundSmsStatus.OUTBOUND;

    public OutboundSms()
    {
        setAutoFields();
    }

    public OutboundSms( String subject, String message, Set<String> recipients )
    {
        this.subject = subject;
        this.message = message;
        this.recipients = recipients;
    }

    @JsonProperty( value = "recipients" )
    @JacksonXmlProperty( localName = "recipients" )
    public Set<String> getRecipients()
    {
        return recipients;
    }

    public void setRecipients( Set<String> recipients )
    {
        this.recipients = recipients;
    }

    @JsonProperty( value = "date" )
    public Date getDate()
    {
        return date;
    }

    public void setDate( Date date )
    {
        this.date = date;
    }

    @JsonProperty( value = "message" )
    @JacksonXmlProperty( localName = "message" )
    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    @JsonProperty( value = "sender" )
    public String getSender()
    {
        return sender;
    }

    public void setSender( String sender )
    {
        this.sender = sender;
    }

    @JsonProperty( value = "status" )
    public OutboundSmsStatus getStatus()
    {
        return status;
    }

    public void setStatus( OutboundSmsStatus status )
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "OutboundSMS [recipients=" + getNumbers() + ", message=" + message + "]";
    }

    private String getNumbers()
    {
        if ( this.recipients == null )
        {
            return null;
        }

        String numbers = "";

        for ( String recipient : this.recipients )
        {
            numbers += recipient + ", ";
        }

        return numbers.substring( 0, numbers.length() - 2 );
    }

    @JsonProperty( value = "subject" )
    public String getSubject()
    {
        return subject;
    }

    public void setSubject( String subject )
    {
        this.subject = subject;
    }
}
