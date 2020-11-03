package com.mass3d.relationship.hibernate;

import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.relationship.RelationshipType;
import com.mass3d.relationship.RelationshipTypeStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.relationship.RelationshipTypeStore" )
public class HibernateRelationshipTypeStore
    extends HibernateIdentifiableObjectStore<RelationshipType>
    implements RelationshipTypeStore
{
    public HibernateRelationshipTypeStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, RelationshipType.class, currentUserService, aclService, true );
    }

    @Override
    public RelationshipType getRelationshipType( String aIsToB, String bIsToA )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getSingleResult( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "aIsToB" ), aIsToB ) )
            .addPredicate( root -> builder.equal( root.get( "bIsToA" ), bIsToA ) ) );
    }
}
