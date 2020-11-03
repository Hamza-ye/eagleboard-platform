package com.mass3d.trackedentity.hibernate;

import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.trackedentity.TrackedEntityType;
import com.mass3d.trackedentity.TrackedEntityTypeStore;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.trackedentity.TrackedEntityTypeStore" )
public class HibernateTrackedEntityTypeStore
    extends HibernateIdentifiableObjectStore<TrackedEntityType>
    implements TrackedEntityTypeStore
{
    public HibernateTrackedEntityTypeStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, TrackedEntityType.class, currentUserService, aclService, true );
    }
}
