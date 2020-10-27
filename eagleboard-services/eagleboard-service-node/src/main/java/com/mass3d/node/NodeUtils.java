package com.mass3d.node;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.Pager;
import com.mass3d.node.types.ComplexNode;
import com.mass3d.node.types.RootNode;
import com.mass3d.node.types.SimpleNode;

public final class NodeUtils
{
    public static RootNode createRootNode( String name )
    {
        RootNode rootNode = new RootNode( name );
        rootNode.setDefaultNamespace( DxfNamespaces.DXF_2_0 );
        rootNode.setNamespace( DxfNamespaces.DXF_2_0 );

        return rootNode;
    }

    public static RootNode createRootNode( Node node )
    {
        RootNode rootNode = new RootNode( node );
        rootNode.setDefaultNamespace( DxfNamespaces.DXF_2_0 );
        rootNode.setNamespace( DxfNamespaces.DXF_2_0 );

        return rootNode;
    }

    public static RootNode createEvents()
    {
        RootNode rootNode = new RootNode( "events" );
        rootNode.setDefaultNamespace( DxfNamespaces.DXF_2_0 );
        rootNode.setNamespace( DxfNamespaces.DXF_2_0 );

        return rootNode;
    }

    public static RootNode createMetadata()
    {
        RootNode rootNode = new RootNode( "metadata" );
        rootNode.setDefaultNamespace( DxfNamespaces.DXF_2_0 );
        rootNode.setNamespace( DxfNamespaces.DXF_2_0 );

        return rootNode;
    }

    public static RootNode createMetadata( Node child )
    {
        RootNode rootNode = createMetadata();
        rootNode.addChild( child );

        return rootNode;
    }

    public static Node createPager( Pager pager )
    {
        ComplexNode pagerNode = new ComplexNode( "pager" );
        pagerNode.setMetadata( true );

        pagerNode.addChild( new SimpleNode( "page", pager.getPage() ) );
        pagerNode.addChild( new SimpleNode( "pageCount", pager.getPageCount() ) );
        pagerNode.addChild( new SimpleNode( "total", pager.getTotal() ) );
        pagerNode.addChild( new SimpleNode( "pageSize", pager.getPageSize() ) );
        pagerNode.addChild( new SimpleNode( "nextPage", pager.getNextPage() ) );
        pagerNode.addChild( new SimpleNode( "prevPage", pager.getPrevPage() ) );

        return pagerNode;
    }

    public static Iterable<? extends Node> createSimples( Collection<?> collection )
    {
        return collection.stream().map( o -> new SimpleNode( "", o ) ).collect( Collectors.toList() );
    }

    public static Iterable<? extends Node> createSimples( Map<String, ?> map )
    {
        return map.keySet().stream().map( k -> new SimpleNode( k, map.get( k ) ) ).collect( Collectors
            .toList() );
    }

    private NodeUtils()
    {
    }
}
