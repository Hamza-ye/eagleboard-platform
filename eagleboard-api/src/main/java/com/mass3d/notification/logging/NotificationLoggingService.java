package com.mass3d.notification.logging;

import java.util.List;

public interface NotificationLoggingService
{
    /***
     *
     * @param uid of the log entry
     * @return log entry if exists otherwise null.
     */
    ExternalNotificationLogEntry get(String uid);

    /**
     *
     * @param templateUid is the uid for the notification template which this log entry is associated to.
     * @return log entry if exists otherwise null.
     */
    ExternalNotificationLogEntry getByTemplateUid(String templateUid);

    /**
     *
     * @param id of the log entry
     * @return log entry if exists otherwise null.
     */
    ExternalNotificationLogEntry get(int id);

    /**
     *
     * @param key unique identifier for the log entry.
     * @return log entry if exists otherwise null.
     */

    ExternalNotificationLogEntry getByKey(String key);

    /**
     * Get all log entries.
     *
     * @return A list containing all notification log entries.
     */
    List<ExternalNotificationLogEntry> getAllLogEntries();

    /**
     *
     * @param templateUid Uid of the template which needs to be sent.
     * @return true in case there is no log entry for this template uid or template is eligible for sending more then once. Otherwise false.
     */
    boolean isValidForSending(String templateUid);

    /**
     *
     * @param entry to be saved.
     */
    void save(ExternalNotificationLogEntry entry);

    /**
     *
     * @param entry to be updated.
     */
    void update(ExternalNotificationLogEntry entry);
}
