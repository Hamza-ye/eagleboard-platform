package com.mass3d.common.hibernate;

import org.hibernate.SessionFactory;
import com.mass3d.common.ObjectDeletionRequestedEvent;
import com.mass3d.common.SoftDeletableObject;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;

public class SoftDeleteHibernateObjectStore<T extends SoftDeletableObject>
    extends HibernateIdentifiableObjectStore<T>
{
    public SoftDeleteHibernateObjectStore( SessionFactory sessionFactory,
        JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher, Class<T> clazz,
        CurrentUserService currentUserService, AclService aclService, boolean cacheable )
    {
        super( sessionFactory, jdbcTemplate, publisher, clazz, currentUserService, aclService,
            cacheable );
    }

    @Override
    public void delete( SoftDeletableObject object )
    {
        publisher.publishEvent( new ObjectDeletionRequestedEvent( object ) );
        object.setDeleted( true );
        getSession().update( object );
    }
}
