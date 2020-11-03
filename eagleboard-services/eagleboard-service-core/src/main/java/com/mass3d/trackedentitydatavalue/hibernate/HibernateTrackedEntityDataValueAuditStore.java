package com.mass3d.trackedentitydatavalue.hibernate;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.mass3d.common.AuditType;
import com.mass3d.dataelement.DataElement;
import com.mass3d.program.ProgramStageInstance;
import com.mass3d.trackedentitydatavalue.TrackedEntityDataValueAudit;
import com.mass3d.trackedentitydatavalue.TrackedEntityDataValueAuditStore;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.trackedentitydatavalue.TrackedEntityDataValueAuditStore" )
public class HibernateTrackedEntityDataValueAuditStore
    implements TrackedEntityDataValueAuditStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SessionFactory sessionFactory;

    public HibernateTrackedEntityDataValueAuditStore( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    public void addTrackedEntityDataValueAudit( TrackedEntityDataValueAudit trackedEntityDataValueAudit )
    {
        Session session = sessionFactory.getCurrentSession();
        session.save( trackedEntityDataValueAudit );
    }

    @Override
    public List<TrackedEntityDataValueAudit> getTrackedEntityDataValueAudits( List<DataElement> dataElements,
        List<ProgramStageInstance> programStageInstances, AuditType auditType )
    {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<TrackedEntityDataValueAudit> query = builder.createQuery( TrackedEntityDataValueAudit.class );
        Root<TrackedEntityDataValueAudit> root = query.from( TrackedEntityDataValueAudit.class );
        query.select( root );

        List<Predicate> predicates = getTrackedEntityDataValueAuditCriteria( dataElements, programStageInstances, auditType, builder, root );
        query.where( predicates.toArray( new Predicate[ predicates.size() ] ) );
        query.orderBy( builder.desc( root.get( "created" ) ) );

        return sessionFactory.getCurrentSession().createQuery( query ).getResultList();
    }

    @Override
    public List<TrackedEntityDataValueAudit> getTrackedEntityDataValueAudits( List<DataElement> dataElements,
        List<ProgramStageInstance> programStageInstances, AuditType auditType, int first, int max )
    {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<TrackedEntityDataValueAudit> query = builder.createQuery( TrackedEntityDataValueAudit.class );
        Root<TrackedEntityDataValueAudit> root = query.from( TrackedEntityDataValueAudit.class );
        query.select( root );

        List<Predicate> predicates = getTrackedEntityDataValueAuditCriteria( dataElements, programStageInstances, auditType, builder, root );
        query.where( predicates.toArray( new Predicate[ predicates.size() ] ) );
        query.orderBy( builder.desc( root.get( "created" ) ) );

        return sessionFactory.getCurrentSession().createQuery( query )
                .setFirstResult( first )
                .setMaxResults( max )
                .getResultList();
    }

    @Override
    public int countTrackedEntityDataValueAudits( List<DataElement> dataElements, List<ProgramStageInstance> programStageInstances, AuditType auditType )
    {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery( Long.class );
        Root<TrackedEntityDataValueAudit> root = query.from( TrackedEntityDataValueAudit.class );
        query.select( builder.countDistinct( root.get( "id" ) ) );

        List<Predicate> predicates = getTrackedEntityDataValueAuditCriteria( dataElements, programStageInstances, auditType, builder, root );
        query.where( predicates.toArray( new Predicate[ predicates.size() ] ) );

        return sessionFactory.getCurrentSession().createQuery( query ).getSingleResult().intValue();
    }

    private List<Predicate> getTrackedEntityDataValueAuditCriteria( List<DataElement> dataElements, List<ProgramStageInstance> programStageInstances,
        AuditType auditType, CriteriaBuilder builder, Root<TrackedEntityDataValueAudit> root )
    {
        List<Predicate> predicates = new ArrayList<>();

        if ( dataElements != null && !dataElements.isEmpty() )
        {
            Expression<DataElement> dataElementExpression = root.get( "dataElement" );
            Predicate dataElementPredicate = dataElementExpression.in( dataElements );
            predicates.add( dataElementPredicate );
        }

        if ( programStageInstances != null && !programStageInstances.isEmpty() )
        {
            Expression<DataElement> psiExpression = root.get( "programStageInstance" );
            Predicate psiPredicate = psiExpression.in( programStageInstances );
            predicates.add( psiPredicate );
        }

        if ( auditType != null )
        {
            predicates.add( builder.equal( root.get( "auditType" ), auditType ) );
        }

        return predicates;
    }
}