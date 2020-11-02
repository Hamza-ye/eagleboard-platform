package com.mass3d.organisationunit.hibernate;

import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.organisationunit.OrganisationUnitGroupSet;
import com.mass3d.organisationunit.OrganisationUnitGroupSetStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.organisationunit.OrganisationUnitGroupSetStore" )
public class HibernateOrganisationUnitGroupSetStore
    extends HibernateIdentifiableObjectStore<OrganisationUnitGroupSet>
    implements OrganisationUnitGroupSetStore
{
    public HibernateOrganisationUnitGroupSetStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, OrganisationUnitGroupSet.class, currentUserService, aclService, true );
    }
}
