package com.mass3d.sms.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.Date;
import com.mass3d.common.BaseIdentifiableObject;

@JacksonXmlRootElement( localName = "inboundsms" )
public class IncomingSms extends BaseIdentifiableObject
    implements Serializable
{
    private static final long serialVersionUID = 3954710607630454226L;

    private SmsMessageEncoding encoding = SmsMessageEncoding.ENC7BIT;

    private Date sentDate;

    private Date receivedDate;

    /*
     * The originator of the received message.
     */
    private String originator;

    /*
     * The ID of the gateway from which the message was received.
     */
    private String gatewayId;

    private String text;

    private byte[] bytes;

    private SmsMessageStatus status = SmsMessageStatus.INCOMING;

    private String statusMessage;

    private boolean parsed = false;

    public IncomingSms()
    {
        setAutoFields();
    }

    /**
     * Incoming smses are one of two types, text or binary.
     *
     * @return is this message a text (not binary) message?
     */
    public boolean isTextSms()
    {
        return text != null;
    }

    @JsonProperty( value = "smsencoding", defaultValue = "1" )
    @JacksonXmlProperty( localName = "smsencoding" )
    public SmsMessageEncoding getEncoding()
    {
        return encoding;
    }

    public void setEncoding( SmsMessageEncoding encoding )
    {
        this.encoding = encoding;
    }

    @JsonProperty( value = "sentdate" )
    @JacksonXmlProperty( localName = "sentdate" )
    public Date getSentDate()
    {
        return sentDate;
    }

    public void setSentDate( Date sentDate )
    {
        this.sentDate = sentDate;
    }

    @JsonProperty( value = "receiveddate" )
    @JacksonXmlProperty( localName = "receiveddate" )
    public Date getReceivedDate()
    {
        return receivedDate;
    }

    public void setReceivedDate( Date receivedDate )
    {
        this.receivedDate = receivedDate;
    }

    @JsonProperty( value = "originator" )
    @JacksonXmlProperty( localName = "originator" )
    public String getOriginator()
    {
        return originator;
    }

    public void setOriginator( String originator )
    {
        this.originator = originator;
    }

    @JsonProperty( value = "gatewayid", defaultValue = "unknown" )
    @JacksonXmlProperty( localName = "gatewayid" )
    public String getGatewayId()
    {
        return gatewayId;
    }

    public void setGatewayId( String gatewayId )
    {
        this.gatewayId = gatewayId;
    }

    @JsonProperty( value = "text" )
    @JacksonXmlProperty( localName = "text" )
    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        if ( bytes != null )
        {
            throw new IllegalArgumentException( "Text and bytes cannot both be set on incoming sms" );
        }
        this.text = text;
    }

    public byte[] getBytes()
    {
        return bytes;
    }

    public void setBytes( byte[] bytes )
    {
        if ( text != null )
        {
            throw new IllegalArgumentException( "Text and bytes cannot both be set on incoming sms" );
        }
        this.bytes = bytes;
    }

    @JsonProperty( value = "smsstatus", defaultValue = "1" )
    @JacksonXmlProperty( localName = "smsstatus" )
    public SmsMessageStatus getStatus()
    {
        return status;
    }

    public void setStatus( SmsMessageStatus status )
    {
        this.status = status;
    }

    public String getStatusMessage()
    {
        return statusMessage;
    }

    public void setStatusMessage( String statusMessage )
    {
        this.statusMessage = statusMessage;
    }

    public boolean isParsed()
    {
        return parsed;
    }

    public void setParsed( boolean parsed )
    {
        this.parsed = parsed;
    }
}