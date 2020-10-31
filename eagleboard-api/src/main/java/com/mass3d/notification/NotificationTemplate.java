package com.mass3d.notification;

import java.util.Set;
import com.mass3d.common.DeliveryChannel;

public interface NotificationTemplate
{
    String getSubjectTemplate();

    String getMessageTemplate();

    Set<DeliveryChannel> getDeliveryChannels();

    Boolean getNotifyUsersInHierarchyOnly();

    Boolean getNotifyParentOrganisationUnitOnly();
}
