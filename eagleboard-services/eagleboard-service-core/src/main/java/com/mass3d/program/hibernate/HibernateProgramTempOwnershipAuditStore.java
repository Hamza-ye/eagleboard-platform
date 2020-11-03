package com.mass3d.program.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.SessionFactory;
import com.mass3d.hibernate.HibernateGenericStore;
import com.mass3d.hibernate.JpaQueryParameters;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramTempOwnershipAudit;
import com.mass3d.program.ProgramTempOwnershipAuditQueryParams;
import com.mass3d.program.ProgramTempOwnershipAuditStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.program.ProgramTempOwnershipAuditStore" )
public class HibernateProgramTempOwnershipAuditStore
    extends HibernateGenericStore<ProgramTempOwnershipAudit>
    implements ProgramTempOwnershipAuditStore
{
    public HibernateProgramTempOwnershipAuditStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher )
    {
        super( sessionFactory, jdbcTemplate, publisher, ProgramTempOwnershipAudit.class, false );
    }

    // -------------------------------------------------------------------------
    // ProgramTempOwnershipAuditStore implementation
    // -------------------------------------------------------------------------

    @Override
    public void addProgramTempOwnershipAudit( ProgramTempOwnershipAudit programTempOwnershipAudit )
    {
        sessionFactory.getCurrentSession().save( programTempOwnershipAudit );
    }

    @Override
    public void deleteProgramTempOwnershipAudit( Program program )
    {
        String hql = "delete ProgramTempOwnershipAudit where program = :program";
        sessionFactory.getCurrentSession().createQuery( hql ).setParameter( "program", program ).executeUpdate();
    }

    @Override
    public List<ProgramTempOwnershipAudit> getProgramTempOwnershipAudits( ProgramTempOwnershipAuditQueryParams params )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<ProgramTempOwnershipAudit> jpaParameters = newJpaParameters()
            .addPredicates( getProgramTempOwnershipAuditPredicates( params, builder ) )
            .addOrder( root -> builder.desc( root.get( "created" ) ) );

        if( !params.isSkipPaging() )
        {
            jpaParameters.setFirstResult( params.getFirst() ).setMaxResults( params.getMax() );
        }

        return getList( builder, jpaParameters );
    }

    @Override
    public int getProgramTempOwnershipAuditsCount( ProgramTempOwnershipAuditQueryParams params )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getCount( builder, newJpaParameters()
            .addPredicates( getProgramTempOwnershipAuditPredicates( params, builder ) )
            .count( root -> builder.countDistinct( root.get( "id" ) ) ) ).intValue();
    }

    private List<Function<Root<ProgramTempOwnershipAudit>, Predicate>> getProgramTempOwnershipAuditPredicates( ProgramTempOwnershipAuditQueryParams params, CriteriaBuilder builder )
    {
        List<Function<Root<ProgramTempOwnershipAudit>, Predicate>> predicates = new ArrayList<>();

        if ( params.hasUsers() )
        {
            predicates.add( root -> root.get( "accessedBy" ).in( params.getUsers() ) );
        }

        if ( params.hasStartDate() )
        {
            predicates.add( root -> builder.greaterThanOrEqualTo( root.get("created" ), params.getStartDate() ) );
        }

        if ( params.hasEndDate() )
        {
            predicates.add( root -> builder.lessThanOrEqualTo( root.get( "created" ), params.getEndDate() ) );
        }

        if ( params.hasPrograms() )
        {
            predicates.add( root -> root.get( "program" ).in( params.getPrograms() ) );
        }

        return predicates;
    }
}