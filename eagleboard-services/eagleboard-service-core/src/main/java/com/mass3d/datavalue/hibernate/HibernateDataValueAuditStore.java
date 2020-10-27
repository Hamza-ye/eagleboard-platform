package com.mass3d.datavalue.hibernate;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Lists;
import com.mass3d.todotask.TodoTask;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.SessionFactory;
import com.mass3d.common.AuditType;
import com.mass3d.dataelement.DataElement;
import com.mass3d.datavalue.DataValue;
import com.mass3d.datavalue.DataValueAudit;
import com.mass3d.datavalue.DataValueAuditStore;
import com.mass3d.hibernate.HibernateGenericStore;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.datavalue.DataValueAuditStore" )
public class HibernateDataValueAuditStore extends HibernateGenericStore<DataValueAudit>
    implements DataValueAuditStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PeriodStore periodStore;

    public HibernateDataValueAuditStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, PeriodStore periodStore )
    {
        super( sessionFactory, jdbcTemplate, publisher, DataValueAudit.class, false );

        checkNotNull( periodStore );

        this.periodStore = periodStore;
    }


    // -------------------------------------------------------------------------
    // DataValueAuditStore implementation
    // -------------------------------------------------------------------------

    @Override
    public void addDataValueAudit( DataValueAudit dataValueAudit )
    {
        getSession().save( dataValueAudit );
    }

    @Override
    public void deleteDataValueAudits( TodoTask todoTask )
    {
        String hql = "delete from DataValueAudit d where d.organisationUnit = :unit";

        getSession().createQuery( hql ).setParameter( "unit", todoTask ).executeUpdate();
    }

    @Override
    public void deleteDataValueAudits( DataElement dataElement )
    {
        String hql = "delete from DataValueAudit d where d.dataElement = :dataElement";

        getSession().createQuery( hql ).setParameter( "dataElement", dataElement ).executeUpdate();
    }

    @Override
    public List<DataValueAudit> getDataValueAudits( DataValue dataValue )
    {
        return getDataValueAudits( Lists.newArrayList( dataValue.getDataElement() ),
            Lists.newArrayList( dataValue.getPeriod() ),
            Lists.newArrayList( dataValue.getSource() ), null );
    }

    @Override
    public List<DataValueAudit> getDataValueAudits( List<DataElement> dataElements, List<Period> periods, List<TodoTask> todoTasks,
         AuditType auditType )
    {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();

        List<Function<Root<DataValueAudit>, Predicate>> predicates = getDataValueAuditPredicates( builder, dataElements,
            periods, todoTasks, auditType );

        if ( !predicates.isEmpty() )
        {
            return getList( builder, newJpaParameters()
                .addPredicate( root -> builder.and( predicates.stream().map( p -> p.apply( root ) ).collect(
                    Collectors.toList() ).toArray( new Predicate[ predicates.size() ] ) ) )
                .addOrder( root -> builder.desc( root.get( "created" ) ) ) );
        }
        else
        {
            return new ArrayList<>();
        }
    }

    @Override
    public List<DataValueAudit> getDataValueAudits( List<DataElement> dataElements, List<Period> periods, List<TodoTask> todoTasks,
        AuditType auditType, int first, int max )
    {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();

        List<Function<Root<DataValueAudit>, Predicate>> predicates = getDataValueAuditPredicates( builder, dataElements, periods, todoTasks, auditType );

        if ( !predicates.isEmpty() )
        {
            return getList( builder, newJpaParameters()
                .addPredicate( root -> builder.and( predicates.stream().map( p -> p.apply( root ) ).collect(
                    Collectors.toList() ).toArray( new Predicate[ predicates.size() ] ) ) )
                .addOrder( root -> builder.desc( root.get( "created" ) ) )
                .setFirstResult( first )
                .setMaxResults( max ) );
        }
        else
        {
            return new ArrayList<>();
        }
    }

    @Override
    public int countDataValueAudits( List<DataElement> dataElements, List<Period> periods, List<TodoTask> todoTasks,
         AuditType auditType )
    {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();

        List<Function<Root<DataValueAudit>, Predicate>> predicates = getDataValueAuditPredicates( builder, dataElements, periods, todoTasks, auditType );

        if ( !predicates.isEmpty() )
        {
            return getCount( builder, newJpaParameters()
                .addPredicate( root -> builder.and( predicates.stream().map( p -> p.apply( root ) ).collect(
                    Collectors.toList() ).toArray( new Predicate[ predicates.size() ] ) ) )
                .count( root -> builder.countDistinct( root.get( "id" ) ) ) ).intValue();

        }
        else
        {
            return 0;
        }
    }

    /**
     * Returns a list of Predicates generated from given parameters. Returns an empty list if given Period does not
     * exist in database.
     *
     * @param builder the {@link CriteriaBuilder}.
     * @param dataElements the list of data elements.
     * @param periods the list of periods.
     * @param todoTasks the list of organisation units.
     * @param auditType the audit type.
     */
    private List<Function<Root<DataValueAudit>, Predicate>> getDataValueAuditPredicates( CriteriaBuilder builder,
        List<DataElement> dataElements, List<Period> periods, List<TodoTask> todoTasks, AuditType auditType )
    {
        List<Period> storedPeriods = new ArrayList<>();

        if ( periods != null && !periods.isEmpty() )
        {
            for ( Period period : periods )
            {
                Period storedPeriod = periodStore.reloadPeriod( period );

                if ( storedPeriod != null )
                {
                    storedPeriods.add( storedPeriod );
                }
            }
        }

        List<Function<Root<DataValueAudit>, Predicate>> predicates = new ArrayList<>();

        if ( !storedPeriods.isEmpty() )
        {
            predicates.add( root -> root.get( "period" ).in( storedPeriods ) );
        }
        else if ( periods != null && !periods.isEmpty() )
        {
            return predicates;
        }

        if ( dataElements != null && !dataElements.isEmpty() )
        {
            predicates.add( root -> root.get( "dataElement" ).in( dataElements ) );
        }

        if ( todoTasks != null && !todoTasks.isEmpty() )
        {
            predicates.add( root -> root.get( "organisationUnit" ).in( todoTasks ) );
        }

//        if ( categoryOptionCombo != null )
//        {
//            predicates.add( root -> builder.equal( root.get( "categoryOptionCombo" ), categoryOptionCombo ) );
//        }
//
//        if ( attributeOptionCombo != null )
//        {
//            predicates.add( root -> builder.equal( root.get( "attributeOptionCombo" ), attributeOptionCombo ) );
//        }

        if ( auditType != null )
        {
            predicates.add( root -> builder.equal( root.get( "auditType" ), auditType ) );
        }

        return predicates;
    }
}
