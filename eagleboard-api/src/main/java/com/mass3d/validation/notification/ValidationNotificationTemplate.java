package com.mass3d.validation.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DeliveryChannel;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.notification.NotificationTemplate;
import com.mass3d.notification.SendStrategy;
import com.mass3d.schema.annotation.PropertyRange;
import com.mass3d.user.UserGroup;
import com.mass3d.validation.ValidationRule;

public class ValidationNotificationTemplate
    extends BaseIdentifiableObject
    implements NotificationTemplate, MetadataObject
{
    private static final Set<DeliveryChannel> ALL_DELIVERY_CHANNELS = Sets.newHashSet( DeliveryChannel.values() );

    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private String subjectTemplate;

    private String messageTemplate;

    private Set<ValidationRule> validationRules = new HashSet<>();

    private Boolean notifyUsersInHierarchyOnly;

    private Boolean notifyParentOrganisationUnitOnly;

    private Set<UserGroup> recipientUserGroups = new HashSet<>();

    private SendStrategy sendStrategy = SendStrategy.COLLECTIVE_SUMMARY;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ValidationNotificationTemplate()
    {
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addValidationRule( ValidationRule validationRule )
    {
        this.validationRules.add( validationRule );
        validationRule.getNotificationTemplates().add( this );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @Override
    public String getSubjectTemplate()
    {
        return subjectTemplate;
    }

    public void setSubjectTemplate( String subjectTemplate )
    {
        this.subjectTemplate = subjectTemplate;
    }

    @PropertyRange( min = 1, max = 1000 )
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @Override
    public String getMessageTemplate()
    {
        return messageTemplate;
    }

    @Override
    public Set<DeliveryChannel> getDeliveryChannels()
    {
        return ALL_DELIVERY_CHANNELS;
    }

    public void setMessageTemplate( String messageTemplate )
    {
        this.messageTemplate = messageTemplate;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Set<ValidationRule> getValidationRules()
    {
        return validationRules;
    }

    public void setValidationRules( Set<ValidationRule> validationRules )
    {
        this.validationRules = validationRules;
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

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Set<UserGroup> getRecipientUserGroups()
    {
        return recipientUserGroups;
    }

    public void setRecipientUserGroups( Set<UserGroup> recipientUserGroups )
    {
        this.recipientUserGroups = recipientUserGroups;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public SendStrategy getSendStrategy()
    {
        return sendStrategy;
    }

    public void setSendStrategy( SendStrategy sendStrategy )
    {
        this.sendStrategy = sendStrategy;
    }
}
