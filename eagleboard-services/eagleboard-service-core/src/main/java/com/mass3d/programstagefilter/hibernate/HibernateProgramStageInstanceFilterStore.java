package com.mass3d.programstagefilter.hibernate;

import java.util.List;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.programstagefilter.ProgramStageInstanceFilter;
import com.mass3d.programstagefilter.ProgramStageInstanceFilterStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.programstagefilter.ProgramStageInstanceFilterStore" )
public class HibernateProgramStageInstanceFilterStore
    extends HibernateIdentifiableObjectStore<ProgramStageInstanceFilter>
    implements ProgramStageInstanceFilterStore
{

    public HibernateProgramStageInstanceFilterStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, ProgramStageInstanceFilter.class, currentUserService, aclService, false );
    }

    @Override
    public List<ProgramStageInstanceFilter> getByProgram( String program )
    {
        String hql = "from ProgramStageInstanceFilter psif where psif.program =:program";
        return getQuery( hql ).setParameter( "program", program ).getResultList();
    }

}
