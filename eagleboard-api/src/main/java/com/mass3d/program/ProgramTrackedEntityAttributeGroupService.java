package com.mass3d.program;

import java.util.List;

public interface ProgramTrackedEntityAttributeGroupService
{
    /**
     * Adds an {@link ProgramTrackedEntityAttributeGroup}
     * 
     * @param programTrackedEntityAttributeGroup The to ProgramTrackedEntityAttributeGroup
     *         add.
     * 
     * @return A generated unique id of the added {@link ProgramTrackedEntityAttributeGroup}.
     */
    long addProgramTrackedEntityAttributeGroup(
        ProgramTrackedEntityAttributeGroup programTrackedEntityAttributeGroup);

    /**
     * Deletes a {@link ProgramTrackedEntityAttributeGroup}.
     *
     * @param programTrackedEntityAttributeGroup the ProgramTrackedEntityAttributeGroup to
     *        delete.
     */
    void deleteProgramTrackedEntityAttributeGroup(
        ProgramTrackedEntityAttributeGroup programTrackedEntityAttributeGroup);

    /**
     * Updates a {@link ProgramTrackedEntityAttributeGroup}.
     *
     * @param programTrackedEntityAttributeGroup the ProgramTrackedEntityAttributeGroup to
     *        update.
     */
    void updateProgramTrackedEntityAttributeGroup(
        ProgramTrackedEntityAttributeGroup programTrackedEntityAttributeGroup);

    /**
     * Returns a {@link ProgramTrackedEntityAttributeGroup}.
     *
     * @param id the id of the ProgramTrackedEntityAttributeGroup to return.
     *
     * @return the ProgramTrackedEntityAttributeGroup with the given id
     */
    ProgramTrackedEntityAttributeGroup getProgramTrackedEntityAttributeGroup(long id);

    /**
     * Returns a {@link ProgramTrackedEntityAttributeGroup}.
     *
     * @param uid the id of the ProgramTrackedEntityAttributeGroup to return.
     *
     * @return the ProgramTrackedEntityAttributeGroup with the given id
     */
    ProgramTrackedEntityAttributeGroup getProgramTrackedEntityAttributeGroup(String uid);

    /**
     * Returns all {@link ProgramTrackedEntityAttributeGroup}
     * 
     * @return a List of all ProgramTrackedEntityAttributeGroup, or an empty
     *         List if there are no ProgramTrackedEntityAttributeGroups.
     */
    List<ProgramTrackedEntityAttributeGroup> getAllProgramTrackedEntityAttributeGroups();
}
