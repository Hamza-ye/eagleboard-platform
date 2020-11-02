package com.mass3d.organisationunit.hibernate;

import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.organisationunit.OrganisationUnitLevel;
import com.mass3d.organisationunit.OrganisationUnitLevelStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.organisationunit.OrganisationUnitLevelStore" )
public class HibernateOrganisationUnitLevelStore
    extends HibernateIdentifiableObjectStore<OrganisationUnitLevel>
    implements OrganisationUnitLevelStore
{
    public HibernateOrganisationUnitLevelStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, OrganisationUnitLevel.class, currentUserService, aclService, true );
    }

    @Override
    public void deleteAll()
    {
        String hql = "delete from OrganisationUnitLevel";

        getQuery( hql ).executeUpdate();
    }

    @Override
    public OrganisationUnitLevel getByLevel( int level )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getSingleResult( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "level" ), level ) ) );
    }
}
