package com.mass3d.artemis.audit.listener;

import com.mass3d.audit.AuditScope;
import com.mass3d.audit.Auditable;

@Auditable( scope = AuditScope.METADATA )
public interface TestInterface
{
}
