package com.mass3d.program;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.trackedentity.TrackedEntityAttribute;

public interface ProgramTrackedEntityAttributeStore
    extends IdentifiableObjectStore<ProgramTrackedEntityAttribute>
{
    ProgramTrackedEntityAttribute get(Program program, TrackedEntityAttribute attribute);

    /**
     * Get all TrackedEntityAttribute filtered by given list of Program
     * @param programs
     * @return List of TrackedEntityAttribute
     */
    List<TrackedEntityAttribute> getAttributes(List<Program> programs);
}
