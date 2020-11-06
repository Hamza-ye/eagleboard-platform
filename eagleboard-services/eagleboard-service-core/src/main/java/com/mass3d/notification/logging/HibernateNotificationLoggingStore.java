package com.mass3d.notification.logging;

import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.notification.logging.NotificationLoggingStore" )
public class HibernateNotificationLoggingStore
    extends HibernateIdentifiableObjectStore<ExternalNotificationLogEntry>
    implements NotificationLoggingStore
{
    public HibernateNotificationLoggingStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, ExternalNotificationLogEntry.class, currentUserService,
            aclService, true );
    }

    @Override
    public ExternalNotificationLogEntry getByTemplateUid( String templateUid )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getSingleResult( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "notificationTemplateUid" ), templateUid ) ) );
    }

    @Override
    public ExternalNotificationLogEntry getByKey( String key )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getSingleResult( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "key" ), key ) ) );
    }
}
