package com.mass3d.audit;

import java.util.List;

public interface AuditRepository
{
    long save(Audit audit);

    void save(List<Audit> audits);

    void delete(Audit audit);

    void delete(AuditQuery query);

    int count(AuditQuery query);

    List<Audit> query(AuditQuery query);
}
