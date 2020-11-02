package com.mass3d.organisationunit.hibernate;

import java.util.List;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.organisationunit.OrganisationUnitGroup;
import com.mass3d.organisationunit.OrganisationUnitGroupStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.organisationunit.OrganisationUnitGroupStore" )
public class HibernateOrganisationUnitGroupStore
    extends HibernateIdentifiableObjectStore<OrganisationUnitGroup>
    implements OrganisationUnitGroupStore
{
    public HibernateOrganisationUnitGroupStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, OrganisationUnitGroup.class, currentUserService, aclService, true );
    }

    @Override
    public List<OrganisationUnitGroup> getOrganisationUnitGroupsWithGroupSets()
    {
        return getQuery( "from OrganisationUnitGroup o where o.groupSet is not null" ).list();
    }
}
