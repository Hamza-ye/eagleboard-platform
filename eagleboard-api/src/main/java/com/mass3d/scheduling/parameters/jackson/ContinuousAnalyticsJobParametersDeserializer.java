package com.mass3d.scheduling.parameters.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mass3d.scheduling.parameters.ContinuousAnalyticsJobParameters;

public class ContinuousAnalyticsJobParametersDeserializer
    extends AbstractJobParametersDeserializer<ContinuousAnalyticsJobParameters>
{
    public ContinuousAnalyticsJobParametersDeserializer()
    {
        super( ContinuousAnalyticsJobParameters.class, CustomJobParameters.class );
    }

    @JsonDeserialize
    public static class CustomJobParameters extends ContinuousAnalyticsJobParameters
    {
    }
}
