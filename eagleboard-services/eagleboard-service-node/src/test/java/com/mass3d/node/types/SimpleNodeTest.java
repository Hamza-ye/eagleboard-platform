package com.mass3d.node.types;

import com.mass3d.node.AbstractNodeTest;
import com.mass3d.schema.Property;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link SimpleNode}.
 *
 */
public class SimpleNodeTest
{
    @Test
    public void createWithProperty()
    {
        final Property property = new Property( AbstractNodeTest.TestClass.class );
        property.setName( "test" );
        property.setNamespace( "testUri" );
        property.setAttribute( true );

        final SimpleNode simpleNode = new SimpleNode( "id", property, "My Test" );
        Assert.assertEquals( "id", simpleNode.getName() );
        Assert.assertEquals( "testUri", simpleNode.getNamespace() );
        Assert.assertTrue( simpleNode.isAttribute() );
        Assert.assertEquals( "My Test", simpleNode.getValue() );
    }
}