package com.mass3d.trackedentityfilter.hibernate;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.program.Program;
import com.mass3d.security.acl.AclService;
import com.mass3d.trackedentityfilter.TrackedEntityInstanceFilter;
import com.mass3d.trackedentityfilter.TrackedEntityInstanceFilterStore;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.trackedentityfilter.TrackedEntityInstanceFilterStore" )
public class HibernateTrackedEntityInstanceFilterStore
    extends HibernateIdentifiableObjectStore<TrackedEntityInstanceFilter> implements TrackedEntityInstanceFilterStore
{
    public HibernateTrackedEntityInstanceFilterStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, TrackedEntityInstanceFilter.class, currentUserService,
            aclService, true );
    }

    @Override
    public List<TrackedEntityInstanceFilter> get( Program program )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "program" ), program ) ) );
    }
}
