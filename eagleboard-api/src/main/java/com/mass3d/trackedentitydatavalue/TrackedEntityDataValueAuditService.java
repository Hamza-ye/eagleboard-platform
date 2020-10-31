package com.mass3d.trackedentitydatavalue;

import java.util.List;
import com.mass3d.common.AuditType;
import com.mass3d.dataelement.DataElement;
import com.mass3d.program.ProgramStageInstance;

public interface TrackedEntityDataValueAuditService
{
    void addTrackedEntityDataValueAudit(TrackedEntityDataValueAudit trackedEntityDataValueAudit);

    List<TrackedEntityDataValueAudit> getTrackedEntityDataValueAudits(
        List<DataElement> dataElements, List<ProgramStageInstance> programStageInstances,
        AuditType auditType);

    List<TrackedEntityDataValueAudit> getTrackedEntityDataValueAudits(
        List<DataElement> dataElements, List<ProgramStageInstance> programStageInstances,
        AuditType auditType, int first, int max);

    int countTrackedEntityDataValueAudits(List<DataElement> dataElements,
        List<ProgramStageInstance> programStageInstances, AuditType auditType);
}
