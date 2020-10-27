package com.mass3d.node.transformers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import com.mass3d.common.Pager;
import com.mass3d.common.PagerUtils;
import com.mass3d.node.AbstractNode;
import com.mass3d.node.Node;
import com.mass3d.node.NodeTransformer;
import com.mass3d.schema.Property;
import org.springframework.stereotype.Component;

@Component
public class PagingNodeTransformer implements NodeTransformer
{
    @Override
    public String name()
    {
        return "paging";
    }

    @Override
    public Node transform( Node node, List<String> args )
    {
        checkNotNull( node );
        checkArgument( !args.isEmpty(), "paging requires at least one parameter, .e.g: property|paging(page, pageSize)" );
        checkNotNull( node.getProperty() );

        int page;
        int pageSize = Pager.DEFAULT_PAGE_SIZE;

        try
        {
            page = Integer.parseInt( args.get( 0 ) );

            if ( args.size() > 1 )
            {
                pageSize = Integer.parseInt( args.get( 1 ) );
            }
        }
        catch ( NumberFormatException ex )
        {
            return node;
        }

        Property property = node.getProperty();

        if ( property.isCollection() )
        {
            Pager pager = new Pager( page, node.getChildren().size(), pageSize );
            ((AbstractNode) node).setChildren( PagerUtils.pageCollection( node.getChildren(), pager ) );

            return node;
        }

        return node;
    }
}
