package com.mass3d.deletedobject.hibernate;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.mass3d.common.Pager;
import com.mass3d.deletedobject.DeletedObject;
import com.mass3d.deletedobject.DeletedObjectQuery;
import com.mass3d.deletedobject.DeletedObjectStore;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.deletedobject.DeletedObjectStore" )
public class HibernateDeletedObjectStore
    implements DeletedObjectStore
{
    private SessionFactory sessionFactory;

    public HibernateDeletedObjectStore( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public long save( DeletedObject deletedObject )
    {
        getCurrentSession().save( deletedObject );

        return deletedObject.getId();
    }

    @Override
    public void delete( DeletedObject deletedObject )
    {
        getCurrentSession().delete( deletedObject );
    }

    @Override
    public void delete( DeletedObjectQuery query )
    {
        query.setSkipPaging( false );
        query( query ).forEach( this::delete );
    }

    @Override
    public List<DeletedObject> getByKlass( String klass )
    {
        DeletedObjectQuery query = new DeletedObjectQuery();
        query.getKlass().add( klass );

        return query( query );
    }

    @Override
    public int count( DeletedObjectQuery query )
    {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<Long> criteriaQuery = builder.createQuery( Long.class );

        Root<DeletedObject> root = criteriaQuery.from( DeletedObject.class );

        Predicate predicate = buildCriteria( builder, root, query );

        criteriaQuery.select( builder.countDistinct( root ) );

        if ( !predicate.getExpressions().isEmpty() ) criteriaQuery.where( predicate );

        Query<Long> typedQuery = getCurrentSession().createQuery( criteriaQuery );

        return typedQuery.getSingleResult().intValue();
    }

    @Override
    public List<DeletedObject> query( DeletedObjectQuery query )
    {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<DeletedObject> criteriaQuery = builder.createQuery( DeletedObject.class );

        Root<DeletedObject> root = criteriaQuery.from( DeletedObject.class );

        Predicate predicate = buildCriteria( builder, root, query );

        criteriaQuery.select( root );

        if ( !predicate.getExpressions().isEmpty() ) criteriaQuery.where( predicate );

        Query<DeletedObject> typedQuery = getCurrentSession().createQuery( criteriaQuery );

        if ( !query.isSkipPaging() )
        {
            Pager pager = query.getPager();
            typedQuery.setFirstResult( pager.getOffset() );
            typedQuery.setMaxResults( pager.getPageSize() );
        }

        return typedQuery.list();
    }

    private Predicate buildCriteria( CriteriaBuilder builder, Root<DeletedObject> root, DeletedObjectQuery query )
    {
        Predicate predicate = builder.conjunction();

        if ( query.getKlass().isEmpty() )
        {
            Predicate disjunction = builder.disjunction();

            if ( !query.getUid().isEmpty() )
            {
                disjunction.getExpressions().add( root.get( "uid" ).in( query.getUid() ) );
            }

            if ( !query.getCode().isEmpty() )
            {
                disjunction.getExpressions().add( root.get( "code" ).in( query.getCode() ) );
            }

            if ( !disjunction.getExpressions().isEmpty() ) predicate.getExpressions().add( disjunction );
        }
        else if ( query.getUid().isEmpty() && query.getCode().isEmpty() )
        {
            predicate.getExpressions().add( builder.or( root.get( "klass" ).in( query.getKlass() ),
                root.get( "klass" ).in( query.getKlass() ) ) );
        }
        else
        {
            Predicate disjunction = builder.disjunction();

            if ( !query.getUid().isEmpty() )
            {
                Predicate conjunction = builder.conjunction();
                conjunction.getExpressions().add( root.get( "klass" ).in( query.getKlass() ) );
                conjunction.getExpressions().add( root.get( "uid" ).in( query.getUid() ) );
                disjunction.getExpressions().add( conjunction );
            }

            if ( !query.getCode().isEmpty() )
            {
                Predicate conjunction = builder.conjunction();
                conjunction.getExpressions().add( root.get( "klass" ).in( query.getKlass() ) );
                conjunction.getExpressions().add( root.get( "code" ).in( query.getUid() ) );
                disjunction.getExpressions().add( conjunction );
            }

            if ( !disjunction.getExpressions().isEmpty() ) predicate.getExpressions().add( disjunction );
        }

        if ( query.getDeletedAt() != null )
        {
            predicate.getExpressions().add( builder.greaterThanOrEqualTo( root.get( "deletedAt" ), query.getDeletedAt() ) );
        }

        return predicate;
    }

    private Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }
}
