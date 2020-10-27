package com.mass3d.node.types;

import com.google.common.collect.Lists;
import java.util.Objects;
import com.mass3d.node.AbstractNode;
import com.mass3d.node.NodeType;

public class CollectionNode extends AbstractNode
{
    /**
     * Should this collection act as a wrapper around its children.
     */
    private boolean wrapping = true;

    public CollectionNode( String name )
    {
        super( name, NodeType.COLLECTION );
    }

    public CollectionNode( String name, int initialChildSize )
    {
        super( name, NodeType.COLLECTION );

        if ( initialChildSize > 0 )
        {
            children = Lists.newArrayListWithCapacity( initialChildSize );
        }
    }

    public CollectionNode( String name, boolean wrapping )
    {
        super( name, NodeType.COLLECTION );
        this.wrapping = wrapping;
    }

    public boolean isWrapping()
    {
        return wrapping;
    }

    public void setWrapping( boolean wrapping )
    {
        this.wrapping = wrapping;
    }

    @Override
    public int hashCode()
    {
        return 31 * super.hashCode() + Objects.hash( wrapping );
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }

        if ( !super.equals( obj ) )
        {
            return false;
        }

        final CollectionNode other = (CollectionNode) obj;
        return Objects.equals( this.wrapping, other.wrapping );
    }
}
