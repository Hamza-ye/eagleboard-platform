package com.mass3d.trackedentityattributevalue;

import java.util.Collection;
import java.util.List;
import com.mass3d.common.GenericStore;
import com.mass3d.program.Program;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.trackedentity.TrackedEntityInstance;

public interface TrackedEntityAttributeValueStore
    extends GenericStore<TrackedEntityAttributeValue>
{
    String ID = TrackedEntityAttributeValueStore.class.getName();

    /**
     * Adds a {@link TrackedEntityAttribute}
     *
     * @param attributeValue The to TrackedEntityAttribute add.
     */
    void saveVoid(TrackedEntityAttributeValue attributeValue);

    /**
     * Deletes all {@link TrackedEntityAttributeValue} of a instance
     *
     * @param instance {@link TrackedEntityInstance}
     * @return The error code. If the code is 0, deleting success
     */
    int deleteByTrackedEntityInstance(TrackedEntityInstance instance);

    /**
     * Retrieve a {@link TrackedEntityAttributeValue} on a
     * {@link TrackedEntityInstance} and {@link TrackedEntityAttribute}
     *
     * @param instance  the {@link TrackedEntityInstance}
     * @param attribute the {@link TrackedEntityAttribute}
     * @return TrackedEntityAttributeValue
     */
    TrackedEntityAttributeValue get(TrackedEntityInstance instance,
        TrackedEntityAttribute attribute);

    /**
     * Retrieve {@link TrackedEntityAttributeValue} of a
     * {@link TrackedEntityInstance}
     *
     * @param instance TrackedEntityInstance
     * @return TrackedEntityAttributeValue list
     */
    List<TrackedEntityAttributeValue> get(TrackedEntityInstance instance);

    /**
     * Retrieve {@link TrackedEntityAttributeValue} of a
     * {@link TrackedEntityInstance}
     *
     * @param attribute the {@link TrackedEntityAttribute}
     * @return TrackedEntityAttributeValue list
     */
    List<TrackedEntityAttributeValue> get(TrackedEntityAttribute attribute);

    /**
     * Retrieve {@link TrackedEntityAttributeValue} of a instance list
     *
     * @param instances TrackedEntityInstance list
     * @return TrackedEntityAttributeValue list
     */
    List<TrackedEntityAttributeValue> get(Collection<TrackedEntityInstance> instances);

    /**
     * Search TrackedEntityAttributeValue objects by a TrackedEntityAttribute
     * and a attribute value (performs partial search )
     *
     * @param attribute  TrackedEntityAttribute
     * @param searchText A string for searching by attribute values
     * @return TrackedEntityAttributeValue list
     */
    List<TrackedEntityAttributeValue> searchByValue(TrackedEntityAttribute attribute,
        String searchText);

    /**
     * Gets a list of {@link TrackedEntityAttributeValue} that matches the parameters
     *
     * @param attribute {@link TrackedEntityAttribute} to get value for
     * @param value     literal value to find within the specified {@link TrackedEntityAttribute}
     * @return list of {@link TrackedEntityAttributeValue}
     */
    List<TrackedEntityAttributeValue> get(TrackedEntityAttribute attribute, String value);


    /**
     * Retrieve attribute values of an instance by a program.
     *
     * @param instance the TrackedEntityInstance
     * @param program  the Program.
     * @return TrackedEntityAttributeValue list
     */
    List<TrackedEntityAttributeValue> get(TrackedEntityInstance instance, Program program);

    /**
     * Return the number of assigned {@link TrackedEntityAttributeValue}s to the {@link TrackedEntityAttribute}
     *
     * @param attribute {@link TrackedEntityAttribute}
     * @return Number of assigned TrackedEntityAttributeValues
     */
    int getCountOfAssignedTEAValues(TrackedEntityAttribute attribute);
}
