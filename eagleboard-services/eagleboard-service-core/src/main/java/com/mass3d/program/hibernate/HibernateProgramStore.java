package com.mass3d.program.hibernate;

import com.google.common.collect.Lists;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.dataentryform.DataEntryForm;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramStore;
import com.mass3d.program.ProgramType;
import com.mass3d.security.acl.AclService;
import com.mass3d.trackedentity.TrackedEntityType;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.program.ProgramStore" )
public class HibernateProgramStore
    extends HibernateIdentifiableObjectStore<Program>
    implements ProgramStore
{
    public HibernateProgramStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, Program.class, currentUserService, aclService, true );

    }
    // -------------------------------------------------------------------------
    // Implemented methods
    // -------------------------------------------------------------------------

    @Override
    public List<Program> getByType( ProgramType type )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
               .addPredicate( root ->  builder.equal( root.get( "programType" ), type )));
    }

    @Override
    public List<Program> get( OrganisationUnit organisationUnit )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.join( "organisationUnits" ).get( "id" ), organisationUnit.getId() ) ) );
    }

    @Override
    public List<Program> getByTrackedEntityType( TrackedEntityType trackedEntityType )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "trackedEntityType" ), trackedEntityType ) ) );
    }

    @Override
    public List<Program> getByDataEntryForm( DataEntryForm dataEntryForm )
    {
        if ( dataEntryForm == null )
        {
            return Lists.newArrayList();
        }

        final String hql = "from Program p where p.dataEntryForm = :dataEntryForm";

        return getQuery( hql ).setParameter( "dataEntryForm", dataEntryForm ).list();
    }
}
