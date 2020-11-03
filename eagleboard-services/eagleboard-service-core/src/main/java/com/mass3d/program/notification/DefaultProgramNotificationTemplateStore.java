package com.mass3d.program.notification;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.program.notification.ProgramNotificationTemplateStore" )
public class DefaultProgramNotificationTemplateStore extends HibernateIdentifiableObjectStore<ProgramNotificationTemplate>
    implements ProgramNotificationTemplateStore
{
    public DefaultProgramNotificationTemplateStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, ProgramNotificationTemplate.class, currentUserService,
            aclService, true );
    }

    @Override
    public List<ProgramNotificationTemplate> getProgramNotificationByTriggerType( NotificationTrigger trigger )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "notificationtrigger" ), trigger ) ) );
    }
}
