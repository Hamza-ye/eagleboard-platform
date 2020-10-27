package com.mass3d.artemis.audit.listener;

import java.time.LocalDateTime;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import com.mass3d.artemis.audit.Audit;
import com.mass3d.artemis.audit.AuditManager;
import com.mass3d.artemis.audit.AuditableEntity;
import com.mass3d.artemis.audit.legacy.AuditObjectFactory;
import com.mass3d.artemis.config.UsernameSupplier;
import com.mass3d.audit.AuditType;
import org.springframework.stereotype.Component;

@Component
public class PostLoadAuditListener
    extends AbstractHibernateListener implements PostLoadEventListener
{
    public PostLoadAuditListener(
        AuditManager auditManager,
        AuditObjectFactory auditObjectFactory,
        UsernameSupplier userNameSupplier )
    {
        super( auditManager, auditObjectFactory, userNameSupplier );
    }

    AuditType getAuditType()
    {
        return AuditType.READ;
    }

    @Override
    public void onPostLoad( PostLoadEvent postLoadEvent )
    {
        Object entity = postLoadEvent.getEntity();

        getAuditable( entity, "read" ).ifPresent( auditable ->
            auditManager.send( Audit.builder()
                .auditType( getAuditType() )
                .auditScope( auditable.scope() )
                .createdAt( LocalDateTime.now() )
                .createdBy( getCreatedBy() )
                .object( entity )
                .auditableEntity( new AuditableEntity( entity ) )
                .build() ) );
    }
}
