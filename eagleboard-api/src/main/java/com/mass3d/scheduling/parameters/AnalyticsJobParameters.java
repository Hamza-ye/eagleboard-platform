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
import com.mass3d.scheduling.parameters.jackson.AnalyticsJobParametersDeserializer;

@JacksonXmlRootElement( localName = "jobParameters", namespace = DxfNamespaces.DXF_2_0 )
@JsonDeserialize( using = AnalyticsJobParametersDeserializer.class )
public class AnalyticsJobParameters
    implements JobParameters
{
    private static final long serialVersionUID = 4613054056442242637L;

    private Integer lastYears;

    private Set<AnalyticsTableType> skipTableTypes = new HashSet<>();

    private boolean skipResourceTables = false;

    public AnalyticsJobParameters()
    {
    }

    public AnalyticsJobParameters( Integer lastYears, Set<AnalyticsTableType> skipTableTypes, boolean skipResourceTables )
    {
        this.lastYears = lastYears;
        this.skipTableTypes = skipTableTypes;
        this.skipResourceTables = skipResourceTables;
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

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isSkipResourceTables()
    {
        return skipResourceTables;
    }

    public void setSkipResourceTables( boolean skipResourceTables )
    {
        this.skipResourceTables = skipResourceTables;
    }

    @Override
    public Optional<ErrorReport> validate()
    {
        return Optional.empty();
    }
}
