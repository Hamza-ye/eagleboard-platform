package com.mass3d.node.transformers;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import com.mass3d.node.Node;
import com.mass3d.node.NodeTransformer;
import com.mass3d.node.types.SimpleNode;
import com.mass3d.schema.Property;
import com.mass3d.schema.PropertyType;
import org.springframework.stereotype.Component;

@Component
public class SizeNodeTransformer implements NodeTransformer
{
    @Override
    public String name()
    {
        return "size";
    }

    @Override
    public Node transform( Node node, List<String> args )
    {
        checkNotNull( node );
        checkNotNull( node.getProperty() );

        Property property = node.getProperty();

        if ( property.isCollection() )
        {
            return new SimpleNode( property.getCollectionName(), node.getChildren().size(), property.isAttribute() );
        }
        else if ( property.is( PropertyType.TEXT ) )
        {
            return new SimpleNode( property.getName(), ((String) ((SimpleNode) node).getValue()).length(), property.isAttribute() );
        }
        else if ( property.is( PropertyType.INTEGER ) || property.is( PropertyType.NUMBER ) )
        {
            return new SimpleNode( property.getName(), ((SimpleNode) node).getValue(), property.isAttribute() );
        }

        return node;
    }
}
