package com.mass3d.scheduling.parameters.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mass3d.scheduling.parameters.PushAnalysisJobParameters;

public class PushAnalysisJobParametersDeserializer extends AbstractJobParametersDeserializer<PushAnalysisJobParameters>
{
    public PushAnalysisJobParametersDeserializer()
    {
        super( PushAnalysisJobParameters.class, CustomJobParameters.class );
    }

    @JsonDeserialize
    public static class CustomJobParameters extends PushAnalysisJobParameters
    {
    }
}
