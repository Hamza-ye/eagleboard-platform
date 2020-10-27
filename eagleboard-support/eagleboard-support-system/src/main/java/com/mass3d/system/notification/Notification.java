package com.mass3d.system.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Date;
import com.mass3d.common.CodeGenerator;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.scheduling.JobType;

@JacksonXmlRootElement( localName = "notification", namespace = DxfNamespaces.DXF_2_0 )
public class Notification
{
    private String uid; // FIXME expose as "id" externally in next API version as "uid" is internal

    private NotificationLevel level;

    private JobType category;

    private Date time;

    private String message;

    private boolean completed;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Notification()
    {
        this.uid = CodeGenerator.generateUid();
    }

    public Notification( NotificationLevel level, JobType category, Date time, String message, boolean completed )
    {
        this.uid = CodeGenerator.generateUid();
        this.level = level;
        this.category = category;
        this.time = time;
        this.message = message;
        this.completed = completed;
    }

    // -------------------------------------------------------------------------
    // Get and set
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public NotificationLevel getLevel()
    {
        return level;
    }

    public void setLevel( NotificationLevel level )
    {
        this.level = level;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getId() // expose as ID also to be future proof, we should not expose UID fields
    {
        return uid;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getUid()
    {
        return uid;
    }

    public void setUid( String uid )
    {
        this.uid = uid;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public JobType getCategory()
    {
        return category;
    }

    public void setCategory( JobType category )
    {
        this.category = category;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getTime()
    {
        return time;
    }

    public void setTime( Date time )
    {
        this.time = time;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted( boolean completed )
    {
        this.completed = completed;
    }

    // -------------------------------------------------------------------------
    // equals, hashCode, toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return uid.hashCode();
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null )
        {
            return false;
        }

        if ( getClass() != object.getClass() )
        {
            return false;
        }

        final Notification other = (Notification) object;

        return uid.equals( other.uid );
    }

    @Override
    public String toString()
    {
        return "[Level: " + level + ", category: " + category + ", time: " + time + ", message: " + message + "]";
    }
}
