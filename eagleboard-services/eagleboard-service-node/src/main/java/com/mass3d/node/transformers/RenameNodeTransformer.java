package com.mass3d.node.transformers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import com.mass3d.node.AbstractNode;
import com.mass3d.node.Node;
import com.mass3d.node.NodeTransformer;
import org.springframework.stereotype.Component;

@Component
public class RenameNodeTransformer implements NodeTransformer
{
    @Override
    public String name()
    {
        return "rename";
    }

    @Override
    public Node transform( Node node, List<String> args )
    {
        checkNotNull( node );
        checkArgument( args.size() > 0, "rename requires a name parameter, .e.g: property~rename(newName)" );
        ((AbstractNode) node).setName( args.get( 0 ) );

        return node;
    }
}
