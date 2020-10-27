package com.mass3d.query;

import com.mass3d.schema.Schema;

public class Conjunction extends Junction
{
    public Conjunction( Schema schema )
    {
        super( schema, Type.AND );
    }

    @Override
    public String toString()
    {
        return "AND[" + criterions + "]";
    }
}
