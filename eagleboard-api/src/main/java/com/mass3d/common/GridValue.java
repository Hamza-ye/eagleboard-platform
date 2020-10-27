package com.mass3d.common;

import java.util.HashMap;
import java.util.Map;

public class GridValue
{
    private Object value;
    
    private Map<Object, Object> attributes = new HashMap<>();

    // ---------------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------------

    public GridValue( Object value )
    {
        this.value = value;
    }
    
    public GridValue( Object value, Map<Object, Object> attributes )
    {
        this.value = value;
        this.attributes = attributes;
    }
    
    // ---------------------------------------------------------------------
    // Logic
    // ---------------------------------------------------------------------

    public void attr( Object attribute, Object value )
    {
        this.attributes.put( attribute, value );
    }
    
    public Object attr( Object attribute )
    {
        return this.attributes.get( attribute );
    }
    
    public boolean hasAttr( Object attribute )
    {
        return this.attributes.containsKey( attribute );
    }

    @Override
    public String toString()
    {
        return value != null ? value.toString() : null;
    }

    // ---------------------------------------------------------------------
    // Get and set methods
    // ---------------------------------------------------------------------

    public Object getValue()
    {
        return value;
    }

    public void setValue( Object value )
    {
        this.value = value;
    }

    public Map<Object, Object> getAttributes()
    {
        return attributes;
    }

    public void setAttributes( Map<Object, Object> attributes )
    {
        this.attributes = attributes;
    }
}
