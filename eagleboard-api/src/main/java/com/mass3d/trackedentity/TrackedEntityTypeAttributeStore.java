package com.mass3d.trackedentity;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

public interface TrackedEntityTypeAttributeStore
    extends IdentifiableObjectStore<TrackedEntityTypeAttribute>
{
    /**
     * Get all TrackedEntityAttribute filtered by given List of TrackedEntityType
     * @param trackedEntityTypes
     * @return List of TrackedEntityAttribute
     */
    List<TrackedEntityAttribute> getAttributes(List<TrackedEntityType> trackedEntityTypes);
}
