package com.mass3d.scheduling.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.Optional;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.scheduling.JobParameters;

public class MockJobParameters
    implements JobParameters
{
    private static final long serialVersionUID = 3600315605964091689L;

    @JsonProperty
    private String message;

    public MockJobParameters()
    {
    }

    public MockJobParameters( String message )
    {
        this.message = message;
    }

    @JacksonXmlProperty
    @JsonProperty
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
