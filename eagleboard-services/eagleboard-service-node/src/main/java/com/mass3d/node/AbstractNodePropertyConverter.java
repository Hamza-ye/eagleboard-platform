package com.mass3d.node;

import com.mass3d.schema.Property;

public abstract class AbstractNodePropertyConverter implements NodePropertyConverter
{
    @Override
    public boolean canConvertTo( Property property, Object value )
    {
        return false;
    }

    public abstract Node convertTo( Property property, Object value );
}
