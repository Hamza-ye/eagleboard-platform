package com.mass3d.node.types;

import com.mass3d.node.AbstractNode;
import com.mass3d.node.NodeType;
import com.mass3d.schema.Property;

public class ComplexNode extends AbstractNode
{
    public ComplexNode( String name )
    {
        super( name, NodeType.COMPLEX );
    }

    public ComplexNode( Property property, SimpleNode child )
    {
        super( property.getName(), NodeType.COMPLEX, property, child );
        setNamespace( property.getNamespace() );
    }
}
