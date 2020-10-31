package com.mass3d.trackedentity;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.user.User;

public interface TrackedEntityInstanceStore
    extends IdentifiableObjectStore<TrackedEntityInstance>
{
    String ID = TrackedEntityInstanceStore.class.getName();

    int countTrackedEntityInstances(TrackedEntityInstanceQueryParams params);

    List<TrackedEntityInstance> getTrackedEntityInstances(TrackedEntityInstanceQueryParams params);

    List<Long> getTrackedEntityInstanceIds(TrackedEntityInstanceQueryParams params);

    List<Map<String, String>> getTrackedEntityInstancesGrid(TrackedEntityInstanceQueryParams params);

    int getTrackedEntityInstanceCountForGrid(TrackedEntityInstanceQueryParams params);

    /**
     * Checks for the existence of a TEI by UID. Deleted TEIs are not taken into account.
     *
     * @param uid PSI UID to check for.
     * @return true/false depending on result.
     */
    boolean exists(String uid);

    /**
     * Checks for the existence of a TEI by UID. Takes into account also the deleted TEIs.
     *
     * @param uid PSI UID to check for.
     * @return true/false depending on result.
     */
    boolean existsIncludingDeleted(String uid);

    /**
     * Returns UIDs of existing TrackedEntityInstances (including deleted) from the provided UIDs
     *
     * @param uids TEI UIDs to check
     * @return Set containing UIDs of existing TEIs (including deleted)
     */
    List<String> getUidsIncludingDeleted(List<String> uids);

    /**
     * Set lastSynchronized timestamp to provided timestamp for provided TEIs
     *
     * @param trackedEntityInstanceUIDs UIDs of Tracked entity instances where the lastSynchronized flag should be updated
     * @param lastSynchronized          The date of last successful sync
     */
    void updateTrackedEntityInstancesSyncTimestamp(List<String> trackedEntityInstanceUIDs,
        Date lastSynchronized);

    List<TrackedEntityInstance> getTrackedEntityInstancesByUid(List<String> uids, User user);
}
