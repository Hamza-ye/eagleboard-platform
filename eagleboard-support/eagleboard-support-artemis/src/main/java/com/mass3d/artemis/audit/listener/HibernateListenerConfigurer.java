package com.mass3d.artemis.audit.listener;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * This component configures the Hibernate Auditing listeners. The listeners are
 * responsible for "intercepting" Hibernate-managed objects after a save/update
 * operation and pass them to the Auditing sub-system.
 * <p>
 * This bean is not active during tests.
 *
 */
@Component
@Conditional( value = AuditEnabledCondition.class )
public class HibernateListenerConfigurer
{
    @PersistenceUnit
    private EntityManagerFactory emf;

    private final PostInsertAuditListener postInsertAuditListener;
    private final PostUpdateAuditListener postUpdateEventListener;
    private final PostDeleteAuditListener postDeleteEventListener;
    private final PostLoadAuditListener postLoadEventListener;

    public HibernateListenerConfigurer(
        PostInsertAuditListener postInsertAuditListener,
        PostUpdateAuditListener postUpdateEventListener,
        PostDeleteAuditListener postDeleteEventListener,
        PostLoadAuditListener postLoadEventListener )
    {
        checkNotNull( postDeleteEventListener );
        checkNotNull( postUpdateEventListener );
        checkNotNull( postInsertAuditListener );
        checkNotNull( postLoadEventListener );

        this.postInsertAuditListener = postInsertAuditListener;
        this.postUpdateEventListener = postUpdateEventListener;
        this.postDeleteEventListener = postDeleteEventListener;
        this.postLoadEventListener = postLoadEventListener;
    }

    @PostConstruct
    protected void init()
    {
        SessionFactoryImpl sessionFactory = emf.unwrap( SessionFactoryImpl.class );

        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService( EventListenerRegistry.class );

        registry.getEventListenerGroup( EventType.POST_COMMIT_INSERT ).appendListener( postInsertAuditListener );

        registry.getEventListenerGroup( EventType.POST_COMMIT_UPDATE ).appendListener( postUpdateEventListener );

        registry.getEventListenerGroup( EventType.POST_COMMIT_DELETE ).appendListener( postDeleteEventListener );

        registry.getEventListenerGroup( EventType.POST_LOAD ).appendListener( postLoadEventListener );
    }
}
