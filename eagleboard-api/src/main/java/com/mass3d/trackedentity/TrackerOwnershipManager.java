package com.mass3d.trackedentity;

import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.program.Program;
import com.mass3d.user.User;

public interface TrackerOwnershipManager
{
    String OWNERSHIP_ACCESS_DENIED = "OWNERSHIP_ACCESS_DENIED";

    String PROGRAM_ACCESS_CLOSED = "PROGRAM_ACCESS_CLOSED";

    /**
     * @param entityInstance The tracked entity instance object
     * @param program The program object
     * @param orgUnit The org unit that has to become the owner
     * @param skipAccessValidation whether ownership access validation has to be
     *        skipped or not.
     */
    void transferOwnership(TrackedEntityInstance entityInstance, Program program,
        OrganisationUnit orgUnit,
        boolean skipAccessValidation, boolean createIfNotExists);

    /**
     * @param entityInstance The tracked entity instance object
     * @param program The program object
     * @param organisationUnit The org unit that has to become the owner
     */
    void assignOwnership(TrackedEntityInstance entityInstance, Program program,
        OrganisationUnit organisationUnit,
        boolean skipAccessValidation, boolean overwriteIfExists);

    /**
     * Check whether the user has access (as owner or has temporarily broken the
     * glass) for the tracked entity instance - program combination.
     *
     * @param user The user with which access has to be checked for.
     * @param entityInstance The tracked entity instance.
     * @param program The program.
     * @return true if the user has access, false otherwise.
     */
    boolean hasAccess(User user, TrackedEntityInstance entityInstance, Program program);

    /**
     * Grant temporary ownership for a user for a specific tei-program
     * combination
     *
     * @param entityInstance The tracked entity instance object
     * @param program The program object
     * @param user The user for which temporary access is granted.
     * @param reason The reason for requesting temporary ownership
     */
    void grantTemporaryOwnership(TrackedEntityInstance entityInstance, Program program, User user,
        String reason);
}
