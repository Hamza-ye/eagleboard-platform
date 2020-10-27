package com.mass3d.scheduling.parameters.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mass3d.scheduling.parameters.SmsJobParameters;

public class SmsJobParametersDeserializer extends AbstractJobParametersDeserializer<SmsJobParameters>
{
    public SmsJobParametersDeserializer()
    {
        super( SmsJobParameters.class, CustomJobParameters.class );
    }

    @JsonDeserialize
    public static class CustomJobParameters extends SmsJobParameters
    {
    }
}
