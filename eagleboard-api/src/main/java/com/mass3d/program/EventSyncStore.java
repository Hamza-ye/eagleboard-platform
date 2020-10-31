package com.mass3d.program;

import java.util.List;

public interface EventSyncStore
{
    /**
     * Returns the {@link ProgramStageInstance} with the given UID.
     *
     * @param uid the UID.
     * @return the ProgramStageInstance with the given UID, or null if no
     * match.
     */
    ProgramStageInstance getEvent(String uid);

    /**
     * Returns the {@link ProgramInstance} with the given UID.
     *
     * @param uid the UID.
     * @return the ProgramInstance with the given UID, or null if no
     * match.
     */
    ProgramInstance getEnrollment(String uid);

    /**
     * Get events (including deleted)
     *
     * @param uids UIDs of events to be fetched
     * @return list of events
     */
    List<ProgramStageInstance> getEvents(List<String> uids);
}