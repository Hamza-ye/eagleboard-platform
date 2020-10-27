package com.mass3d.artemis.audit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuditScheduler
{
    private final long delay = 20_000; // 20 seconds

    private final AuditProducerSupplier auditProducerSupplier;

    private final BlockingQueue<QueuedAudit> delayed = new DelayQueue<>();

    public AuditScheduler( AuditProducerSupplier auditProducerSupplier )
    {
        this.auditProducerSupplier = auditProducerSupplier;
    }

    public void addAuditItem( final Audit auditItem )
    {
        if ( log.isDebugEnabled() )
        {
            log.debug( String.format( "add Audit object with content %s to delayed queue", auditItem.toLog() ) );
        }
        final QueuedAudit postponed = new QueuedAudit( auditItem, delay );

        if ( !delayed.contains( postponed ) )
        {
            delayed.offer( postponed );
        }
    }

    @Scheduled( fixedDelay = 30_000 ) // TODO this value should come from configuration
    public void process()
    {
        final Collection<QueuedAudit> expired = new ArrayList<>();

        delayed.drainTo( expired );

        expired.stream().map( QueuedAudit::getAuditItem ).forEach( auditProducerSupplier::publish );
    }
}
