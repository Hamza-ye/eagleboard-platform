package com.mass3d.scheduling.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.scheduling.JobParameters;
import com.mass3d.scheduling.parameters.jackson.SmsJobParametersDeserializer;

@JacksonXmlRootElement( localName = "jobParameters", namespace = DxfNamespaces.DXF_2_0 )
@JsonDeserialize( using = SmsJobParametersDeserializer.class )
public class SmsJobParameters
    implements JobParameters
{
    private static final long serialVersionUID = -6116489359345047961L;

    private String smsSubject;

    private List<String> recipientsList = new ArrayList<>();

    private String message;

    public SmsJobParameters()
    {
    }

    public SmsJobParameters( String smsSubject, String message, List<String> recipientsList )
    {
        this.smsSubject = smsSubject;
        this.recipientsList = recipientsList;
        this.message = message;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getSmsSubject()
    {
        return smsSubject;
    }

    public void setSmsSubject( String smsSubject )
    {
        this.smsSubject = smsSubject;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "recipientsLists", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "recipientsList", namespace = DxfNamespaces.DXF_2_0 )
    public List<String> getRecipientsList()
    {
        return recipientsList;
    }

    public void setRecipientsList( List<String> recipientsList )
    {
        this.recipientsList = recipientsList;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    @Override
    public Optional<ErrorReport> validate()
    {
        return Optional.empty();
    }
}
