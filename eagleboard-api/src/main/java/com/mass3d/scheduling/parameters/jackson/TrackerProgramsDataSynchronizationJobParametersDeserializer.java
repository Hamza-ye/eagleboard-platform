package com.mass3d.scheduling.parameters.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mass3d.scheduling.parameters.TrackerProgramsDataSynchronizationJobParameters;

public class TrackerProgramsDataSynchronizationJobParametersDeserializer
    extends AbstractJobParametersDeserializer<TrackerProgramsDataSynchronizationJobParameters>
{
    public TrackerProgramsDataSynchronizationJobParametersDeserializer()
    {
        super( TrackerProgramsDataSynchronizationJobParameters.class,
            CustomJobParameters.class );
    }

    @JsonDeserialize
    public static class CustomJobParameters extends TrackerProgramsDataSynchronizationJobParameters
    {
    }
}
