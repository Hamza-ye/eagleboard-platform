package com.mass3d.scheduling.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import com.mass3d.analytics.AnalyticsTableType;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.scheduling.JobParameters;
import com.mass3d.scheduling.parameters.jackson.ContinuousAnalyticsJobParametersDeserializer;

@JacksonXmlRootElement( localName = "jobParameters", namespace = DxfNamespaces.DXF_2_0 )
@JsonDeserialize( using = ContinuousAnalyticsJobParametersDeserializer.class )
public class ContinuousAnalyticsJobParameters
    implements JobParameters
{
    private static final long serialVersionUID = 4613054056442276592L;

    /**
     * The hour of day at which the full analytics table update will be invoked.
     */
    private Integer fullUpdateHourOfDay = 0;

    /**
     * The number of last years of data to include in the full analytics table update.
     */
    private Integer lastYears;

    /**
     * The types of analytics tables for which to skip update.
     */
    private Set<AnalyticsTableType> skipTableTypes = new HashSet<>();

    public ContinuousAnalyticsJobParameters()
    {
    }

    public ContinuousAnalyticsJobParameters( Integer fullUpdateHourOfDay, Integer lastYears, Set<AnalyticsTableType> skipTableTypes )
    {
        this.fullUpdateHourOfDay = fullUpdateHourOfDay;
        this.lastYears = lastYears;
        this.skipTableTypes = skipTableTypes;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Integer getFullUpdateHourOfDay()
    {
        return fullUpdateHourOfDay;
    }

    public void setFullUpdateHourOfDay( Integer fullUpdateHourOfDay )
    {
        this.fullUpdateHourOfDay = fullUpdateHourOfDay;
    }


    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Integer getLastYears()
    {
        return lastYears;
    }

    public void setLastYears( Integer lastYears )
    {
        this.lastYears = lastYears;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "skipTableTypes", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "skipTableType", namespace = DxfNamespaces.DXF_2_0 )
    public Set<AnalyticsTableType> getSkipTableTypes()
    {
        return skipTableTypes;
    }

    public void setSkipTableTypes( Set<AnalyticsTableType> skipTableTypes )
    {
        this.skipTableTypes = skipTableTypes;
    }

    @Override
    public Optional<ErrorReport> validate()
    {
        return Optional.empty();
    }
}
