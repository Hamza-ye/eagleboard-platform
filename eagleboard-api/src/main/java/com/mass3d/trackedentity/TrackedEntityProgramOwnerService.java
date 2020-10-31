package com.mass3d.trackedentity;

import java.util.List;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.program.Program;

public interface TrackedEntityProgramOwnerService
{
    String ID = TrackedEntityProgramOwnerService.class.getName();

    /**
     * Assign an orgUnit as the owner for a tracked entity instance for the
     * given program. If another owner already exist then this method would
     * fail.
     * 
     * @param teiUid The Uid of the tracked entity instance
     * @param programUid The program Uid
     * @param orgUnitUid The organisation units Uid
     */
    void createTrackedEntityProgramOwner(String teiUid, String programUid, String orgUnitUid);

    /**
     * Update the owner ou for a tracked entity instance for the given program.
     * If no owner previously exist, then this method will fail.
     *
     * @param teiUid The tracked entity instance Uid
     * @param programUid The program Uid
     * @param orgUnitUid The organisation Unit Uid
     */
    void updateTrackedEntityProgramOwner(String teiUid, String programUid, String orgUnitUid);

    /**
     * Assign an orgUnit as the owner for a tracked entity instance for the
     * given program. If another owner already exist then this method would
     * fail.
     *
     * @param teiId The Id of the tracked entity instance
     * @param programId The program Id
     * @param orgUnitId The organisation units Id
     */
    void createTrackedEntityProgramOwner(long teiId, long programId, long orgUnitId);

    /**
     * Update the owner ou for a tracked entity instance for the given program.
     * If no owner previously exist, then this method will fail.
     *
     * @param teiId The tracked entity instance Id
     * @param programId The program Id
     * @param orgUnitId The organisation Unit Id
     */
    void updateTrackedEntityProgramOwner(long teiId, long programId, long orgUnitId);

    /**
     * Get the program owner details for a tracked entity instance.
     *
     * @param teiId The tracked entity instance Id
     * @param programId The program Id
     * @return The TrackedEntityProgramOwner object
     */
    TrackedEntityProgramOwner getTrackedEntityProgramOwner(long teiId, long programId);

    /**
     * Get the program owner details for a tracked entity instance.
     *
     * @param teiUid The tracked entity instance Uid
     * @param programUid The program Uid
     * @return The TrackedEntityProgramOwner object
     */
    TrackedEntityProgramOwner getTrackedEntityProgramOwner(String teiUid, String programUid);

    /**
     * Get the program owner details for a list of teiIds. Includes all possible program
     *
     * @param teiIds The list of tei Ids
     * @return The  list of TrackedEntityProgramOwner details
     */
    List<TrackedEntityProgramOwner> getTrackedEntityProgramOwnersUsingId(List<Long> teiIds);

    /**
     * Get the program owner details for a list of teiIds for a specific program
     *
     * @param teiIds The list of tei Ids
     * @param program The program
     * @return The  list of TrackedEntityProgramOwner details
     */
    List<TrackedEntityProgramOwner> getTrackedEntityProgramOwnersUsingId(List<Long> teiIds,
        Program program);

    /**
     * Assign an orgUnit as the owner for a tracked entity instance for the
     * given program. If another owner already exist then it would be overwritten.
     *
     * @param teiUid
     * @param programUid
     * @param orgUnitUid
     */
    void createOrUpdateTrackedEntityProgramOwner(String teiUid, String programUid,
        String orgUnitUid);

    /**
     * Assign an orgUnit as the owner for a tracked entity instance for the
     * given program. If another owner already exist then it would be overwritten.
     *
     * @param teiUid
     * @param programUid
     * @param orgUnitUid
     */
    void createOrUpdateTrackedEntityProgramOwner(long teiUid, long programUid, long orgUnitUid);

    /**
     * Assign an orgUnit as the owner for a tracked entity instance for the
     * given program. If another owner already exist then it would be overwritten.
     *
     * @param entityInstance
     * @param program
     * @param ou
     */
    void createOrUpdateTrackedEntityProgramOwner(TrackedEntityInstance entityInstance,
        Program program,
        OrganisationUnit ou);

    /**
     * Update the owner ou for a tracked entity instance for the given program.
     * If no owner previously exist, then this method will fail.
     *
     * @param entityInstance
     * @param program
     * @param ou
     */
    void updateTrackedEntityProgramOwner(TrackedEntityInstance entityInstance, Program program,
        OrganisationUnit ou);

    /**
     * Create a new program owner ou for a tracked entity instance.
     * If an owner previously exist, then this method will fail.
     *
     * @param entityInstance
     * @param program
     * @param ou
     */
    void createTrackedEntityProgramOwner(TrackedEntityInstance entityInstance, Program program,
        OrganisationUnit ou);

}
