package com.mass3d.trackedentity;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.program.Program;
import com.mass3d.user.User;

public interface TrackedEntityAttributeService
{
    String ID = TrackedEntityAttributeService.class.getName();

    /**
     * The max length of a value. This is also naturally constrained by the database table, due to the
     * data type: varchar(1200).
     */
    int TEA_VALUE_MAX_LENGTH = 1200;

    /**
     * Adds an {@link TrackedEntityAttribute}
     *
     * @param attribute The to TrackedEntityAttribute add.
     * @return A generated unique id of the added {@link TrackedEntityAttribute}
     * .
     */
    long addTrackedEntityAttribute(TrackedEntityAttribute attribute);

    /**
     * Deletes a {@link TrackedEntityAttribute}.
     *
     * @param attribute the TrackedEntityAttribute to delete.
     */
    void deleteTrackedEntityAttribute(TrackedEntityAttribute attribute);

    /**
     * Updates an {@link TrackedEntityAttribute}.
     *
     * @param attribute the TrackedEntityAttribute to update.
     */
    void updateTrackedEntityAttribute(TrackedEntityAttribute attribute);

    /**
     * Returns a {@link TrackedEntityAttribute}.
     *
     * @param id the id of the TrackedEntityAttribute to return.
     * @return the TrackedEntityAttribute with the given id
     */
    TrackedEntityAttribute getTrackedEntityAttribute(long id);

    /**
     * Returns the {@link TrackedEntityAttribute} with the given UID.
     *
     * @param uid the UID.
     * @return the TrackedEntityAttribute with the given UID, or null if no
     * match.
     */
    TrackedEntityAttribute getTrackedEntityAttribute(String uid);

    /**
     * Returns a {@link TrackedEntityAttribute} with a given name.
     *
     * @param name the name of the TrackedEntityAttribute to return.
     * @return the TrackedEntityAttribute with the given name, or null if no
     * match.
     */
    TrackedEntityAttribute getTrackedEntityAttributeByName(String name);

    /**
     * Returns all {@link TrackedEntityAttribute}
     *
     * @return a list of all TrackedEntityAttribute, or an empty
     * List if there are no TrackedEntityAttributes.
     */
    List<TrackedEntityAttribute> getAllTrackedEntityAttributes();


    Set<TrackedEntityAttribute> getAllUserReadableTrackedEntityAttributes(User user);

    Set<TrackedEntityAttribute> getAllUserReadableTrackedEntityAttributes(User user,
        List<Program> programs, List<TrackedEntityType> trackedEntityTypes);

    /**
     * Returns all {@link TrackedEntityAttribute}
     *
     * @return a List of all system wide uniqe TrackedEntityAttribute, or an empty
     * List if there are no TrackedEntityAttributes.
     */
    List<TrackedEntityAttribute> getAllSystemWideUniqueTrackedEntityAttributes();

    /**
     * Get attributes which are displayed in visit schedule
     *
     * @param displayOnVisitSchedule True/False value
     * @return a list of attributes
     */
    List<TrackedEntityAttribute> getTrackedEntityAttributesByDisplayOnVisitSchedule(
        boolean displayOnVisitSchedule);

    /**
     * Get attributes which are displayed in visit schedule
     *
     * @return a list of attributes
     */
    List<TrackedEntityAttribute> getTrackedEntityAttributesDisplayInListNoProgram();

    /**
     * Get all attributes that user is allowed to read
     * (through program and tracked entity type)
     *
     * @return a list of attributes
     */
    Set<TrackedEntityAttribute> getAllUserReadableTrackedEntityAttributes();

    /**
     * Validate uniqueness of the tracked entity attribute value within its scope. Will return non-empty error message
     * if attribute is non-unique.
     *
     * @param trackedEntityAttribute TrackedEntityAttribute
     * @param value Value
     * @param trackedEntityInstance TrackedEntityInstance - required if updating TEI
     * @param organisationUnit OrganisationUnit - only required if org unit scoped
     * @return null if valid, a message if not
     */
    String validateAttributeUniquenessWithinScope(TrackedEntityAttribute trackedEntityAttribute,
        String value, TrackedEntityInstance trackedEntityInstance,
        OrganisationUnit organisationUnit);

    /**
     * Validate value against tracked entity attribute value type.
     *
     * @param trackedEntityAttribute TrackedEntityAttribute
     * @param value Value
     * @return null if valid, a message if not
     */
    String validateValueType(TrackedEntityAttribute trackedEntityAttribute, String value);

    /**
     * Get all {@link TrackedEntityAttribute} linked to all
     * {@link TrackedEntityType} present in the system
     *
     * @return a Set of {@link TrackedEntityAttribute}
     */
    Set<TrackedEntityAttribute> getTrackedEntityAttributesByTrackedEntityTypes();

    /**
     * Get all {@link TrackedEntityAttribute} grouped by {@link Program}
     *
     * @return a Map, where the key is the {@link Program} and the values is a Set of {@link TrackedEntityAttribute} associated
     * to the {@link Program} in the key
     */
    Map<Program, Set<TrackedEntityAttribute>> getTrackedEntityAttributesByProgram();
}
