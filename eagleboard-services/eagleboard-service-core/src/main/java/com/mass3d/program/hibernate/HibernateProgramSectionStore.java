package com.mass3d.program.hibernate;

import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.program.ProgramSection;
import com.mass3d.program.ProgramSectionStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.program.ProgramSectionStore" )
public class HibernateProgramSectionStore
    extends HibernateIdentifiableObjectStore<ProgramSection>
    implements ProgramSectionStore
{
    public HibernateProgramSectionStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, ProgramSection.class, currentUserService, aclService, true );
    }
}