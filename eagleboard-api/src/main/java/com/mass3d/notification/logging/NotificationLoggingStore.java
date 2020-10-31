package com.mass3d.notification.logging;

import com.mass3d.common.IdentifiableObjectStore;

public interface NotificationLoggingStore
    extends IdentifiableObjectStore<ExternalNotificationLogEntry>
{
    ExternalNotificationLogEntry getByTemplateUid(String templateUid);

    ExternalNotificationLogEntry getByKey(String key);
}
