package com.mass3d.scheduling.parameters.jackson;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.mass3d.scheduling.JobConfiguration;

/**
 * Cleans the resulting job configuration after de-serializing.
 *
 */
public class JobConfigurationSanitizer extends StdConverter<JobConfiguration, JobConfiguration>
{
    @Override
    public JobConfiguration convert( JobConfiguration value )
    {
        if ( value == null )
        {
            return null;
        }

        final JobConfiguration jobConfiguration = new JobConfiguration( value.getName(), value.getJobType(),
            value.getCronExpression(), value.getJobParameters(), value.isEnabled(), value.isInMemoryJob() );
        jobConfiguration.setDelay( value.getDelay() );
        jobConfiguration.setLeaderOnlyJob( value.isLeaderOnlyJob() );
        jobConfiguration.setUid( value.getUid() );
        return jobConfiguration;
    }
}
