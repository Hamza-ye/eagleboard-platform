package com.mass3d.node.types;

import java.util.Objects;
import com.mass3d.node.AbstractNode;
import com.mass3d.node.Node;
import com.mass3d.node.NodeType;
import com.mass3d.node.exception.InvalidTypeException;
import com.mass3d.schema.Property;

public class SimpleNode
    extends AbstractNode
{
    /**
     * Value of this node.
     */
    private final Object value;

    /**
     * Is this node considered a attribute.
     */
    private boolean attribute;

    public SimpleNode( String name, Object value )
    {
        super( name, NodeType.SIMPLE );
        this.value = value;
        this.attribute = false;
    }

    public SimpleNode( String name, Property property, Object value )
    {
        super( name, NodeType.SIMPLE );
        this.value = value;
        this.attribute = property.isAttribute();
        this.namespace = property.getNamespace();
        this.property = property;
    }

    public SimpleNode( String name, Object value, boolean attribute )
    {
        this( name, value );
        this.attribute = attribute;
    }

    public Object getValue()
    {
        return value;
    }

    public boolean isAttribute()
    {
        return attribute;
    }

    public void setAttribute( boolean attribute )
    {
        this.attribute = attribute;
    }

    @Override
    public <T extends Node> T addChild( T child )
    {
        throw new InvalidTypeException();
    }

    @Override
    public <T extends Node> void addChildren( Iterable<T> children )
    {
        throw new InvalidTypeException();
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        if ( !super.equals( o ) )
        {
            return false;
        }

        SimpleNode that = (SimpleNode) o;

        return attribute == that.attribute && Objects.equals( value, that.value );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( super.hashCode(), value );
    }
}
