package com.mass3d.trackedentityfilter;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.program.Program;

public interface TrackedEntityInstanceFilterStore
    extends IdentifiableObjectStore<TrackedEntityInstanceFilter>
{
    /**
     * Gets trackedEntityInstanceFilters
     * @param program program of trackedEntityInstanceFilter to be fetched
     * @return list of trackedEntityInstanceFilters
     */
    List<TrackedEntityInstanceFilter> get(Program program);
}
