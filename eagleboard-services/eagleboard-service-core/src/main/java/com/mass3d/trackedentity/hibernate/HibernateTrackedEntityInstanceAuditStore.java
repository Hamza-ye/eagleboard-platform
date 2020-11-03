package com.mass3d.trackedentity.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.SessionFactory;
import com.mass3d.audit.payloads.TrackedEntityInstanceAudit;
import com.mass3d.hibernate.HibernateGenericStore;
import com.mass3d.hibernate.JpaQueryParameters;
import com.mass3d.trackedentity.TrackedEntityInstance;
import com.mass3d.trackedentity.TrackedEntityInstanceAuditQueryParams;
import com.mass3d.trackedentity.TrackedEntityInstanceAuditStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.trackedentity.TrackedEntityInstanceAuditStore" )
public class HibernateTrackedEntityInstanceAuditStore
    extends HibernateGenericStore<TrackedEntityInstanceAudit>
    implements TrackedEntityInstanceAuditStore
{
    public HibernateTrackedEntityInstanceAuditStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher )
    {
        super( sessionFactory, jdbcTemplate, publisher, TrackedEntityInstanceAudit.class, false );
    }

    // -------------------------------------------------------------------------
    // TrackedEntityInstanceAuditService implementation
    // -------------------------------------------------------------------------

    @Override
    public void addTrackedEntityInstanceAudit( TrackedEntityInstanceAudit trackedEntityInstanceAudit )
    {
        getSession().save( trackedEntityInstanceAudit );
    }

    @Override
    public void deleteTrackedEntityInstanceAudit( TrackedEntityInstance trackedEntityInstance )
    {
        String hql = "delete TrackedEntityInstanceAudit where trackedEntityInstance = :trackedEntityInstance";
        getSession().createQuery( hql ).setParameter( "trackedEntityInstance", trackedEntityInstance ).executeUpdate();
    }

    @Override
    public List<TrackedEntityInstanceAudit> getTrackedEntityInstanceAudits( TrackedEntityInstanceAuditQueryParams params )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<TrackedEntityInstanceAudit> jpaParameters = newJpaParameters()
            .addPredicates( getTrackedEntityInstanceAuditPredicates( params, builder ) )
            .addOrder( root -> builder.desc( root.get( "created" ) ) );

        if( !params.isSkipPaging() )
        {
            jpaParameters.setFirstResult( params.getFirst() ).setMaxResults( params.getMax() );
        }

        return getList( builder, jpaParameters );
    }

    @Override
    public int getTrackedEntityInstanceAuditsCount( TrackedEntityInstanceAuditQueryParams params )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getCount( builder, newJpaParameters()
            .addPredicates( getTrackedEntityInstanceAuditPredicates( params, builder ) )
            .count( root -> builder.countDistinct( root.get( "id" ) ) ) ).intValue();
    }

    private List<Function<Root<TrackedEntityInstanceAudit>, Predicate>> getTrackedEntityInstanceAuditPredicates( TrackedEntityInstanceAuditQueryParams params, CriteriaBuilder builder )
    {
        List<Function<Root<TrackedEntityInstanceAudit>, Predicate>> predicates = new ArrayList<>();

        if ( params.hasTrackedEntityInstances() )
        {
            predicates.add( root -> root.get( "trackedEntityInstance").in( params.getTrackedEntityInstances() ) );
        }

        if ( params.hasUsers() )
        {
            predicates.add( root -> root.get( "accessedBy" ).in( params.getUsers() ) );
        }

        if ( params.hasAuditType() )
        {
            predicates.add( root -> builder.equal( root.get( "auditType" ), params.getAuditType() ) );
        }

        if ( params.hasStartDate() )
        {
            predicates.add( root -> builder.greaterThanOrEqualTo( root.get("created" ), params.getStartDate() ) );
        }

        if ( params.hasEndDate() )
        {
            predicates.add( root -> builder.lessThanOrEqualTo( root.get( "created" ), params.getEndDate() ) );
        }

        return predicates;
    }
}
