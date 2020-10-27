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
import com.mass3d.scheduling.parameters.jackson.PredictorJobParametersDeserializer;

@JacksonXmlRootElement( localName = "jobParametesr", namespace = DxfNamespaces.DXF_2_0 )
@JsonDeserialize( using = PredictorJobParametersDeserializer.class )
public class PredictorJobParameters
    implements JobParameters
{
    private static final long serialVersionUID = 5526554074518768146L;

    private int relativeStart;

    private int relativeEnd;

    private List<String> predictors = new ArrayList<>();

    private List<String> predictorGroups = new ArrayList<>();

    public PredictorJobParameters()
    {
    }

    public PredictorJobParameters( int relativeStart, int relativeEnd, List<String> predictors, List<String> predictorGroups )
    {
        this.relativeStart = relativeStart;
        this.relativeEnd = relativeEnd;
        this.predictors = predictors;
        this.predictorGroups = predictorGroups;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getRelativeStart()
    {
        return relativeStart;
    }

    public void setRelativeStart( int relativeStart )
    {
        this.relativeStart = relativeStart;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getRelativeEnd()
    {
        return relativeEnd;
    }

    public void setRelativeEnd( int relativeEnd )
    {
        this.relativeEnd = relativeEnd;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "predictors", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "predictor", namespace = DxfNamespaces.DXF_2_0 )
    public List<String> getPredictors()
    {
        return predictors;
    }

    public void setPredictors( List<String> predictors )
    {
        this.predictors = predictors;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "predictorGroups", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "predictorGroup", namespace = DxfNamespaces.DXF_2_0 )
    public List<String> getPredictorGroups()
    {
        return predictorGroups;
    }

    public void setPredictorGroups( List<String> predictorGroups )
    {
        this.predictorGroups = predictorGroups;
    }

    @Override
    public Optional<ErrorReport> validate()
    {
        return Optional.empty();
    }
}
