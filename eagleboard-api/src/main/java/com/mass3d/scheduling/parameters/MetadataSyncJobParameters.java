package com.mass3d.scheduling.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Optional;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.scheduling.JobParameters;
import com.mass3d.scheduling.parameters.jackson.MetadataSyncJobParametersDeserializer;

@JacksonXmlRootElement( localName = "jobParameters", namespace = DxfNamespaces.DXF_2_0 )
@JsonDeserialize( using = MetadataSyncJobParametersDeserializer.class )
public class MetadataSyncJobParameters
    implements JobParameters
{
    private static final long serialVersionUID = 332495511301532169L;

    private int trackerProgramPageSize = 20;
    private int eventProgramPageSize = 60;
    private int dataValuesPageSize = 10000;

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getTrackerProgramPageSize()
    {
        return trackerProgramPageSize;
    }

    public void setTrackerProgramPageSize( final int trackerProgramPageSize )
    {
        this.trackerProgramPageSize = trackerProgramPageSize;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getEventProgramPageSize()
    {
        return eventProgramPageSize;
    }

    public void setEventProgramPageSize( final int eventProgramPageSize )
    {
        this.eventProgramPageSize = eventProgramPageSize;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getDataValuesPageSize()
    {
        return dataValuesPageSize;
    }

    public void setDataValuesPageSize( final int dataValuesPageSize )
    {
        this.dataValuesPageSize = dataValuesPageSize;
    }

    @Override
    public Optional<ErrorReport> validate()
    {
        if ( trackerProgramPageSize < TrackerProgramsDataSynchronizationJobParameters.PAGE_SIZE_MIN ||
            trackerProgramPageSize > TrackerProgramsDataSynchronizationJobParameters.PAGE_SIZE_MAX )
        {
            return Optional.of(
                new ErrorReport(
                    this.getClass(),
                    ErrorCode.E4008,
                    "trackerProgramPageSize",
                    TrackerProgramsDataSynchronizationJobParameters.PAGE_SIZE_MIN,
                    TrackerProgramsDataSynchronizationJobParameters.PAGE_SIZE_MAX,
                    trackerProgramPageSize )
            );
        }

        if ( eventProgramPageSize < EventProgramsDataSynchronizationJobParameters.PAGE_SIZE_MIN ||
            eventProgramPageSize > EventProgramsDataSynchronizationJobParameters.PAGE_SIZE_MAX )
        {
            return Optional.of(
                new ErrorReport(
                    this.getClass(),
                    ErrorCode.E4008,
                    "eventProgramPageSize",
                    EventProgramsDataSynchronizationJobParameters.PAGE_SIZE_MIN,
                    EventProgramsDataSynchronizationJobParameters.PAGE_SIZE_MAX,
                    eventProgramPageSize )
            );
        }

        if ( dataValuesPageSize < DataSynchronizationJobParameters.PAGE_SIZE_MIN ||
            dataValuesPageSize > DataSynchronizationJobParameters.PAGE_SIZE_MAX )
        {
            return Optional.of(
                new ErrorReport(
                    this.getClass(),
                    ErrorCode.E4008,
                    "dataValuesPageSize",
                    DataSynchronizationJobParameters.PAGE_SIZE_MIN,
                    DataSynchronizationJobParameters.PAGE_SIZE_MAX,
                    dataValuesPageSize )
            );
        }

        return Optional.empty();
    }
}
