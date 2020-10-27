package com.mass3d.scheduling.parameters.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mass3d.scheduling.parameters.MetadataSyncJobParameters;

public class MetadataSyncJobParametersDeserializer
    extends AbstractJobParametersDeserializer<MetadataSyncJobParameters>
{
    public MetadataSyncJobParametersDeserializer()
    {
        super( MetadataSyncJobParameters.class, CustomJobParameters.class );
    }

    @JsonDeserialize private static class CustomJobParameters extends MetadataSyncJobParameters
    {
    }
}