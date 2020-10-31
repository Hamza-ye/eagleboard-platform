package com.mass3d.program;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.event.EventStatus;
import com.mass3d.program.notification.ProgramNotificationTemplate;
import com.mass3d.trackedentity.TrackedEntityInstance;

/**
 * @version $Id$
 */
public interface ProgramStageInstanceStore
    extends IdentifiableObjectStore<ProgramStageInstance>
{
    String ID = ProgramStageInstanceStore.class.getName();

    /**
     * Retrieve an event on a program instance and a program stage. For
     * repeatable stage, the system returns the last event
     *
     * @param programInstance ProgramInstance
     * @param programStage    ProgramStage
     * @return ProgramStageInstance
     */
    ProgramStageInstance get(ProgramInstance programInstance, ProgramStage programStage);

    /**
     * Retrieve an event list on program instance list with a certain status
     *
     * @param programInstances ProgramInstance list
     * @param status           EventStatus
     * @return ProgramStageInstance list
     */
    List<ProgramStageInstance> get(Collection<ProgramInstance> programInstances, EventStatus status);

    /**
     * Get all events by TrackedEntityInstance, optionally filtering by completed.
     *
     * @param entityInstance TrackedEntityInstance
     * @param status         EventStatus
     * @return ProgramStageInstance list
     */
    List<ProgramStageInstance> get(TrackedEntityInstance entityInstance, EventStatus status);

    /**
     * Get the number of ProgramStageInstances updates since the given Date.
     *
     * @param time the time.
     * @return the number of ProgramStageInstances.
     */
    long getProgramStageInstanceCountLastUpdatedAfter(Date time);

    /**
     * Checks for the existence of a PSI by UID. The deleted PSIs are not taken into account.
     *
     * @param uid PSI UID to check for
     * @return true/false depending on result
     */
    boolean exists(String uid);

    /**
     * Checks for the existence of a PSI by UID. It takes into account also the deleted PSIs.
     *
     * @param uid PSI UID to check for
     * @return true/false depending on result
     */
    boolean existsIncludingDeleted(String uid);

    /**
     * Returns UIDs of existing ProgramStageInstances (including deleted) from the provided UIDs
     *
     * @param uids PSI UIDs to check
     * @return Set containing UIDs of existing PSIs (including deleted)
     */
    List<String> getUidsIncludingDeleted(List<String> uids);

    /**
     * Get all ProgramStageInstances which have notifications with the given ProgramNotificationTemplate scheduled on the given date.
     *
     * @param template         the template.
     * @param notificationDate the Date for which the notification is scheduled.
     * @return a list of ProgramStageInstance.
     */
    List<ProgramStageInstance> getWithScheduledNotifications(ProgramNotificationTemplate template,
        Date notificationDate);

    /**
     * Set lastSynchronized timestamp to provided timestamp for provided PSIs
     *
     * @param programStageInstanceUIDs UIDs of ProgramStageInstances where the lastSynchronized flag should be updated
     * @param lastSynchronized         The date of last successful sync
     */
    void updateProgramStageInstancesSyncTimestamp(List<String> programStageInstanceUIDs,
        Date lastSynchronized);
}
