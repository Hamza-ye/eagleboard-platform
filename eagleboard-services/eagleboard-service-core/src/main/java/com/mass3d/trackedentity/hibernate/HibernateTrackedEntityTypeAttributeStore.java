package com.mass3d.trackedentity.hibernate;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.jdbc.StatementBuilder;
import com.mass3d.security.acl.AclService;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.trackedentity.TrackedEntityType;
import com.mass3d.trackedentity.TrackedEntityTypeAttribute;
import com.mass3d.trackedentity.TrackedEntityTypeAttributeStore;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.program.TrackedEntityTypeAttributeStore" )
public class HibernateTrackedEntityTypeAttributeStore
    extends HibernateIdentifiableObjectStore<TrackedEntityTypeAttribute>
    implements TrackedEntityTypeAttributeStore
{
    public HibernateTrackedEntityTypeAttributeStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService,
        AclService aclService, StatementBuilder statementBuilder )
    {
        super( sessionFactory, jdbcTemplate, publisher, TrackedEntityTypeAttribute.class, currentUserService, aclService, true );
    }

    @Override
    public List<TrackedEntityAttribute> getAttributes( List<TrackedEntityType> trackedEntityTypes )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        CriteriaQuery<TrackedEntityAttribute> query = builder.createQuery( TrackedEntityAttribute.class );
        Root<TrackedEntityTypeAttribute> root = query.from( TrackedEntityTypeAttribute.class );
        query.select( root.get( "trackedEntityAttribute" ) );
        query.where( root.get( "trackedEntityType" ).in( trackedEntityTypes ) );
        query.distinct( true );

        return getSession().createQuery( query ).getResultList();
    }
}
