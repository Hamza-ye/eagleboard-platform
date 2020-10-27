package com.mass3d.dxf2.scheduling;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.Date;
import com.mass3d.dxf2.webmessage.WebMessageResponse;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.scheduling.JobParameters;
import com.mass3d.scheduling.JobStatus;
import com.mass3d.scheduling.JobType;

public class JobConfigurationWebMessageResponse
    implements WebMessageResponse
{
    private String name;

    private String id;

    private Date created;

    private JobType jobType;

    private JobStatus jobStatus;

    private JobParameters jobParameters;

    private String relativeNotifierEndpoint;

    public JobConfigurationWebMessageResponse( JobConfiguration jobConfiguration )
    {
        this.name = jobConfiguration.getDisplayName();
        this.id = jobConfiguration.getUid();
        this.created = jobConfiguration.getCreated();
        this.jobType = jobConfiguration.getJobType();
        this.jobStatus = jobConfiguration.getJobStatus();
        this.jobParameters = jobConfiguration.getJobParameters();
        this.relativeNotifierEndpoint = "/api/system/tasks/" + this.jobType + "/" + this.id;
    }

    @JacksonXmlProperty
    @JsonProperty
    public String getName()
    {
        return name;
    }

    @JacksonXmlProperty
    @JsonProperty
    public String getId()
    {
        return id;
    }

    @JacksonXmlProperty
    @JsonProperty
    public Date getCreated()
    {
        return created;
    }

    @JacksonXmlProperty
    @JsonProperty
    public JobType getJobType()
    {
        return jobType;
    }

    @JacksonXmlProperty
    @JsonProperty
    public JobStatus getJobStatus()
    {
        return jobStatus;
    }

    @JacksonXmlProperty
    @JsonProperty
    public JobParameters getJobParameters()
    {
        return jobParameters;
    }

    @JacksonXmlProperty
    @JsonProperty
    public String getRelativeNotifierEndpoint()
    {
        return relativeNotifierEndpoint;
    }
}
