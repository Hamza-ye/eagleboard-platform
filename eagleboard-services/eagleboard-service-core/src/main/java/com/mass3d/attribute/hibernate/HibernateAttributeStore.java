package com.mass3d.attribute.hibernate;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.attribute.Attribute;
import com.mass3d.attribute.AttributeStore;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.attribute.AttributeStore" )
public class HibernateAttributeStore
    extends HibernateIdentifiableObjectStore<Attribute>
    implements AttributeStore
{
    @Autowired
    public HibernateAttributeStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
        CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, Attribute.class, currentUserService, aclService, true );
    }

    @Override
    public List<Attribute> getAttributes( Class<?> klass )
    {
        if ( !CLASS_ATTRIBUTE_MAP.containsKey( klass ) )
        {
            return new ArrayList<>();
        }

        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( CLASS_ATTRIBUTE_MAP.get( klass ) ), true ) ) );
    }

    @Override
    public List<Attribute> getMandatoryAttributes( Class<?> klass )
    {
        if ( !CLASS_ATTRIBUTE_MAP.containsKey( klass ) )
        {
            return new ArrayList<>();
        }

        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( CLASS_ATTRIBUTE_MAP.get( klass ) ), true ) )
            .addPredicate( root -> builder.equal( root.get( "mandatory" ), true ) ) );
    }

    @Override
    public List<Attribute> getUniqueAttributes( Class<?> klass )
    {
        if ( !CLASS_ATTRIBUTE_MAP.containsKey( klass ) )
        {
            return new ArrayList<>();
        }

        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( CLASS_ATTRIBUTE_MAP.get( klass ) ), true ) )
            .addPredicate( root -> builder.equal( root.get( "unique" ), true ) ) );
    }
}
