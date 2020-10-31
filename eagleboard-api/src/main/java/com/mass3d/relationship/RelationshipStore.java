package com.mass3d.relationship;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramStageInstance;
import com.mass3d.trackedentity.TrackedEntityInstance;

public interface RelationshipStore
    extends IdentifiableObjectStore<Relationship>
{
    String ID = RelationshipStore.class.getName();

    List<Relationship> getByTrackedEntityInstance(TrackedEntityInstance tei);

    List<Relationship> getByProgramInstance(ProgramInstance pi);

    List<Relationship> getByProgramStageInstance(ProgramStageInstance psi);

    List<Relationship> getByRelationshipType(RelationshipType relationshipType);

    /**
     * Fetches a {@link Relationship} based on a relationship identifying attributes:
     * - relationship type
     * - from
     * - to
     *
     * @param relationship A valid Relationship
     *
     * @return a {@link Relationship} or null if no Relationship is found matching the identifying criterias
     */
    Relationship getByRelationship(Relationship relationship);
}
