package com.mass3d.trackedentityfilter;

import java.util.List;
import com.mass3d.program.Program;

public interface TrackedEntityInstanceFilterService
{
    String ID = TrackedEntityInstanceFilter.class.getName();
    
    /** 
     * Adds trackedEntityInstanceFilter
     * 
     * @param trackedEntityInstanceFilter
     * @return id of added trackedEntityInstanceFilter
     */
    long add(TrackedEntityInstanceFilter trackedEntityInstanceFilter);
    
    /**
     * Deletes trackedEntityInstanceFilter
     * 
     * @param trackedEntityInstanceFilter
     */
    void delete(TrackedEntityInstanceFilter trackedEntityInstanceFilter);
    
    /**
     * Updates trackedEntityInstanceFilter
     * 
     * @param trackedEntityInstanceFilter
     */
    void update(TrackedEntityInstanceFilter trackedEntityInstanceFilter);
    
    /**
     * Gets trackedEntityInstanceFilter 
     * @param id id of trackedEntityInstanceFilter to be fetched
     * @return trackedEntityInstanceFilter
     */
    TrackedEntityInstanceFilter get(long id);
    
    /**
     * Gets trackedEntityInstanceFilter
     * @param program program of trackedEntityInstanceFilter to be fetched
     * @return trackedEntityInstanceFilter
     */
    List<TrackedEntityInstanceFilter> get(Program program);
    
    /**
     * Gets all trackedEntityInstanceFilters 
     * @return list of trackedEntityInstanceFilters
     */
    List<TrackedEntityInstanceFilter> getAll();

}
