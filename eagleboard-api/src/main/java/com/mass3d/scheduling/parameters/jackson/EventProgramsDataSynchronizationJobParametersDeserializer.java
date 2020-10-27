package com.mass3d.scheduling.parameters.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mass3d.scheduling.parameters.EventProgramsDataSynchronizationJobParameters;

public class EventProgramsDataSynchronizationJobParametersDeserializer
    extends AbstractJobParametersDeserializer<EventProgramsDataSynchronizationJobParameters>
{
    public EventProgramsDataSynchronizationJobParametersDeserializer()
    {
        super( EventProgramsDataSynchronizationJobParameters.class,
            CustomJobParameters.class );
    }

    @JsonDeserialize
    private static class CustomJobParameters extends EventProgramsDataSynchronizationJobParameters
    {
    }
}