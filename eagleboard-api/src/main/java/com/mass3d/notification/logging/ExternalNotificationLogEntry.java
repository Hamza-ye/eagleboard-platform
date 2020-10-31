package com.mass3d.notification.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Date;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "externalNotificationLogEntry", namespace = DxfNamespaces.DXF_2_0 )
public class ExternalNotificationLogEntry
    extends BaseIdentifiableObject
{
    private Date lastSentAt;

    private int retries;

    private String key;

    private String notificationTemplateUid;

    private boolean allowMultiple;

    private NotificationTriggerEvent notificationTriggeredBy;

    public ExternalNotificationLogEntry()
    {
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getKey()
    {
        return key;
    }

    public void setKey( String key )
    {
        this.key = key;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getLastSentAt()
    {
        return lastSentAt;
    }

    public void setLastSentAt( Date lastSentAt )
    {
        this.lastSentAt = lastSentAt;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getRetries()
    {
        return retries;
    }

    public void setRetries( int retries )
    {
        this.retries = retries;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public NotificationTriggerEvent getNotificationTriggeredBy()
    {
        return notificationTriggeredBy;
    }

    public void setNotificationTriggeredBy( NotificationTriggerEvent notificationTriggeredBy )
    {
        this.notificationTriggeredBy = notificationTriggeredBy;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isAllowMultiple()
    {
        return allowMultiple;
    }

    public void setAllowMultiple( boolean allowMultiple )
    {
        this.allowMultiple = allowMultiple;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getNotificationTemplateUid()
    {
        return notificationTemplateUid;
    }

    public void setNotificationTemplateUid( String notificationTemplateUid )
    {
        this.notificationTemplateUid = notificationTemplateUid;
    }

    @Override
    public void setAutoFields()
    {
        super.setAutoFields();
    }
}
