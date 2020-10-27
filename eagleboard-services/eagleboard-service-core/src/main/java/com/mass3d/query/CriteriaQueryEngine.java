package com.mass3d.query;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.hibernate.InternalHibernateGenericStore;
import com.mass3d.query.planner.QueryPlan;
import com.mass3d.query.planner.QueryPlanner;
import com.mass3d.schema.Schema;
import com.mass3d.user.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of QueryEngine that uses Hibernate Criteria and
 * supports idObjects only.
 *
 */
@Component
public class CriteriaQueryEngine<T extends IdentifiableObject>
    implements QueryEngine<T>
{
    private final CurrentUserService currentUserService;

    private final QueryPlanner queryPlanner;

    private final List<InternalHibernateGenericStore<T>> hibernateGenericStores;

    private Map<Class<?>, InternalHibernateGenericStore<T>> stores = new HashMap<>();

    @Autowired
    public CriteriaQueryEngine( CurrentUserService currentUserService, QueryPlanner queryPlanner,
        List<InternalHibernateGenericStore<T>> hibernateGenericStores )
    {
        checkNotNull( currentUserService );
        checkNotNull( queryPlanner );
        checkNotNull( hibernateGenericStores );

        this.currentUserService = currentUserService;
        this.queryPlanner = queryPlanner;
        this.hibernateGenericStores = hibernateGenericStores;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public List<T> query( Query query )
    {
        Schema schema = query.getSchema();
        InternalHibernateGenericStore<?> store = getStore( (Class<? extends IdentifiableObject>) schema.getKlass() );

        if ( store == null )
        {
            return new ArrayList<>();
        }

        if ( query.getUser() == null )
        {
            query.setUser( currentUserService.getCurrentUser() );
        }

        if ( !query.isPlannedQuery() )
        {
            QueryPlan queryPlan = queryPlanner.planQuery( query, true );
            query = queryPlan.getPersistedQuery();
        }

        DetachedCriteria detachedCriteria = buildCriteria( store.getSharingDetachedCriteria( query.getUser() ), query );
        Criteria criteria = store.getCriteria();

        if ( criteria == null )
        {
            return new ArrayList<>();
        }

        criteria.setFirstResult( query.getFirstResult() );
        criteria.setMaxResults( query.getMaxResults() );

        for ( Order order : query.getOrders() )
        {
            criteria.addOrder( getHibernateOrder( order ) );
        }

        return criteria.add( Subqueries.propertyIn( "id", detachedCriteria ) ).list();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public int count( Query query )
    {
        Schema schema = query.getSchema();
        InternalHibernateGenericStore<?> store = getStore( (Class<? extends IdentifiableObject>) schema.getKlass() );

        if ( store == null )
        {
            return 0;
        }

        if ( query.getUser() == null )
        {
            query.setUser( currentUserService.getCurrentUser() );
        }

        if ( !query.isPlannedQuery() )
        {
            QueryPlan queryPlan = queryPlanner.planQuery( query, true );
            query = queryPlan.getPersistedQuery();
        }

        DetachedCriteria detachedCriteria = buildCriteria( store.getSharingDetachedCriteria( query.getUser() ), query );
        Criteria criteria = store.getCriteria();

        if ( criteria == null )
        {
            return 0;
        }

        return ((Number) criteria.add( Subqueries.propertyIn( "id", detachedCriteria ) )
            .setProjection( Projections.countDistinct( "id" ) )
            .uniqueResult()).intValue();
    }

    private DetachedCriteria buildCriteria( DetachedCriteria detachedCriteria, Query query )
    {
        if ( query.isEmpty() )
        {
            return detachedCriteria.setProjection(
                Projections.distinct( Projections.id() )
            );
        }

        org.hibernate.criterion.Junction junction = getHibernateJunction( query.getRootJunctionType() );
        detachedCriteria.add( junction );

        for ( com.mass3d.query.Criterion criterion : query.getCriterions() )
        {
            addCriterion( junction, criterion );
        }

        query.getAliases().forEach( alias -> detachedCriteria.createAlias( alias, alias ) );

        return detachedCriteria.setProjection(
            Projections.distinct( Projections.id() )
        );
    }

    private void addCriterion( org.hibernate.criterion.Junction criteria, com.mass3d.query.Criterion criterion )
    {
        if ( Restriction.class.isInstance( criterion ) )
        {
            Restriction restriction = (Restriction) criterion;
            Criterion hibernateCriterion = getHibernateCriterion( restriction );

            if ( hibernateCriterion != null )
            {
                criteria.add( hibernateCriterion );
            }
        }
        else if ( Junction.class.isInstance( criterion ) )
        {
            org.hibernate.criterion.Junction junction = null;

            if ( Disjunction.class.isInstance( criterion ) )
            {
                junction = Restrictions.disjunction();
            }
            else if ( Conjunction.class.isInstance( criterion ) )
            {
                junction = Restrictions.conjunction();
            }

            criteria.add( junction );

            for ( com.mass3d.query.Criterion c : ((Junction) criterion).getCriterions() )
            {
                addJunction( junction, c );
            }
        }
    }

    private void addJunction( org.hibernate.criterion.Junction junction, com.mass3d.query.Criterion criterion )
    {
        if ( Restriction.class.isInstance( criterion ) )
        {
            Restriction restriction = (Restriction) criterion;
            Criterion hibernateCriterion = getHibernateCriterion( restriction );

            if ( hibernateCriterion != null )
            {
                junction.add( hibernateCriterion );
            }
        }
        else if ( Junction.class.isInstance( criterion ) )
        {
            org.hibernate.criterion.Junction j = null;

            if ( Disjunction.class.isInstance( criterion ) )
            {
                j = Restrictions.disjunction();
            }
            else if ( Conjunction.class.isInstance( criterion ) )
            {
                j = Restrictions.conjunction();
            }

            junction.add( j );

            for ( com.mass3d.query.Criterion c : ((Junction) criterion).getCriterions() )
            {
                addJunction( junction, c );
            }
        }
    }

    private org.hibernate.criterion.Junction getHibernateJunction( Junction.Type type )
    {
        switch ( type )
        {
            case AND:
                return Restrictions.conjunction();
            case OR:
                return Restrictions.disjunction();
        }

        return Restrictions.conjunction();
    }

    private Criterion getHibernateCriterion( Restriction restriction )
    {
        if ( restriction == null || restriction.getOperator() == null )
        {
            return null;
        }

        return restriction.getOperator().getHibernateCriterion( restriction.getQueryPath() );
    }

    public org.hibernate.criterion.Order getHibernateOrder( Order order )
    {
        if ( order == null || order.getProperty() == null || !order.getProperty().isPersisted() || !order.getProperty().isSimple() )
        {
            return null;
        }

        org.hibernate.criterion.Order criteriaOrder;
        String fieldName = order.getProperty().getFieldName();

        if ( fieldName == null )
        {
            fieldName = order.getProperty().getName();
        }

        if ( order.isAscending() )
        {
            criteriaOrder = org.hibernate.criterion.Order.asc( fieldName );
        }
        else
        {
            criteriaOrder = org.hibernate.criterion.Order.desc( fieldName );
        }

        if ( order.isIgnoreCase() )
        {
            criteriaOrder.ignoreCase();
        }

        return criteriaOrder;
    }

    private void initStoreMap()
    {
        if ( !stores.isEmpty() )
        {
            return;
        }

        for ( InternalHibernateGenericStore<T> store : hibernateGenericStores )
        {
            stores.put( store.getClazz(), store );
        }
    }

    private InternalHibernateGenericStore<?> getStore( Class<? extends IdentifiableObject> klass )
    {
        initStoreMap();
        return stores.get( klass );
    }
}
