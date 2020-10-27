package com.mass3d.artemis.audit.listener;

import static com.cronutils.utils.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Optional;
import com.mass3d.artemis.audit.AuditManager;
import com.mass3d.artemis.audit.legacy.AuditObjectFactory;
import com.mass3d.artemis.config.UsernameSupplier;
import com.mass3d.audit.AuditType;
import com.mass3d.audit.Auditable;
import com.mass3d.system.util.AnnotationUtils;

public abstract class AbstractHibernateListener
{
    protected final AuditManager auditManager;
    protected final AuditObjectFactory objectFactory;
    private final UsernameSupplier usernameSupplier;

    public AbstractHibernateListener(
        AuditManager auditManager,
        AuditObjectFactory objectFactory,
        UsernameSupplier usernameSupplier )
    {
        checkNotNull( auditManager );
        checkNotNull( objectFactory );
        checkNotNull( usernameSupplier );

        this.auditManager = auditManager;
        this.objectFactory = objectFactory;
        this.usernameSupplier = usernameSupplier;
    }

    Optional<Auditable> getAuditable( Object object, String type )
    {
        if ( AnnotationUtils.isAnnotationPresent( object.getClass(), Auditable.class ) )
        {
            Auditable auditable = AnnotationUtils.getAnnotation( object.getClass(), Auditable.class );

            boolean shouldAudit = Arrays.stream( auditable.eventType() )
                .anyMatch( s -> s.contains( "all" ) || s.contains( type ) );

            if ( shouldAudit )
            {
                return Optional.of( auditable );
            }
        }

        return Optional.empty();
    }

    public String getCreatedBy()
    {
        return usernameSupplier.get();
    }

    abstract AuditType getAuditType();
}
