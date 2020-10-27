package com.mass3d.query;

import com.mass3d.query.Junction.Type;
import com.mass3d.schema.Property;
import com.mass3d.schema.Schema;

import java.util.List;

public interface QueryParser
{
    /**
     * Parses filter expressions, need 2 or 3 components depending on operator.
     * i.e. for null you can use "name:null" which checks to see if property name is null
     * i.e. for eq you can use "name:eq:ANC" which check to see if property name is equal to ANC
     * <p>
     * The general syntax is "propertyName:operatorName:<Value to check against if needed>"
     *
     * @param klass        Class type to query for
     * @param filters      List of filters to add to Query
     * @param rootJunction Root junction to use (defaults to AND)
     * @return Query instance based on Schema of klass and filters list
     * @throws QueryParserException
     */
    Query parse(Class<?> klass, List<String> filters, Junction.Type rootJunction) throws QueryParserException;

    Query parse(Class<?> klass, List<String> filters) throws QueryParserException;

    Property getProperty(Schema schema, String path) throws QueryParserException;

    Restriction getRestriction(Schema schema, String path, String operator, Object arg) throws QueryParserException;

    Query parse(Class<?> klass, List<String> filters, Type rootJunction,
        boolean restrictToCaptureScope)
        throws QueryParserException;
}
