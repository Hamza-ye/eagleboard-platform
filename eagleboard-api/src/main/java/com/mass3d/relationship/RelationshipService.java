package com.mass3d.relationship;

import java.util.List;
import java.util.Optional;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramStageInstance;
import com.mass3d.trackedentity.TrackedEntityInstance;

public interface RelationshipService
{
    String ID = RelationshipService.class.getName();

    boolean relationshipExists(String uid);

    /**
     * Adds an {@link Relationship}
     *
     * @param relationship the relationship.
     * @return id of the added relationship.
     */
    long addRelationship(Relationship relationship);

    /**
     * Returns a {@link Relationship}.
     *
     * @param relationship the relationship.
     */
    void deleteRelationship(Relationship relationship);

    /**
     * Updates a {@link Relationship}.
     *
     * @param relationship the relationship.
     */
    void updateRelationship(Relationship relationship);

    /**
     * Returns a {@link Relationship}.
     *
     * @param id the id of the relationship to return.
     * @return the relationship with the given identifier.
     */
    Relationship getRelationship(long id);

    /**
     * Fetches a {@link Relationship} based on a relationship identifying attributes:
     *
     * - relationship type
     * - from
     * - to
     *
     * @param relationship A valid Relationship
     * @return an Optional Relationship
     */
    Optional<Relationship> getRelationshipByRelationship(Relationship relationship);

    Relationship getRelationship(String uid);

    List<Relationship> getRelationshipsByTrackedEntityInstance(TrackedEntityInstance tei,
        boolean skipAccessValidation);

    List<Relationship> getRelationshipsByProgramInstance(ProgramInstance pi,
        boolean skipAccessValidation);

    List<Relationship> getRelationshipsByProgramStageInstance(ProgramStageInstance psi,
        boolean skipAccessValidation);

    List<Relationship> getRelationshipsByRelationshipType(RelationshipType relationshipType);
}
