package com.mass3d.dataelement.hibernate;

import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.dataelement.DataElementGroupSet;
import com.mass3d.dataelement.DataElementGroupSetStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.dataelement.DataElementGroupSetStore" )
public class HibernateDataElementGroupSetStore
    extends
    HibernateIdentifiableObjectStore<DataElementGroupSet>
    implements
    DataElementGroupSetStore
{
    public HibernateDataElementGroupSetStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, DataElementGroupSet.class, currentUserService, aclService, false );
    }
}
