package com.mass3d.scheduling.parameters.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mass3d.scheduling.parameters.DataSynchronizationJobParameters;

public class DataSynchronizationJobParametersDeserializer
    extends AbstractJobParametersDeserializer<DataSynchronizationJobParameters>
{
    public DataSynchronizationJobParametersDeserializer()
    {
        super( DataSynchronizationJobParameters.class,
            DataSynchronizationJobParametersDeserializer.CustomJobParameters.class );
    }

    @JsonDeserialize
    public static class CustomJobParameters extends DataSynchronizationJobParameters
    {
    }
}
