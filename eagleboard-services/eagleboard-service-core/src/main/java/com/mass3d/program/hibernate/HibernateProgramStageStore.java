package com.mass3d.program.hibernate;

import com.google.common.collect.Lists;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.dataentryform.DataEntryForm;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramStage;
import com.mass3d.program.ProgramStageStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.program.ProgramStageStore" )
public class HibernateProgramStageStore
    extends HibernateIdentifiableObjectStore<ProgramStage>
    implements ProgramStageStore
{
    public HibernateProgramStageStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, ProgramStage.class, currentUserService, aclService, true );
    }
    // -------------------------------------------------------------------------
    // Implemented methods
    // -------------------------------------------------------------------------

    @Override
    public ProgramStage getByNameAndProgram( String name, Program program )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getSingleResult( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "name" ), name ) )
            .addPredicate( root -> builder.equal( root.get( "program" ), program ) ) );
    }

    @Override
    public List<ProgramStage> getByDataEntryForm( DataEntryForm dataEntryForm )
    {
        if ( dataEntryForm == null )
        {
            return Lists.newArrayList();
        }

        final String hql = "from ProgramStage p where p.dataEntryForm = :dataEntryForm";

        return getQuery( hql ).setParameter( "dataEntryForm", dataEntryForm ).list();
    }
}
