package com.mass3d.program.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import java.util.Set;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DeliveryChannel;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.dataelement.DataElement;
import com.mass3d.notification.NotificationTemplate;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.annotation.Property;
import com.mass3d.schema.annotation.PropertyRange;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.user.UserGroup;

@JacksonXmlRootElement( namespace = DxfNamespaces.DXF_2_0 )
public class ProgramNotificationTemplate
    extends BaseIdentifiableObject implements NotificationTemplate, MetadataObject
{
    private String subjectTemplate;

    private String messageTemplate;

    private NotificationTrigger notificationTrigger = NotificationTrigger.COMPLETION;

    private ProgramNotificationRecipient notificationRecipient = ProgramNotificationRecipient.USER_GROUP;

    private Set<DeliveryChannel> deliveryChannels = Sets.newHashSet();

    private Boolean notifyUsersInHierarchyOnly;

    private Boolean notifyParentOrganisationUnitOnly;

    // -------------------------------------------------------------------------
    // Conditionally relevant properties
    // -------------------------------------------------------------------------

    private Integer relativeScheduledDays = null;

    private UserGroup recipientUserGroup = null;

    private TrackedEntityAttribute recipientProgramAttribute = null;

    private DataElement recipientDataElement = null;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramNotificationTemplate()
    {
    }

    public ProgramNotificationTemplate( String name, String subjectTemplate, String messageTemplate,
        NotificationTrigger notificationTrigger, ProgramNotificationRecipient notificationRecipient,
        Set<DeliveryChannel> deliveryChannels, Integer relativeScheduledDays, UserGroup recipientUserGroup,
        TrackedEntityAttribute recipientProgramAttribute )
    {
        this.name = name;
        this.subjectTemplate = subjectTemplate;
        this.messageTemplate = messageTemplate;
        this.notificationTrigger = notificationTrigger;
        this.notificationRecipient = notificationRecipient;
        this.deliveryChannels = deliveryChannels;
        this.relativeScheduledDays = relativeScheduledDays;
        this.recipientUserGroup = recipientUserGroup;
        this.recipientProgramAttribute = recipientProgramAttribute;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getSubjectTemplate()
    {
        return subjectTemplate;
    }

    public void setSubjectTemplate( String subjectTemplate )
    {
        this.subjectTemplate = subjectTemplate;
    }

    @PropertyRange( min = 1, max = 10000 )
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getMessageTemplate()
    {
        return messageTemplate;
    }

    public void setMessageTemplate( String messageTemplate )
    {
        this.messageTemplate = messageTemplate;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public NotificationTrigger getNotificationTrigger()
    {
        return notificationTrigger;
    }

    public void setNotificationTrigger( NotificationTrigger notificationTrigger )
    {
        this.notificationTrigger = notificationTrigger;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ProgramNotificationRecipient getNotificationRecipient()
    {
        return notificationRecipient;
    }

    public void setNotificationRecipient( ProgramNotificationRecipient notificationRecipient )
    {
        this.notificationRecipient = notificationRecipient;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Set<DeliveryChannel> getDeliveryChannels()
    {
        return deliveryChannels;
    }

    public void setDeliveryChannels( Set<DeliveryChannel> deliveryChannels )
    {
        this.deliveryChannels = deliveryChannels;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @Property( value = PropertyType.INTEGER )
    @PropertyRange( min = Integer.MIN_VALUE, max = Integer.MAX_VALUE )
    public Integer getRelativeScheduledDays()
    {
        return relativeScheduledDays;
    }

    public void setRelativeScheduledDays( Integer relativeScheduledDays )
    {
        this.relativeScheduledDays = relativeScheduledDays;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public UserGroup getRecipientUserGroup()
    {
        return recipientUserGroup;
    }

    public void setRecipientUserGroup( UserGroup recipientUserGroup )
    {
        this.recipientUserGroup = recipientUserGroup;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public TrackedEntityAttribute getRecipientProgramAttribute()
    {
        return recipientProgramAttribute;
    }

    public void setRecipientProgramAttribute( TrackedEntityAttribute recipientProgramAttribute )
    {
        this.recipientProgramAttribute = recipientProgramAttribute;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public DataElement getRecipientDataElement()
    {
        return recipientDataElement;
    }

    public void setRecipientDataElement( DataElement dataElement )
    {
        this.recipientDataElement = dataElement;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean getNotifyUsersInHierarchyOnly()
    {
        return notifyUsersInHierarchyOnly;
    }

    public void setNotifyUsersInHierarchyOnly( Boolean notifyUsersInHierarchyOnly )
    {
        this.notifyUsersInHierarchyOnly = notifyUsersInHierarchyOnly;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean getNotifyParentOrganisationUnitOnly()
    {
        return notifyParentOrganisationUnitOnly;
    }

    public void setNotifyParentOrganisationUnitOnly( Boolean notifyParentOrganisationUnitOnly )
    {
        this.notifyParentOrganisationUnitOnly = notifyParentOrganisationUnitOnly;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "uid", uid )
            .add( "name", name )
            .add( "notificationTrigger", notificationTrigger )
            .add( "notificationRecipient", notificationRecipient )
            .add( "deliveryChannels", deliveryChannels )
            .add( "messageTemplate", messageTemplate )
            .add( "subjectTemplate", subjectTemplate )
            .toString();
    }
}
