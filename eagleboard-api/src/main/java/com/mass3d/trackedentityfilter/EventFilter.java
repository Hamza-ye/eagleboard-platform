package com.mass3d.trackedentityfilter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.common.AssignedUserSelectionMode;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.event.EventStatus;

public class EventFilter implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String programStage;

    private EventStatus eventStatus;

    private FilterPeriod eventCreatedPeriod;
    
    private AssignedUserSelectionMode assignedUserMode;
    
    private Set<String> assignedUsers = new HashSet<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public EventFilter()
    {

    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getProgramStage()
    {
        return programStage;
    }

    public void setProgramStage( String programStage )
    {
        this.programStage = programStage;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public EventStatus getEventStatus()
    {
        return eventStatus;
    }

    public void setEventStatus( EventStatus eventStatus )
    {
        this.eventStatus = eventStatus;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public FilterPeriod getEventCreatedPeriod()
    {
        return eventCreatedPeriod;
    }

    public void setEventCreatedPeriod( FilterPeriod eventCreatedPeriod )
    {
        this.eventCreatedPeriod = eventCreatedPeriod;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public AssignedUserSelectionMode getAssignedUserMode()
    {
        return assignedUserMode;
    }

    public void setAssignedUserMode( AssignedUserSelectionMode assignedUserMode )
    {
        this.assignedUserMode = assignedUserMode;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Set<String> getAssignedUsers()
    {
        return assignedUsers;
    }

    public void setAssignedUsers( Set<String> assignedUsers )
    {
        this.assignedUsers = assignedUsers;
    }
    
    

}
