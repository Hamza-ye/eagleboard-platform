package com.mass3d.artemis.audit.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import com.mass3d.artemis.audit.Audit;
import com.mass3d.audit.AuditScope;
import com.mass3d.audit.AuditType;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn( "dhisConfigurationProvider" )
public class AuditMatrix
{
    private Map<AuditScope, Map<AuditType, Boolean>> matrix;

    public AuditMatrix( AuditMatrixConfigurer auditMatrixConfigurer )
    {
        checkNotNull( auditMatrixConfigurer );

        matrix = auditMatrixConfigurer.configure();
    }

    public boolean isEnabled( Audit audit )
    {
        return matrix.get( audit.getAuditScope() ).getOrDefault( audit.getAuditType(), false );
    }

    public boolean isEnabled( AuditScope auditScope, AuditType auditType )
    {
        return matrix.get( auditScope ).getOrDefault( auditType, false );
    }
}
