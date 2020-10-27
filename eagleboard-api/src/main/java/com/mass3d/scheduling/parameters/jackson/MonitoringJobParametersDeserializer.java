package com.mass3d.scheduling.parameters.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mass3d.scheduling.parameters.MonitoringJobParameters;

public class MonitoringJobParametersDeserializer extends AbstractJobParametersDeserializer<MonitoringJobParameters>
{
    public MonitoringJobParametersDeserializer()
    {
        super( MonitoringJobParameters.class, CustomJobParameters.class );
    }

    @JsonDeserialize
    public static class CustomJobParameters extends MonitoringJobParameters
    {
    }
}
