package com.mass3d.query;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.fieldfilter.Defaults;
import com.mass3d.schema.Schema;
import com.mass3d.user.User;
import org.springframework.util.StringUtils;

public class Query extends Criteria
{
    private User user;

    private String locale;

    private List<Order> orders = new ArrayList<>();

    private boolean skipPaging;

    private Integer firstResult = 0;

    private Integer maxResults = Integer.MAX_VALUE;

    private Junction.Type rootJunctionType = Junction.Type.AND;

    private boolean plannedQuery;

    private Defaults defaults = Defaults.EXCLUDE;

    private List<? extends IdentifiableObject> objects;

    public static Query from( Schema schema )
    {
        return new Query( schema );
    }

    public static Query from( Schema schema, Junction.Type rootJunction )
    {
        return new Query( schema, rootJunction );
    }

    public static Query from( Query query )
    {
        Query clone = Query.from( query.getSchema(), query.getRootJunctionType() );
        clone.setUser( query.getUser() );
        clone.setLocale( query.getLocale() );
        clone.addOrders( query.getOrders() );
        clone.setFirstResult( query.getFirstResult() );
        clone.setMaxResults( query.getMaxResults() );
        clone.add( query.getCriterions() );
        clone.setObjects( query.getObjects() );

        return clone;
    }

    private Query( Schema schema )
    {
        super( schema );
    }

    private Query( Schema schema, Junction.Type rootJunctionType )
    {
        super( schema );
        this.rootJunctionType = rootJunctionType;
    }

    public Schema getSchema()
    {
        return schema;
    }

    public boolean isEmpty()
    {
        return criterions.isEmpty() && orders.isEmpty();
    }

    public List<Order> getOrders()
    {
        return orders;
    }

    public boolean ordersPersisted()
    {
        for ( Order order : orders )
        {
            if ( order.isNonPersisted() )
            {
                return false;
            }
        }

        return true;
    }

    public void clearOrders()
    {
        orders.clear();
    }

    public User getUser()
    {
        return user;
    }

    public Query setUser( User user )
    {
        this.user = user;
        return this;
    }

    public String getLocale()
    {
        return locale;
    }

    public boolean hasLocale()
    {
        return !StringUtils.isEmpty( locale );
    }

    public void setLocale( String locale )
    {
        this.locale = locale;
    }

    public boolean isSkipPaging()
    {
        return skipPaging;
    }

    public Query setSkipPaging( boolean skipPaging )
    {
        this.skipPaging = skipPaging;
        return this;
    }

    public Integer getFirstResult()
    {
        return skipPaging ? 0 : firstResult;
    }

    public Query setFirstResult( Integer firstResult )
    {
        this.firstResult = firstResult;
        return this;
    }

    public Integer getMaxResults()
    {
        return skipPaging ? Integer.MAX_VALUE : maxResults;
    }

    public Query setMaxResults( Integer maxResults )
    {
        this.maxResults = maxResults;
        return this;
    }

    public Junction.Type getRootJunctionType()
    {
        return rootJunctionType;
    }

    public boolean isPlannedQuery()
    {
        return plannedQuery;
    }

    public Query setPlannedQuery( boolean plannedQuery )
    {
        this.plannedQuery = plannedQuery;
        return this;
    }

    public Defaults getDefaults()
    {
        return defaults;
    }

    public Query setDefaults( Defaults defaults )
    {
        this.defaults = defaults;
        return this;
    }

    public List<? extends IdentifiableObject> getObjects()
    {
        return objects;
    }

    public Query setObjects( List<? extends IdentifiableObject> objects )
    {
        this.objects = objects;
        return this;
    }

    public Query addOrder( Order... orders )
    {
        for ( Order order : orders )
        {
            if ( order != null )
            {
                this.orders.add( order );
            }
        }

        return this;
    }

    public Query addOrders( Collection<Order> orders )
    {
        this.orders.addAll( orders );
        return this;
    }

    public Disjunction addDisjunction()
    {
        Disjunction disjunction = new Disjunction( schema );
        add( disjunction );

        return disjunction;
    }

    public Disjunction disjunction()
    {
        return new Disjunction( schema );
    }

    public Conjunction addConjunction()
    {
        Conjunction conjunction = new Conjunction( schema );
        add( conjunction );

        return conjunction;
    }

    public Conjunction conjunction()
    {
        return new Conjunction( schema );
    }

    public Query forceDefaultOrder()
    {
        orders.clear();
        return setDefaultOrder();
    }

    public Query setDefaultOrder()
    {
        if ( !orders.isEmpty() )
        {
            return this;
        }

        if ( schema.havePersistedProperty( "name" ) )
        {
            addOrder( Order.iasc( schema.getPersistedProperty( "name" ) ) );
        }
        if ( schema.havePersistedProperty( "created" ) )
        {
            addOrder( Order.idesc( schema.getPersistedProperty( "created" ) ) );
        }

        return this;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "firstResult", firstResult )
            .add( "maxResults", maxResults )
            .add( "orders", orders )
            .add( "criterions", criterions )
            .toString();
    }
}
