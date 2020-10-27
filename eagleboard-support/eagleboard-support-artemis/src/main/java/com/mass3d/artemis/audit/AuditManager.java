package com.mass3d.artemis.audit;

import static com.google.common.base.Preconditions.checkNotNull;

import lombok.extern.slf4j.Slf4j;
import com.mass3d.artemis.AuditProducerConfiguration;
import com.mass3d.artemis.audit.configuration.AuditMatrix;
import com.mass3d.artemis.audit.legacy.AuditObjectFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuditManager
{
    private final AuditProducerSupplier auditProducerSupplier;
    private final AuditProducerConfiguration config;
    private final AuditScheduler auditScheduler;
    private final AuditMatrix auditMatrix;

    private final AuditObjectFactory objectFactory;

    public AuditManager(
        AuditProducerSupplier auditProducerSupplier,
        AuditScheduler auditScheduler,
        AuditProducerConfiguration config,
        AuditMatrix auditMatrix,
        AuditObjectFactory auditObjectFactory )
    {
        checkNotNull( auditProducerSupplier );
        checkNotNull( config );
        checkNotNull( auditMatrix );
        checkNotNull( auditObjectFactory );

        this.auditProducerSupplier = auditProducerSupplier;
        this.config = config;
        this.auditScheduler = auditScheduler;
        this.auditMatrix = auditMatrix;
        this.objectFactory = auditObjectFactory;
    }

    public void send( Audit audit )
    {
        if ( !auditMatrix.isEnabled( audit ) || audit.getAuditableEntity() == null )
        {
            log.debug( "Audit message ignored:\n" + audit.toLog() );
            return;
        }

        if ( audit.getData() == null )
        {
            audit.setData( this.objectFactory.create(
                audit.getAuditScope(),
                audit.getAuditType(),
                audit.getAuditableEntity().getEntity(),
                audit.getCreatedBy() ) );
        }

        audit.setAttributes( this.objectFactory.collectAuditAttributes( audit.getAuditableEntity().getEntity() ) );

        if ( config.isUseQueue() )
        {
            auditScheduler.addAuditItem( audit );
        }
        else
        {
            auditProducerSupplier.publish( audit );
        }
    }

}
