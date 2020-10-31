package com.mass3d.trackedentity;

import java.util.List;
import com.mass3d.common.GenericStore;

public interface TrackedEntityProgramOwnerStore extends GenericStore<TrackedEntityProgramOwner>
{
    String ID = TrackedEntityProgramOwnerStore.class.getName();

    /**
     * Get tracked entity program owner entity for the tei-program combination.
     * 
     * @param teiId The tracked entity instance id.
     * @param programId the program id
     * @return matching tracked entity program owner entity
     */
    TrackedEntityProgramOwner getTrackedEntityProgramOwner(long teiId, long programId);

    /**
     * Get all Tracked entity program owner entities for the list of teis.
     *
     * @param teiIds The list of tracked entity instance ids.
     * @return matching tracked entity program owner entities.
     */
    List<TrackedEntityProgramOwner> getTrackedEntityProgramOwners(List<Long> teiIds);

    /**
     *  Get all Tracked entity program owner entities for the list of teis and program.
     *
     * @param teiIds The list of tracked entity instance ids.
     * @param programId The program id
     * @return matching tracked entity program owner entities.
     */
    List<TrackedEntityProgramOwner> getTrackedEntityProgramOwners(List<Long> teiIds, long programId);
}
