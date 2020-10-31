package com.mass3d.trackedentityattributevalue;

import java.util.List;
import com.mass3d.common.AuditType;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.trackedentity.TrackedEntityInstance;

public interface TrackedEntityAttributeValueAuditService
{
    void addTrackedEntityAttributeValueAudit(
        TrackedEntityAttributeValueAudit trackedEntityAttributeValueAudit);

    List<TrackedEntityAttributeValueAudit> getTrackedEntityAttributeValueAudits(
        List<TrackedEntityAttribute> trackedEntityAttributes,
        List<TrackedEntityInstance> trackedEntityInstances, AuditType auditType);

    List<TrackedEntityAttributeValueAudit> getTrackedEntityAttributeValueAudits(
        List<TrackedEntityAttribute> trackedEntityAttributes,
        List<TrackedEntityInstance> trackedEntityInstances, AuditType auditType, int first, int max);

    int countTrackedEntityAttributeValueAudits(List<TrackedEntityAttribute> trackedEntityAttributes,
        List<TrackedEntityInstance> trackedEntityInstances, AuditType auditType);
    
    void deleteTrackedEntityAttributeValueAudits(TrackedEntityInstance trackedEntityInstance);
}
