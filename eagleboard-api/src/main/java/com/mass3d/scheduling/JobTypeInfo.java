package com.mass3d.scheduling;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.schema.Property;

/**
 * Class which represents information about a job type.
 *
 */
public class JobTypeInfo
{
    private String name;

    private JobType jobType;

    private String key;

    private SchedulingType schedulingType;

    private List<Property> jobParameters = new ArrayList<>();

    /**
     * Default constructor.
     */
    public JobTypeInfo()
    {
    }

    /**
     * Constructor.
     *
     * @param name the job type name.
     * @param jobType the {@link JobType}.
     * @param jobParameters the list of {@link Property}.
     */
    public JobTypeInfo( String name, JobType jobType, List<Property> jobParameters )
    {
        this.name = name;
        this.jobType = jobType;
        this.key = jobType.getKey();
        this.schedulingType = jobType.getSchedulingType();
        this.jobParameters = jobParameters;
    }

    @JsonProperty
    public String getName()
    {
        return name;
    }

    @JsonProperty
    public JobType getJobType()
    {
        return jobType;
    }

    @JsonProperty
    public String getKey()
    {
        return key;
    }

    @JsonProperty
    public SchedulingType getSchedulingType()
    {
        return schedulingType;
    }

    @JsonProperty
    public List<Property> getJobParameters()
    {
        return jobParameters;
    }
}
