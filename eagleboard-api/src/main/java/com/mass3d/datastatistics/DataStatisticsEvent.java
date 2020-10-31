package com.mass3d.datastatistics;

import java.util.Date;

/**
 * Object of event to be saved
 *
 */
public class DataStatisticsEvent
{
    private int id;
    private DataStatisticsEventType eventType;
    private Date timestamp;
    private String username;
    private String favoriteUid;

    public DataStatisticsEvent()
    {
    }

    public DataStatisticsEvent( DataStatisticsEventType eventType, Date timestamp, String username )
    {
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.username = username;
    }

    public DataStatisticsEvent( DataStatisticsEventType eventType, Date timestamp, String username, String favoriteUid )
    {
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.username = username;
        this.favoriteUid = favoriteUid;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public DataStatisticsEventType getEventType()
    {
        return eventType;
    }

    public void setEventType( DataStatisticsEventType eventType )
    {
        this.eventType = eventType;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp( Date timestamp )
    {
        this.timestamp = timestamp;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getFavoriteUid()
    {
        return favoriteUid;
    }

    public void setFavoriteUid( String favoriteUid )
    {
        this.favoriteUid = favoriteUid;
    }
}
