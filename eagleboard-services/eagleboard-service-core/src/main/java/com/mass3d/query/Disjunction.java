package com.mass3d.query;

import com.mass3d.schema.Schema;

public class Disjunction extends Junction
{
    public Disjunction( Schema schema )
    {
        super( schema, Type.OR );
    }

    @Override
    public String toString()
    {
        return "OR[" + criterions + "]";
    }
}
