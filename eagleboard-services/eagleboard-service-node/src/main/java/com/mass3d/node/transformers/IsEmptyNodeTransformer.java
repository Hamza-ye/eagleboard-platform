package com.mass3d.node.transformers;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import com.mass3d.node.Node;
import com.mass3d.node.NodeTransformer;
import com.mass3d.node.types.SimpleNode;
import com.mass3d.schema.Property;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class IsEmptyNodeTransformer implements NodeTransformer
{
    @Override
    public String name()
    {
        return "isEmpty";
    }

    @Override
    public Node transform( Node node, List<String> args )
    {
        checkNotNull( node );
        checkNotNull( node.getProperty() );

        Property property = node.getProperty();

        if ( property.isCollection() )
        {
            return new SimpleNode( property.getCollectionName(), node.getChildren().isEmpty(), property.isAttribute() );
        }
        else if ( property.isSimple() )
        {
            return new SimpleNode( property.getName(), StringUtils.isEmpty( ((SimpleNode) node).getValue() ), property.isAttribute() );
        }

        return node;
    }
}
