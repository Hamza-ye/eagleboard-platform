package com.mass3d.node;

import com.mass3d.node.types.SimpleNode;
import com.mass3d.schema.Property;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link AbstractNode}.
 *
 */
public class AbstractNodeTest
{
    @Test
    public void createSingleChild()
    {
        final SimpleNode simpleNode = new SimpleNode( "id", "My Test" );
        final TestNode testNode = new TestNode( "tests", NodeType.COMPLEX, new Property( TestClass.class ), simpleNode );
        Assert.assertEquals( "tests", testNode.getName() );
        Assert.assertEquals( NodeType.COMPLEX, testNode.nodeType );
        Assert.assertEquals( TestClass.class, testNode.getProperty().getKlass() );
        Assert.assertEquals( 1, testNode.getUnorderedChildren().size() );
        Assert.assertSame( simpleNode, testNode.getUnorderedChildren().get( 0 ) );
    }

    public static class TestNode extends AbstractNode
    {
        public TestNode( String name, NodeType nodeType )
        {
            super( name, nodeType );
        }

        public TestNode( String name, NodeType nodeType, Property property, AbstractNode child )
        {
            super( name, nodeType, property, child );
        }
    }

    public static class TestClass
    {
        // nothing to define
    }
}