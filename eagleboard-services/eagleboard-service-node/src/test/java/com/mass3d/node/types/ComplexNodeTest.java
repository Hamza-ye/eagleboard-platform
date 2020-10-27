package com.mass3d.node.types;

import com.mass3d.node.AbstractNodeTest;
import com.mass3d.schema.Property;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link ComplexNode}.
 *
 */
public class ComplexNodeTest
{
    @Test
    public void createSingleChild()
    {
        final Property property = new Property( AbstractNodeTest.TestClass.class );
        property.setName( "tests" );
        property.setNamespace( "testUri" );

        final SimpleNode simpleNode = new SimpleNode( "id", "My Test" );

        final ComplexNode testNode = new ComplexNode( property, simpleNode );
        Assert.assertEquals( "tests", testNode.getName() );
        Assert.assertEquals( "testUri", testNode.getNamespace() );
        Assert.assertEquals( AbstractNodeTest.TestClass.class, testNode.getProperty().getKlass() );
        Assert.assertEquals( 1, testNode.getUnorderedChildren().size() );
        Assert.assertSame( simpleNode, testNode.getUnorderedChildren().get( 0 ) );
    }
}