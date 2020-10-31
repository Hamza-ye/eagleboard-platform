package com.mass3d.trackedentity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.program.Program;

public interface TrackedEntityAttributeStore
    extends IdentifiableObjectStore<TrackedEntityAttribute>
{
    String ID = TrackedEntityAttributeStore.class.getName();

    /**
     * Get attributes which are displayed in visit schedule
     *
     * @param displayOnVisitSchedule True/False value
     *
     * @return List of attributes
     */
    List<TrackedEntityAttribute> getByDisplayOnVisitSchedule(boolean displayOnVisitSchedule);

    /**
     * Get attributes which are displayed in visit schedule
     *
     * @return List of attributes
     */
    List<TrackedEntityAttribute> getDisplayInListNoProgram();

    /**
     * Check whether there already exists a TrackedEntityInstance with given unique attribute value. If yes, return
     * Optional containing UID of it. Otherwise, return empty Optional.
     *
     * @param params Query params. Contains value of unique attribute that should be checked.
     * @return Optional of TrackedEntityInstance UID or empty Optional.
     */
    Optional<String> getTrackedEntityInstanceUidWithUniqueAttributeValue(
        TrackedEntityInstanceQueryParams params);

    /**
     * Fetches all {@link TrackedEntityAttribute} linked to all
     * {@link TrackedEntityType} present in the system
     *
     * @return a Set of {@link TrackedEntityAttribute}
     */
    Set<TrackedEntityAttribute> getTrackedEntityAttributesByTrackedEntityTypes();

    /**
     * Fetches all {@link TrackedEntityAttribute} and groups them by {@link Program}
     *
     * @return a Map, where the key is the {@link Program} and the values is a Set of
     * {@link TrackedEntityAttribute} associated to the {@link Program} in the key
     */
    Map<Program, Set<TrackedEntityAttribute>> getTrackedEntityAttributesByProgram();
}
