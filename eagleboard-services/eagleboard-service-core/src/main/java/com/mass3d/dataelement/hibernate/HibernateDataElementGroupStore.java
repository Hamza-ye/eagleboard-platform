package com.mass3d.dataelement.hibernate;

import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.dataelement.DataElementGroupStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.dataelement.DataElementGroupStore" )
public class HibernateDataElementGroupStore
    extends HibernateIdentifiableObjectStore<DataElementGroup>
    implements DataElementGroupStore
{
    public HibernateDataElementGroupStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, DataElementGroup.class, currentUserService, aclService, false );
    }
}
