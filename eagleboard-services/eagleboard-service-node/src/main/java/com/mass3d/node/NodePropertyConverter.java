package com.mass3d.node;

import com.mass3d.schema.Property;

public interface NodePropertyConverter
{
    /**
     * @return Public/external name of this transformer.
     */
    String name();

    /**
     * @param property Property instance belonging to value
     * @param value    Actual value to transform
     * @return true of false depending on support
     */
    boolean canConvertTo(Property property, Object value);

    /**
     * @param property Property instance belonging to value
     * @param value    Actual value to transform
     * @return Value transformed to a Node
     */
    Node convertTo(Property property, Object value);
}