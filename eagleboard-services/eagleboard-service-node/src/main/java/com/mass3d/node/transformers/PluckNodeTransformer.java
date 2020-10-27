package com.mass3d.node.transformers;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.node.Node;
import com.mass3d.node.NodeTransformer;
import com.mass3d.node.types.CollectionNode;
import com.mass3d.node.types.SimpleNode;
import com.mass3d.schema.Property;
import org.springframework.stereotype.Component;

@Component
public class PluckNodeTransformer implements NodeTransformer
{
    @Override
    public String name()
    {
        return "pluck";
    }

    @Override
    public Node transform( Node node, List<String> args )
    {
        checkNotNull( node );
        checkNotNull( node.getProperty() );

        Property property = node.getProperty();

        if ( property.isCollection() )
        {
            final String fieldName = ( args == null || args.isEmpty() ) ? null : StringUtils
                .defaultIfEmpty( args.get( 0 ), null );

            final CollectionNode collectionNode = new CollectionNode( node.getName(), node.getUnorderedChildren().size() );
            collectionNode.setNamespace( node.getNamespace() );

            for ( final Node objectNode : node.getUnorderedChildren() )
            {
                for ( final Node fieldNode : objectNode.getUnorderedChildren() )
                {
                    if ( fieldNode instanceof SimpleNode && ( fieldName == null || fieldName.equals( fieldNode.getName() ) ) )
                    {
                        final SimpleNode childNode = new SimpleNode( fieldNode.getName(), ( (SimpleNode) fieldNode ).getValue() );
                        childNode.setProperty( collectionNode.getProperty() );
                        collectionNode.addChild( childNode );

                        break;
                    }
                }
            }

            return collectionNode;
        }

        return node;
    }
}
