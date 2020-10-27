package com.mass3d.scheduling.parameters.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mass3d.scheduling.parameters.AnalyticsJobParameters;

public class AnalyticsJobParametersDeserializer extends AbstractJobParametersDeserializer<AnalyticsJobParameters>
{
    public AnalyticsJobParametersDeserializer()
    {
        super( AnalyticsJobParameters.class, CustomJobParameters.class );
    }

    @JsonDeserialize
    public static class CustomJobParameters extends AnalyticsJobParameters
    {
    }
}
