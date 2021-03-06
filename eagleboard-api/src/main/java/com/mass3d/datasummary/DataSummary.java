package com.mass3d.datasummary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * DataSummary object to transfer System Statistics
 *
 */
@JacksonXmlRootElement
public class DataSummary
{
    private Map<String, Integer> objectCounts = new HashMap<>();

    private Map<Integer, Integer> activeUsers = new HashMap<>();

    private Map<String, Integer> userInvitations = new HashMap<>();

    private Map<Integer, Integer> dataValueCount = new HashMap<>();

    private Map<Integer, Long> eventCount = new HashMap<>();

    public DataSummary()
    {
    }

    public DataSummary( Map<String, Integer> objectCounts,
        Map<Integer, Integer> activeUsers, Map<String, Integer> userInvitations,
        Map<Integer, Integer> dataValueCount, Map<Integer, Long> eventCount )
    {
        this.objectCounts = objectCounts;
        this.activeUsers = activeUsers;
        this.userInvitations = userInvitations;
        this.dataValueCount = dataValueCount;
        this.eventCount = eventCount;
    }

    @JsonProperty
    public Map<String, Integer> getObjectCounts()
    {
        return objectCounts;
    }

    public void setObjectCounts( Map<String, Integer> objectCounts )
    {
        this.objectCounts = objectCounts;
    }

    @JsonProperty
    public Map<Integer, Integer> getActiveUsers()
    {
        return activeUsers;
    }

    public void setActiveUsers( Map<Integer, Integer> activeUsers )
    {
        this.activeUsers = activeUsers;
    }

    @JsonProperty
    public Map<String, Integer> getUserInvitations()
    {
        return userInvitations;
    }

    public void setUserInvitations( Map<String, Integer> userInvitations )
    {
        this.userInvitations = userInvitations;
    }

    @JsonProperty
    public Map<Integer, Integer> getDataValueCount()
    {
        return dataValueCount;
    }

    public void setDataValueCount( Map<Integer, Integer> dataValueCount )
    {
        this.dataValueCount = dataValueCount;
    }

    @JsonProperty
    public Map<Integer, Long> getEventCount()
    {
        return eventCount;
    }

    @JsonProperty
    public void setEventCount( Map<Integer, Long> eventCount )
    {
        this.eventCount = eventCount;
    }
}
