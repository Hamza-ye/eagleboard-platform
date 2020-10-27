package com.mass3d.scheduling.parameters.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mass3d.scheduling.parameters.PredictorJobParameters;

public class PredictorJobParametersDeserializer extends AbstractJobParametersDeserializer<PredictorJobParameters>
{
    public PredictorJobParametersDeserializer()
    {
        super( PredictorJobParameters.class, CustomJobParameters.class );
    }

    @JsonDeserialize
    public static class CustomJobParameters extends PredictorJobParameters
    {
    }

}
