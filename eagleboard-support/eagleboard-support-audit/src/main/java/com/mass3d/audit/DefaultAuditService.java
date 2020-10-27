package com.mass3d.audit;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuditService implements AuditService
{
    private final AuditRepository auditRepository;

    public DefaultAuditService( AuditRepository auditRepository )
    {
        this.auditRepository = auditRepository;
    }

    @Override
    public long addAudit( Audit audit )
    {
        return auditRepository.save( audit );
    }

    @Override
    public int countAudits( AuditQuery query )
    {
        return auditRepository.count( query );
    }

    @Override
    public List<Audit> getAudits( AuditQuery query )
    {
        return auditRepository.query( query );
    }
}
