package com.mass3d.program.hibernate;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramTrackedEntityAttribute;
import com.mass3d.program.ProgramTrackedEntityAttributeStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.program.ProgramTrackedEntityAttributeStore" )
public class HibernateProgramTrackedEntityAttributeStore
    extends HibernateIdentifiableObjectStore<ProgramTrackedEntityAttribute>
        implements ProgramTrackedEntityAttributeStore
{
    public HibernateProgramTrackedEntityAttributeStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, ProgramTrackedEntityAttribute.class, currentUserService,
            aclService, true );
    }

    @Override
    public ProgramTrackedEntityAttribute get(Program program, TrackedEntityAttribute attribute )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getSingleResult( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "program" ), program ) )
            .addPredicate( root -> builder.equal( root.get( "attribute" ), attribute ) ) );
    }

    @Override
    public List<TrackedEntityAttribute> getAttributes( List<Program> programs )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        CriteriaQuery<TrackedEntityAttribute> query = builder.createQuery( TrackedEntityAttribute.class );
        Root<ProgramTrackedEntityAttribute> root = query.from( ProgramTrackedEntityAttribute.class );
        query.select( root.get( "attribute" ) );
        query.where( root.get( "program" ).in( programs ) );
        query.distinct( true );

        return getSession().createQuery( query ).getResultList();
    }
}
