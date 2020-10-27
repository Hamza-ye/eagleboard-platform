package com.mass3d.dataset.hibernate;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Lists;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.dataset.DataSet;
import com.mass3d.dataset.DataSetStore;
import com.mass3d.hibernate.JpaQueryParameters;
import com.mass3d.period.PeriodService;
import com.mass3d.period.PeriodType;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository ( "com.mass3d.dataset.DataSetStore" )
public class HibernateDataSetStore
    extends HibernateIdentifiableObjectStore<DataSet>
    implements DataSetStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final PeriodService periodService;

    public HibernateDataSetStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService,
        PeriodService periodService )
    {
        super( sessionFactory, jdbcTemplate, publisher, DataSet.class, currentUserService, aclService, true );

        checkNotNull( periodService );

        this.periodService = periodService;
    }

    // -------------------------------------------------------------------------
    // DataSet
    // -------------------------------------------------------------------------

    @Override
    public void save( DataSet dataSet )
    {
        PeriodType periodType = periodService.reloadPeriodType( dataSet.getPeriodType() );

        dataSet.setPeriodType( periodType );

        super.save( dataSet );
    }

    @Override
    public void update( DataSet dataSet )
    {
        PeriodType periodType = periodService.reloadPeriodType( dataSet.getPeriodType() );

        dataSet.setPeriodType( periodType );

        super.update( dataSet );
    }

    @Override
    public List<DataSet> getDataSetsByPeriodType( PeriodType periodType )
    {
        PeriodType refreshedPeriodType = periodService.reloadPeriodType( periodType );

        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<DataSet> parameters = newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "periodType" ), refreshedPeriodType ) ) ;

        return getList( builder, parameters );
    }

    @Override
    public List<DataSet> getDataSetsWithoutTodoTasks() {
        String hql = "from DataSet d where size(d.sources) = 0";

        return getQuery(hql).setCacheable(true).list();
    }

    @Override
    public List<DataSet> getDataSetsWithTodoTasks() {
        String hql = "from DataSet d where size(d.sources) > 0";

        return getQuery(hql).setCacheable(true).list();
    }
    
//    @Override
//    public List<DataSet> getDataSetsByDataEntryForm( DataEntryForm dataEntryForm )
//    {
//        if ( dataEntryForm == null )
//        {
//            return Lists.newArrayList();
//        }
//
//        final String hql = "from DataSet d where d.dataEntryForm = :dataEntryForm";
//
//        Query<DataSet> query = getQuery( hql );
//
//        return query.setParameter( "dataEntryForm", dataEntryForm ).list();
//    }
}
