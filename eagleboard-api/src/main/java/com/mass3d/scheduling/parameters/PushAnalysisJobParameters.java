package com.mass3d.scheduling.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.scheduling.JobParameters;
import com.mass3d.scheduling.parameters.jackson.PushAnalysisJobParametersDeserializer;

@JacksonXmlRootElement( localName = "jobParameters", namespace = DxfNamespaces.DXF_2_0 )
@JsonDeserialize( using = PushAnalysisJobParametersDeserializer.class )
public class PushAnalysisJobParameters
    implements JobParameters
{
    private static final long serialVersionUID = -1848833906375595488L;

    private List<String> pushAnalysis = new ArrayList<>();

    public PushAnalysisJobParameters()
    {
    }

    public PushAnalysisJobParameters( String pushAnalysis )
    {
        this.pushAnalysis.add( pushAnalysis );
    }

    public PushAnalysisJobParameters( List<String> pushAnalysis )
    {
        this.pushAnalysis = pushAnalysis;
    }

    @JsonProperty( required = true )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public List<String> getPushAnalysis()
    {
        return pushAnalysis;
    }

    public void setPushAnalysis( List<String> pushAnalysis )
    {
        this.pushAnalysis = pushAnalysis;
    }

    @Override
    public Optional<ErrorReport> validate()
    {
        if ( pushAnalysis == null || pushAnalysis.isEmpty() )
        {
            return Optional
                .of( new ErrorReport( this.getClass(), ErrorCode.E4014, pushAnalysis, "pushAnalysis" ) );
        }

        return Optional.empty();
    }
}
