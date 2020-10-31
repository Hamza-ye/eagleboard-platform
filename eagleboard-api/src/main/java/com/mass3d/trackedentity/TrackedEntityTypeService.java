package com.mass3d.trackedentity;

import java.util.List;

/**
 *
 * @version $ TrackedEntityService.java Feb 15, 2014 7:23:48 PM $
 */
public interface TrackedEntityTypeService
{
    String ID = TrackedEntityTypeService.class.getName();

    /**
     * Adds an {@link TrackedEntityType}
     * 
     * @param trackedEntityType The to TrackedEntityType
     *        add.
     * 
     * @return A generated unique id of the added
     *         {@link TrackedEntityType}.
     */
    long addTrackedEntityType(TrackedEntityType trackedEntityType);

    /**
     * Deletes a {@link TrackedEntityType}.
     *
     * @param trackedEntityType the TrackedEntityType to
     *        delete.
     */
    void deleteTrackedEntityType(TrackedEntityType trackedEntityType);

    /**
     * Updates a {@link TrackedEntityType}.
     *
     * @param trackedEntityType the TrackedEntityType to
     *        update.
     */
    void updateTrackedEntityType(TrackedEntityType trackedEntityType);

    /**
     * Returns a {@link TrackedEntityType}.
     *
     * @param id the id of the TrackedEntityType to return.
     *
     * @return the TrackedEntityType with the given id
     */
    TrackedEntityType getTrackedEntityType(long id);

    /**
     * Returns a {@link TrackedEntityType}.
     *
     * @param uid the identifier of the TrackedEntityType to return.
     *
     * @return the TrackedEntityType with the given id
     */
    TrackedEntityType getTrackedEntityType(String uid);

    /**
     * Returns a {@link TrackedEntityType} with a given name.
     *
     * @param name the name of the TrackedEntityType to return.
     *
     * @return the TrackedEntityType with the given name, or null if
     *         no match.
     */
    TrackedEntityType getTrackedEntityByName(String name);

    /**
     * Returns all {@link TrackedEntityType}
     * 
     * @return a List of all TrackedEntityType, or an empty
     *         List if there are no TrackedEntitys.
     */
    List<TrackedEntityType> getAllTrackedEntityType();
}
