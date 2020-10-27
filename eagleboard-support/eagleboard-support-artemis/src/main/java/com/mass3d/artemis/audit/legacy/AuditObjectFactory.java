package com.mass3d.artemis.audit.legacy;

import com.mass3d.audit.AuditAttributes;
import com.mass3d.audit.AuditScope;
import com.mass3d.audit.AuditType;

public interface AuditObjectFactory
{
    Object create(AuditScope auditScope, AuditType auditType, Object object, String user);

    AuditAttributes collectAuditAttributes(Object auditObject);
}
