package com.mass3d.webapi.webdomain;

import java.util.ArrayList;
import java.util.List;

import com.mass3d.scheduling.JobTypeInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wrapper DTO class for a list of {@link JobTypeInfo}.
 *
 */
public class JobTypes
{
    private List<JobTypeInfo> jobTypes = new ArrayList<>();

    public JobTypes()
    {
    }

    public JobTypes( List<JobTypeInfo> jobTypes )
    {
        this.jobTypes = jobTypes;
    }

    @JsonProperty
    public List<JobTypeInfo> getJobTypes()
    {
        return jobTypes;
    }

    public void setJobTypes( List<JobTypeInfo> jobTypes )
    {
        this.jobTypes = jobTypes;
    }
}
