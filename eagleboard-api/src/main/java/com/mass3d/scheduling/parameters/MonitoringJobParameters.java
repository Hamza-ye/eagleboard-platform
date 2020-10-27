package com.mass3d.scheduling.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.mass3d.common.CodeGenerator;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.scheduling.JobParameters;
import com.mass3d.scheduling.parameters.jackson.MonitoringJobParametersDeserializer;

@JacksonXmlRootElement( localName = "jobParameters", namespace = DxfNamespaces.DXF_2_0 )
@JsonDeserialize( using = MonitoringJobParametersDeserializer.class )
public class MonitoringJobParameters
    implements JobParameters
{
    private static final long serialVersionUID = -1683853240301569669L;

    private int relativeStart;

    private int relativeEnd;

    private List<String> validationRuleGroups = new ArrayList<>();

    private boolean sendNotifications;

    private boolean persistResults;

    public MonitoringJobParameters()
    {
    }

    public MonitoringJobParameters( int relativeStart, int relativeEnd, List<String> validationRuleGroups,
        boolean sendNotifications, boolean persistResults )
    {
        this.relativeStart = relativeStart;
        this.relativeEnd = relativeEnd;
        this.validationRuleGroups = validationRuleGroups != null ? validationRuleGroups : Lists.newArrayList();
        this.sendNotifications = sendNotifications;
        this.persistResults = persistResults;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getRelativeStart()
    {
        return relativeStart;
    }

    public void setRelativeStart( int relativeStart )
    {
        this.relativeStart = relativeStart;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getRelativeEnd()
    {
        return relativeEnd;
    }

    public void setRelativeEnd( int relativeEnd )
    {
        this.relativeEnd = relativeEnd;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "validationRuleGroups", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "validationRuleGroup", namespace = DxfNamespaces.DXF_2_0 )
    public List<String> getValidationRuleGroups()
    {
        return validationRuleGroups;
    }

    public void setValidationRuleGroups( List<String> validationRuleGroups )
    {
        this.validationRuleGroups = validationRuleGroups;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isSendNotifications()
    {
        return sendNotifications;
    }

    public void setSendNotifications( boolean sendNotifications )
    {
        this.sendNotifications = sendNotifications;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isPersistResults()
    {
        return persistResults;
    }

    public void setPersistResults( boolean persistResults )
    {
        this.persistResults = persistResults;
    }

    @Override
    public Optional<ErrorReport> validate()
    {
        // No need to validate relatePeriods, since it will fail in the controller if invalid.

        // Validating validationRuleGroup. Since it's too late to check if the input was an array of strings or
        // something else, this is a best effort to avoid invalid data in the object.
        List<String> invalidUIDs = validationRuleGroups.stream()
            .filter( ( group ) -> !CodeGenerator.isValidUid( group ) )
            .collect( Collectors.toList() );

        if ( invalidUIDs.size() > 0 )
        {
            return Optional.of(  new ErrorReport( this.getClass(), ErrorCode.E4014, invalidUIDs.get( 0 ),
                "validationRuleGroups" ));
        }

        return Optional.empty();
    }

}
