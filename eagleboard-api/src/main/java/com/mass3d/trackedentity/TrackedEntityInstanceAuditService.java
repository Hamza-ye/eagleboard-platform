package com.mass3d.trackedentity;

import java.util.List;
import com.mass3d.audit.payloads.TrackedEntityInstanceAudit;

public interface TrackedEntityInstanceAuditService
{
    
    String ID = TrackedEntityInstanceAuditService.class.getName();
    
    /**
     * Adds tracked entity instance audit
     * 
     * @param trackedEntityInstanceAudit the audit to add
     */
    void addTrackedEntityInstanceAudit(TrackedEntityInstanceAudit trackedEntityInstanceAudit);

    /**
     * Deletes tracked entity instance audit for the given tracked entity instance
     *
     * @param trackedEntityInstance the tracked entity instance
     */
    void deleteTrackedEntityInstanceAudit(TrackedEntityInstance trackedEntityInstance);

    /**
     * Returns tracked entity instance audits matching query params
     *
     * @param params tracked entity instance audit query params
     * @return matching TrackedEntityInstanceAudits
     */
    List<TrackedEntityInstanceAudit> getTrackedEntityInstanceAudits(
        TrackedEntityInstanceAuditQueryParams params);

    /**
     * Returns count of tracked entity instance audits matching query params
     *
     * @param params tracked entity instance audit query params
     * @return count of TrackedEntityInstanceAudits
     */
    int getTrackedEntityInstanceAuditsCount(TrackedEntityInstanceAuditQueryParams params);

}
