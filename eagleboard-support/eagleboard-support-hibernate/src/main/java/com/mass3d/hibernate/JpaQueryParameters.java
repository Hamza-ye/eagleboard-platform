package com.mass3d.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JpaQueryParameters<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    // pagination
    private int maxResults ;

    private int firstResult ;

    private int pageSize;

    // query properties
    private boolean caseSensitive = true;

    private boolean useDistinct = false;

    private Boolean cacheable;

    // select attributes

    private List<Function<Root<T>, Predicate>> predicates = new ArrayList<>();

    private List<Function<Root<T>, Order>> orders = new ArrayList<>();

    private List<Function<Root<T>, Expression<Long>>> countExpressions = new ArrayList<>();

    protected Class<?> clazz;

    private boolean withSharing = false;

    public JpaQueryParameters()
    {
        firstResult = -1;
        maxResults = -1;
    }

    // -----------------------------
    // Supporting methods
    // -----------------------------

    public JpaQueryParameters<T> addPredicate( Function<Root<T>, Predicate> predicate )
    {
        predicates.add( predicate );
        return this;
    }

    public JpaQueryParameters<T> addPredicates( List<Function<Root<T>, Predicate>> predicates )
    {
        this.predicates.addAll( predicates );
        return this;
    }

    public JpaQueryParameters<T> addOrder( Function<Root<T>, Order> order )
    {
        orders.add( order );
        return this;
    }

    public JpaQueryParameters<T> count( Function<Root<T>, Expression<Long>> countExpression )
    {
        countExpressions.add( countExpression );
        return this;
    }

    public boolean hasFirstResult()
    {
        return firstResult > -1 ;
    }

    public boolean hasMaxResult()
    {
        return maxResults > -1;
    }
    
    public boolean isCacheable( boolean defaultValue )
    {
        return cacheable != null ? cacheable : defaultValue;
    }

    // -----------------------------
    // Getters & Setters
    // -----------------------------

    public Class<?> getClazz()
    {
        return clazz;
    }

    public int getMaxResults()
    {
        return maxResults >= 0 ? maxResults : 0;
    }

    public JpaQueryParameters<T> setMaxResults( int maxResults )
    {
        this.maxResults = maxResults;
        return this;
    }

    public int getFirstResult()
    {
        return firstResult;
    }

    public JpaQueryParameters<T> setFirstResult( int firstResult )
    {
        this.firstResult = firstResult;
        return this;
    }

    public int getPageSize()
    {
        return this.pageSize;
    }

    public void setPageSize( int pageSize )
    {
        this.pageSize = pageSize;
    }

    public boolean isCaseSensitive()
    {
        return this.caseSensitive;
    }

    public JpaQueryParameters<T> setCaseSensitive( boolean caseSensitive )
    {
        this.caseSensitive = caseSensitive;
        return this;
    }

    public boolean isUseDistinct()
    {
        return this.useDistinct;
    }

    public JpaQueryParameters<T> setUseDistinct( boolean useDistinct )
    {
        this.useDistinct = useDistinct;
        return this;
    }

    public List<Function<Root<T>, Predicate>> getPredicates()
    {
        return predicates;
    }

    public void setPredicates( List<Function<Root<T>, Predicate>> predicates )
    {
        this.predicates = predicates;
    }

    public List<Function<Root<T>, Order>> getOrders()
    {
        return orders;
    }

    public void setOrders( List<Function<Root<T>, Order>> orders )
    {
        this.orders = orders;
    }

    public Boolean isCacheable()
    {
        return this.cacheable;
    }

    public JpaQueryParameters<T> setCacheable( boolean cachable )
    {
        this.cacheable = cachable;
        return this;
    }

    public boolean isWithSharing()
    {
        return withSharing;
    }

    public JpaQueryParameters<T> setWithSharing( boolean withSharing )
    {
        this.withSharing = withSharing;
        return this;
    }

    public List<Function<Root<T>, Expression<Long>>> getCountExpressions()
    {
        return countExpressions;
    }

    public JpaQueryParameters<T> setCountExpressions( List<Function<Root<T>, Expression<Long>>> countExpressions )
    {
        this.countExpressions = countExpressions;
        return this;
    }
}
