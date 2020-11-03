package com.mass3d.trackedentityattributevalue.hibernate;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.mass3d.common.AuditType;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.trackedentity.TrackedEntityInstance;
import com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValueAudit;
import com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValueAuditStore;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValueAuditStore" )
public class HibernateTrackedEntityAttributeValueAuditStore
    implements TrackedEntityAttributeValueAuditStore
{
    private SessionFactory sessionFactory;

    public HibernateTrackedEntityAttributeValueAuditStore( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    public void addTrackedEntityAttributeValueAudit( TrackedEntityAttributeValueAudit trackedEntityAttributeValueAudit )
    {
        Session session = sessionFactory.getCurrentSession();
        session.save( trackedEntityAttributeValueAudit );
    }

    @Override
    public List<TrackedEntityAttributeValueAudit> getTrackedEntityAttributeValueAudits( List<TrackedEntityAttribute> trackedEntityAttributes,
        List<TrackedEntityInstance> trackedEntityInstances, AuditType auditType )
    {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<TrackedEntityAttributeValueAudit>  query = builder.createQuery( TrackedEntityAttributeValueAudit.class );

        Root<TrackedEntityAttributeValueAudit> root = query.from( TrackedEntityAttributeValueAudit.class );

        List<Predicate> predicates = getTrackedEntityAttributeValueAuditCriteria( builder, root, trackedEntityAttributes, trackedEntityInstances, auditType );

        query.where( predicates.toArray( new Predicate[0] ) )
            .orderBy( builder.desc( root.get( "created" ) ) );

        return sessionFactory.getCurrentSession()
            .createQuery( query )
            .getResultList();
    }

    @Override
    public List<TrackedEntityAttributeValueAudit> getTrackedEntityAttributeValueAudits( List<TrackedEntityAttribute> trackedEntityAttributes,
        List<TrackedEntityInstance> trackedEntityInstances, AuditType auditType, int first, int max )
    {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<TrackedEntityAttributeValueAudit>  query = builder.createQuery( TrackedEntityAttributeValueAudit.class );

        Root<TrackedEntityAttributeValueAudit> root = query.from( TrackedEntityAttributeValueAudit.class );

        List<Predicate> predicates = getTrackedEntityAttributeValueAuditCriteria( builder, root, trackedEntityAttributes, trackedEntityInstances, auditType );

        query.where( predicates.toArray( new Predicate[0] ) )
            .orderBy( builder.desc( root.get( "created" ) ) );

        return sessionFactory.getCurrentSession()
            .createQuery( query )
            .setFirstResult( first )
            .setMaxResults( max ).getResultList();
    }

    @Override
    public int countTrackedEntityAttributeValueAudits( List<TrackedEntityAttribute> trackedEntityAttributes,
        List<TrackedEntityInstance> trackedEntityInstances, AuditType auditType )
    {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<Long>  query = builder.createQuery( Long.class );

        Root<TrackedEntityAttributeValueAudit> root = query.from( TrackedEntityAttributeValueAudit.class );

        List<Predicate> predicates = getTrackedEntityAttributeValueAuditCriteria( builder, root, trackedEntityAttributes, trackedEntityInstances, auditType );

        query.select( builder.countDistinct( root.get( "id" ) ) )
             .where( predicates.toArray( new Predicate[0] ) )
             .orderBy( builder.desc( root.get( "created" ) ) );

        return ( sessionFactory.getCurrentSession()
            .createQuery( query )
            .uniqueResult() ).intValue();
    }

    @Override
    public void deleteTrackedEntityAttributeValueAudits( TrackedEntityInstance entityInstance )
    {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery( "delete TrackedEntityAttributeValueAudit where entityInstance = :entityInstance" );
        query.setParameter( "entityInstance", entityInstance );
        query.executeUpdate();
    }

    private List<Predicate> getTrackedEntityAttributeValueAuditCriteria( CriteriaBuilder builder, Root<TrackedEntityAttributeValueAudit> root, List<TrackedEntityAttribute> trackedEntityAttributes, List<TrackedEntityInstance> trackedEntityInstances, AuditType auditType )
    {
        List<Predicate> predicates = new ArrayList<>();

        if ( trackedEntityAttributes != null && !trackedEntityAttributes.isEmpty() )
        {
            predicates.add( root.get( "attribute" ).in( trackedEntityAttributes ) );
        }

        if ( trackedEntityInstances != null && !trackedEntityInstances.isEmpty() )
        {
            predicates.add(  root.get( "entityInstance" ).in( trackedEntityInstances ) );
        }

        if ( auditType != null )
        {
            predicates.add(  builder.equal( root.get( "auditType" ), auditType ) );
        }

        return predicates;
    }
}
