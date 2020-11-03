package com.mass3d.program.hibernate;

import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.program.ProgramStageSection;
import com.mass3d.program.ProgramStageSectionStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.program.ProgramStageSectionStore" )
public class HibernateProgramStageSectionStore
    extends HibernateIdentifiableObjectStore<ProgramStageSection>
    implements ProgramStageSectionStore
{
    public HibernateProgramStageSectionStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, ProgramStageSection.class, currentUserService, aclService, true );
    }
}
