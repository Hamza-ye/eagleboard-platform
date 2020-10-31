package com.mass3d.program.notification;

import java.util.Date;

public interface ProgramNotificationService
{
    /**
     * Send all scheduled notifications for the given day.
     *
     * Queries for any upcoming ProgramStageInstances or ProgramInstances
     * which have a {@link ProgramNotificationTemplate} scheduled for the
     * given day, creates the messages and immediately dispatches them.
     *
     * Potentially a time consuming job, depending on the amount of
     * configured notifications, the amount of recipients, the message
     * types (SMS, email, dhis message) and the amount of events resolved
     * by the query.
     *
     * Due to the time consuming nature of the process this method
     * should be wrapped in an asynchronous task.
     *
     * @param day the Date representing the day relative to the
     *             scheduled notifications for which to send messages.
     */
    void sendScheduledNotificationsForDay(Date day);

    /**
     * Sends all notifications which are scheduled by program rule and having scheduledDate
     * for today.
     */
    void sendScheduledNotifications();

    /**
     * Send completion notifications for the ProgramStageInstance.
     * If the ProgramStage is not configured with suitable
     * {@link ProgramNotificationTemplate templates}, nothing will happen.
     *
     * @param programStageInstance the ProgramStageInstance id.
     */
    void sendEventCompletionNotifications(long programStageInstance);

    /**
     * Send completion notifications for the ProgramInstance triggered by ProgramRule evaluation.
     * {@link ProgramNotificationTemplate templates}, nothing will happen.
     * @param pnt ProgramNotificationTemplate id to send
     * @param programInstance the ProgramInstance id.
     */
    void sendProgramRuleTriggeredNotifications(long pnt, long programInstance);

    /**
     * Send completion notifications for the ProgramStageInstance triggered by ProgramRule evaluation.
     * {@link ProgramNotificationTemplate templates}, nothing will happen.
     * @param pnt ProgramNotificationTemplate id to send
     * @param programStageInstance the ProgramStageInstance id.
     */
    void sendProgramRuleTriggeredEventNotifications(long pnt, long programStageInstance);

    /**
     * Send completion notifications for the ProgramInstance.
     * If the Program is not configured with suitable
     * {@link ProgramNotificationTemplate templates}, nothing will happen.
     *
     * @param programInstance the ProgramInstance id.
     */
    void sendEnrollmentCompletionNotifications(long programInstance);

    /**
     * Send enrollment notifications for the ProgramInstance.
     * If the Program is not configured with suitable
     * {@link ProgramNotificationTemplate templates}, nothing will happen.

     * @param programInstance the ProgramInstance id.
     */
    void sendEnrollmentNotifications(long programInstance);
}
