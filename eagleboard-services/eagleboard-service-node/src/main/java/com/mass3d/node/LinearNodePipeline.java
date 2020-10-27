package com.mass3d.node;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple linear pipeline that run transformers sequentially.
 *
 */
public class LinearNodePipeline implements NodePipeline
{
    private class NodeTransformerWithArgs
    {
        NodeTransformer transformer;
        List<String> arguments;

        NodeTransformerWithArgs( NodeTransformer transformer, List<String> arguments )
        {
            this.transformer = transformer;
            this.arguments = arguments;
        }

        Node transform( Node node )
        {
            return transformer.transform( node, arguments );
        }
    }

    private List<NodeTransformerWithArgs> nodeTransformers = new ArrayList<>();

    @Override
    public Node process( Node node )
    {
        for ( NodeTransformerWithArgs nodeTransformer : nodeTransformers )
        {
            node = nodeTransformer.transform( node );

            if ( node == null )
            {
                return null;
            }
        }

        return node;
    }

    public void addTransformer( NodeTransformer nodeTransformer )
    {
        nodeTransformers.add( new NodeTransformerWithArgs( checkNotNull( nodeTransformer ), new ArrayList<>() ) );
    }

    public void addTransformer( NodeTransformer nodeTransformer, List<String> arguments )
    {
        nodeTransformers.add( new NodeTransformerWithArgs( checkNotNull( nodeTransformer ), arguments ) );
    }
}
