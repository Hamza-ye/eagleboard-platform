package com.mass3d.keyjsonvalue.hibernate;

import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.keyjsonvalue.KeyJsonValue;
import com.mass3d.keyjsonvalue.KeyJsonValueStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.keyjsonvalue.KeyJsonValueStore" )
public class HibernateKeyJsonValueStore
    extends HibernateIdentifiableObjectStore<KeyJsonValue>
    implements KeyJsonValueStore
{
    public HibernateKeyJsonValueStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, KeyJsonValue.class, currentUserService, aclService,
            true );
    }

    @Override
    public List<String> getNamespaces()
    {
        String hql = "select distinct namespace from KeyJsonValue";
        Query<String> query = getTypedQuery( hql );
        return query.list();
    }

    @Override
    public List<String> getKeysInNamespace( String namespace )
    {
        String hql = "select key from KeyJsonValue where namespace = :namespace";
        Query<String> query = getTypedQuery( hql );
        return query.setParameter( "namespace", namespace ).list();
    }

    @Override
    public List<String> getKeysInNamespace( String namespace, Date lastUpdated )
    {
        String hql = "select key from KeyJsonValue where namespace = :namespace";

        if ( lastUpdated != null )
        {
            hql += " and lastupdated >= :lastUpdated ";
        }

        Query<String> query = getTypedQuery( hql );
        query.setParameter( "namespace", namespace );

        if ( lastUpdated != null )
        {
            query.setParameter( "lastUpdated", lastUpdated );
        }

        return query.list();
    }

    @Override
    public List<KeyJsonValue> getKeyJsonValueByNamespace( String namespace )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters().addPredicate( root -> builder.equal( root.get( "namespace" ), namespace ) ) );
    }

    @Override
    public KeyJsonValue getKeyJsonValue( String namespace, String key )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getSingleResult( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "namespace" ), namespace ) )
            .addPredicate( root -> builder.equal( root.get( "key" ), key ) ) );
    }
}
