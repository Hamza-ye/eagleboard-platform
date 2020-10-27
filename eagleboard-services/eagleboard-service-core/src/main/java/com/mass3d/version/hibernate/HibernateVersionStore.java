package com.mass3d.version.hibernate;

import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.hibernate.HibernateGenericStore;
import com.mass3d.version.Version;
import com.mass3d.version.VersionStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.version.VersionStore" )
public class HibernateVersionStore
    extends HibernateGenericStore<Version>
    implements VersionStore
{
    public HibernateVersionStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher )
    {
        super( sessionFactory, jdbcTemplate, publisher, Version.class, true );
    }

    @Override
    public Version getVersionByKey( String key )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getSingleResult( builder, newJpaParameters().addPredicate( root -> builder.equal( root.get( "key" ), key ) ) );
    }
}
