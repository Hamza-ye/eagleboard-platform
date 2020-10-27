package com.mass3d.query;

import com.mass3d.query.operators.Operator;
import com.mass3d.query.planner.QueryPath;

public class Restriction implements Criterion
{
    /**
     * Path to property you want to restrict only, one first-level properties are currently supported.
     */
    private String path;

    /**
     * Operator for restriction.
     */
    private Operator operator;

    /**
     * Query Path.
     */
    private QueryPath queryPath;

    public Restriction( String path, Operator operator )
    {
        this.path = path;
        this.operator = operator;
    }

    public String getPath()
    {
        return path;
    }

    public Operator getOperator()
    {
        return operator;
    }

    public QueryPath getQueryPath()
    {
        return queryPath;
    }

    public Restriction setQueryPath( QueryPath queryPath )
    {
        this.queryPath = queryPath;
        return this;
    }

    public boolean haveQueryPath()
    {
        return queryPath != null;
    }

    @Override
    public String toString()
    {
        return "[" + path + ", op: " + operator + "]";
    }
}
