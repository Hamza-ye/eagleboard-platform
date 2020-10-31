package com.mass3d.program;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.program.notification.ProgramNotificationTemplate;
import com.mass3d.trackedentity.TrackedEntityInstance;

/**
 * @version $Id$
 */
public interface ProgramInstanceStore
    extends IdentifiableObjectStore<ProgramInstance>
{
    String ID = ProgramInstanceStore.class.getName();

    /**
     * Count all program instances by PI query params.
     *
     * @param params ProgramInstanceQueryParams to use
     * @return Count of matching PIs
     */
    int countProgramInstances(ProgramInstanceQueryParams params);

    /**
     * Get all program instances by PI query params.
     *
     * @param params ProgramInstanceQueryParams to use
     * @return PIs matching params
     */
    List<ProgramInstance> getProgramInstances(ProgramInstanceQueryParams params);

    /**
     * Retrieve program instances on a program
     *
     * @param program Program
     * @return ProgramInstance list
     */
    List<ProgramInstance> get(Program program);

    /**
     * Retrieve program instances on a program by status
     *
     * @param program Program
     * @param status  Status of program-instance, include STATUS_ACTIVE,
     *                STATUS_COMPLETED and STATUS_CANCELLED
     * @return ProgramInstance list
     */
    List<ProgramInstance> get(Program program, ProgramStatus status);

    /**
     * Retrieve program instances on a TrackedEntityInstance with a status by a program
     *
     * @param entityInstance TrackedEntityInstance
     * @param program        Program
     * @param status         Status of program-instance, include STATUS_ACTIVE,
     *                       STATUS_COMPLETED and STATUS_CANCELLED
     * @return ProgramInstance list
     */
    List<ProgramInstance> get(TrackedEntityInstance entityInstance, Program program,
        ProgramStatus status);

    /**
     * Checks for the existence of a PI by UID, Deleted PIs are not taken into account.
     *
     * @param uid PSI UID to check for
     * @return true/false depending on result
     */
    boolean exists(String uid);

    /**
     * Checks for the existence of a PI by UID. Takes into account also the deleted PIs.
     *
     * @param uid PSI UID to check for
     * @return true/false depending on result
     */
    boolean existsIncludingDeleted(String uid);

    /**
     * Returns UIDs of existing ProgramInstances (including deleted) from the provided UIDs
     *
     * @param uids PSI UIDs to check
     * @return Set containing UIDs of existing PSIs (including deleted)
     */
    List<String> getUidsIncludingDeleted(List<String> uids);

    /**
     * Get all ProgramInstances which have notifications with the given ProgramNotificationTemplate scheduled on the given date.
     *
     * @param template         the template.
     * @param notificationDate the Date for which the notification is scheduled.
     * @return a list of ProgramInstance.
     */
    List<ProgramInstance> getWithScheduledNotifications(ProgramNotificationTemplate template,
        Date notificationDate);

    /**
     * Return all program instance by type.
     * <p>
     * Warning: this is meant to be used for WITHOUT_REGISTRATION programs only, be careful if you need it for other uses.
     *
     * @param type ProgramType to fetch by
     * @return List of all PIs that matches the wanted type
     */
    List<ProgramInstance> getByType(ProgramType type);

    /**
     * Hard deletes a {@link ProgramInstance}.
     *
     * @param programInstance the ProgramInstance to delete.
     */
    void hardDelete(ProgramInstance programInstance);

    /**
     * Executes a query to fetch all {@see ProgramInstance} matching the
     * Program/TrackedEntityInstance list.
     *
     * Resulting SQL query:
     *
     * <pre>{@code
     *  select programinstanceid, programid, trackedentityinstanceid
     *      from programinstance
     *      where (programid = 726 and trackedentityinstanceid = 19 and status = 'ACTIVE')
     *         or (programid = 726 and trackedentityinstanceid = 18 and status = 'ACTIVE')
     *         or (programid = 726 and trackedentityinstanceid = 17 and status = 'ACTIVE')
     * }</pre>
     *
     * @param programTeiPair a List of Pair, where the left side is a {@see Program}
     *        and the right side is a {@see TrackedEntityInstance}
     * @param programStatus filter on the status of all the Program
     * @return a List of {@see ProgramInstance}
     */
    List<ProgramInstance> getByProgramAndTrackedEntityInstance(
        List<Pair<Program, TrackedEntityInstance>> programTeiPair, ProgramStatus programStatus);
}
