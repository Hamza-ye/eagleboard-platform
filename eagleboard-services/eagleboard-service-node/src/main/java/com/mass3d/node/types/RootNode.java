package com.mass3d.node.types;

import java.util.Objects;
import com.mass3d.node.Node;
import com.mass3d.node.config.Config;

public class RootNode extends ComplexNode
{
    private String defaultNamespace;

    private final Config config = new Config();

    public RootNode( String name )
    {
        super( name );
    }

    public RootNode( Node node )
    {
        super( node.getName() );
        setNamespace( node.getNamespace() );
        setComment( node.getComment() );
        addChildren( node.getChildren() );
    }

    public String getDefaultNamespace()
    {
        return defaultNamespace;
    }

    public void setDefaultNamespace( String defaultNamespace )
    {
        this.defaultNamespace = defaultNamespace;
    }

    public Config getConfig()
    {
        return config;
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
        RootNode rootNode = (RootNode) o;
        return Objects.equals( defaultNamespace, rootNode.defaultNamespace ) &&
            config.equals( rootNode.config );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( super.hashCode(), defaultNamespace, config );
    }
}
