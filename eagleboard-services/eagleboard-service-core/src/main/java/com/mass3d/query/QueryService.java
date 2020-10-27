package com.mass3d.query;

import com.mass3d.common.IdentifiableObject;
import com.mass3d.query.Junction.Type;

import java.util.List;

public interface QueryService
{
    /**
     * Return objects matching given query, T typed according to QueryEngine
     * implementation.
     *
     * @param query Query instance to use
     * @return Matching objects
     */
    List<? extends IdentifiableObject> query(Query query);

    /**
     * Return objects matching given query, T typed according to QueryEngine
     * implementation.
     *
     * @param query       Query instance to use
     * @param transformer ResultTransformer to use for mutating the result
     * @return Matching objects
     */
    @SuppressWarnings( "rawtypes" )
    List<? extends IdentifiableObject> query(Query query, ResultTransformer transformer);

    /**
     * Returns how many objects matches the given query.
     *
     * @param query Query instance to use
     * @return N number of matching objects
     */
    int count(Query query);

    /**
     * Create a query instance from a given set of filters (property:operator:value), and
     * a list of orders.
     *
     * @param klass        Type of object you want to query
     * @param filters      List of filters to use as basis for query instance
     * @param orders       List of orders to use for query
     * @param rootJunction Root junction (defaults to AND)
     * @return New query instance using provided filters/orders
     */
    Query getQueryFromUrl(Class<?> klass, List<String> filters, List<Order> orders,
        Pagination pagination, Junction.Type rootJunction) throws QueryParserException;

    Query getQueryFromUrl(Class<?> klass, List<String> filters, List<Order> orders,
        Pagination pagination) throws QueryParserException;

    Query getQueryFromUrl(Class<?> klass, List<String> filters, List<Order> orders) throws QueryParserException;

    Query getQueryFromUrl(Class<?> klass, List<String> filters, List<Order> orders,
        Pagination pagination, Type rootJunction, boolean restrictToCaptureScope)
        throws QueryParserException;
}
