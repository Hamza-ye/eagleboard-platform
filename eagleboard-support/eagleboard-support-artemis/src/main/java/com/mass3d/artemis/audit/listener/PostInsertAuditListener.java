package com.mass3d.artemis.audit.listener;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.PostCommitInsertEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.persister.entity.EntityPersister;
import com.mass3d.artemis.audit.Audit;
import com.mass3d.artemis.audit.AuditManager;
import com.mass3d.artemis.audit.AuditableEntity;
import com.mass3d.artemis.audit.legacy.AuditObjectFactory;
import com.mass3d.artemis.config.UsernameSupplier;
import com.mass3d.audit.AuditType;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostInsertAuditListener
    extends AbstractHibernateListener implements PostCommitInsertEventListener
{
    public PostInsertAuditListener(
        AuditManager auditManager,
        AuditObjectFactory auditObjectFactory,
        UsernameSupplier userNameSupplier )
    {
        super( auditManager, auditObjectFactory, userNameSupplier );
    }

    @Override
    AuditType getAuditType()
    {
        return AuditType.CREATE;
    }

    @Override
    public void onPostInsert( PostInsertEvent postInsertEvent )
    {
        Object entity = postInsertEvent.getEntity();

        getAuditable( entity, "create" ).ifPresent( auditable ->
            auditManager.send( Audit.builder()
                .auditType( getAuditType() )
                .auditScope( auditable.scope() )
                .createdAt( LocalDateTime.now() )
                .createdBy( getCreatedBy() )
                .object( entity )
                .auditableEntity( new AuditableEntity( entity ) )
                .build() ) );
    }

    @Override
    public boolean requiresPostCommitHanding( EntityPersister entityPersister )
    {
        return true;
    }

    @Override
    public void onPostInsertCommitFailed( PostInsertEvent event )
    {
        log.warn( "onPostInsertCommitFailed: " + event );
    }
}
