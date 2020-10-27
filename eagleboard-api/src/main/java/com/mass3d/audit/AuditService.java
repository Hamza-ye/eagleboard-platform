package com.mass3d.audit;

import java.util.List;

public interface AuditService
{
    long addAudit(Audit audit);

    int countAudits(AuditQuery query);

    List<Audit> getAudits(AuditQuery query);
}
