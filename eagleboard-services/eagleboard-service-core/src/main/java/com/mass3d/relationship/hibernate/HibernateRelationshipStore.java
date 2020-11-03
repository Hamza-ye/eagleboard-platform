package com.mass3d.relationship.hibernate;

import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramStageInstance;
import com.mass3d.relationship.Relationship;
import com.mass3d.relationship.RelationshipItem;
import com.mass3d.relationship.RelationshipStore;
import com.mass3d.relationship.RelationshipType;
import com.mass3d.security.acl.AclService;
import com.mass3d.trackedentity.TrackedEntityInstance;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.relationship.RelationshipStore" )
public class HibernateRelationshipStore
    extends HibernateIdentifiableObjectStore<Relationship>
    implements RelationshipStore
{
    public HibernateRelationshipStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, Relationship.class, currentUserService, aclService, true );
    }

    @Override
    public List<Relationship> getByTrackedEntityInstance( TrackedEntityInstance tei )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root ->
                builder.or(
                    builder.equal( root.join( "from" ).get( "trackedEntityInstance" ), tei )
                    ,builder.equal( root.join( "to" ).get( "trackedEntityInstance" ), tei ) ) ) );
    }

    @Override
    public List<Relationship> getByProgramInstance( ProgramInstance pi )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root ->
                builder.or(
                    builder.equal( root.join( "from" ).get( "programInstance" ), pi )
                    ,builder.equal( root.join( "to" ).get( "programInstance" ), pi ) ) ) );
    }

    @Override
    public List<Relationship> getByProgramStageInstance( ProgramStageInstance psi )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root ->
                builder.or(
                    builder.equal( root.join( "from" ).get( "programStageInstance" ), psi )
                    ,builder.equal( root.join( "to" ).get( "programStageInstance" ), psi ) ) ) );
    }

    @Override
    public List<Relationship> getByRelationshipType( RelationshipType relationshipType )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.join( "relationshipType" ), relationshipType ) ) );

    }

    @Override
    public Relationship getByRelationship( Relationship relationship )
    {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Relationship> criteriaQuery = builder.createQuery( Relationship.class );

        Root<Relationship> root = criteriaQuery.from( Relationship.class );

        criteriaQuery.where( builder.and(
            getFromOrToPredicate("from", builder, root, relationship),
            getFromOrToPredicate("to", builder, root, relationship),
            builder.equal( root.join( "relationshipType" ), relationship.getRelationshipType() ) ) );

        try
        {
            return getSession().createQuery( criteriaQuery ).setMaxResults( 1 ).getSingleResult();
        }
        catch ( NoResultException nre )
        {
            return null;
        }

    }

    private Predicate getFromOrToPredicate(String direction, CriteriaBuilder builder, Root<Relationship> root, Relationship relationship) {

        RelationshipItem relationshipItemDirection = getItem( direction, relationship );

        if ( relationshipItemDirection.getTrackedEntityInstance() != null )
        {
            return builder.equal( root.join( direction ).get( "trackedEntityInstance" ),
                getItem( direction, relationship ).getTrackedEntityInstance() );
        }
        else if ( relationshipItemDirection.getProgramInstance() != null )
        {
            return builder.equal( root.join( direction ).get( "programInstance" ),
                getItem( direction, relationship ).getProgramInstance() );
        }
        else if ( relationshipItemDirection.getProgramStageInstance() != null )
        {
            return builder.equal( root.join( direction ).get( "programStageInstance" ),
                getItem( direction, relationship ).getProgramStageInstance() );
        }
        else
        {
            return null;
        }
    }

    private RelationshipItem getItem( String direction, Relationship relationship )
    {
        return (direction.equalsIgnoreCase( "from" ) ? relationship.getFrom() : relationship.getTo());
    }
}
