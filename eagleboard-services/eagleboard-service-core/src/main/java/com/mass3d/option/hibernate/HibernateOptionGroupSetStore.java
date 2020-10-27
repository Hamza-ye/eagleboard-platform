package com.mass3d.option.hibernate;

import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.option.OptionGroupSet;
import com.mass3d.option.OptionGroupSetStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.option.OptionGroupSetStore" )
public class HibernateOptionGroupSetStore
    extends HibernateIdentifiableObjectStore<OptionGroupSet>
    implements OptionGroupSetStore
{
    public HibernateOptionGroupSetStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, OptionGroupSet.class, currentUserService, aclService, true );
    }
}