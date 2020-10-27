package com.mass3d.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import com.mass3d.node.types.ComplexNode;
import com.mass3d.node.types.RootNode;
import com.mass3d.node.types.SimpleNode;
import org.junit.Test;

public class AbstractNodeTest
{

    private static final String NODE_1 = "node1";

    private static final String NODE_2 = "node2";

    @Test
    public void testRootNodeEquals()
    {
        final RootNode rootNode1 = createRootNode( NODE_1, "propName1", "propValue1" );
        final RootNode rootNode2 = createRootNode( NODE_1, "propName1", "propValue1" );

        assertEquals( rootNode1, rootNode2 );
    }

    @Test
    public void testRootNodeNotEquals()
    {
        final RootNode rootNode1 = createRootNode( NODE_1, "propName1", "propValue1" );
        final RootNode rootNode2 = createRootNode( NODE_1, "propName2", "propValue2" );

        assertNotEquals( rootNode1, rootNode2 );
    }

    private RootNode createRootNode( String nodeName, String propertyName, String propertyValue )
    {
        RootNode rootNode = new RootNode( createComplexNode( nodeName ) );
        rootNode.setDefaultNamespace( "testNamespace" );
        rootNode.getConfig().getProperties().put( propertyName, propertyValue );

        return rootNode;
    }

    @Test
    public void testComplexNodeEquals()
    {
        // Instantiating object 1
        ComplexNode complexNode1 = createComplexNode( NODE_1 );

        // Instantiating object 2
        ComplexNode complexNode2 = createComplexNode( NODE_1 );

        assertEquals( complexNode1, complexNode2 );
    }

    @Test
    public void testComplexNodeNotEquals()
    {
        // Instantiating object 1
        ComplexNode complexNode1 = createComplexNode( NODE_1 );

        // Instantiating object 2
        ComplexNode complexNode2 = createComplexNode( NODE_2 );

        assertNotEquals( complexNode1, complexNode2 );
    }

    private ComplexNode createComplexNode( String node1 )
    {
        ComplexNode complexNode1;
        List<Node> children1 = new ArrayList<>();
        children1.add( new SimpleNode( "id", node1 ) );
        complexNode1 = new ComplexNode( "dataElement" );
        complexNode1.setMetadata( false );
        complexNode1.setChildren( children1 );

        return complexNode1;
    }

}