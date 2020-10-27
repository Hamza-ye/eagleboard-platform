package com.mass3d.node.types;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link CollectionNodeTest}.
 *
 */
public class CollectionNodeTest
{
    @Test
    public void createEmpty()
    {
        final CollectionNode collectionNode = new CollectionNode( "tests", 0 );
        Assert.assertEquals( "tests", collectionNode.getName() );
        Assert.assertEquals( 0, collectionNode.getUnorderedChildren().size() );
    }

    @Test
    public void createNonEmpty()
    {
        final CollectionNode collectionNode = new CollectionNode( "tests", 10 );
        Assert.assertEquals( "tests", collectionNode.getName() );
        Assert.assertEquals( 0, collectionNode.getUnorderedChildren().size() );
    }

    @Test
    public void getChildren()
    {
        final CollectionNode collectionNode = new CollectionNode( "tests", 0 );

        final SimpleNode simpleNode1 = new SimpleNode( "id", "My Test 1" )
        {
            @Override
            public int getOrder()
            {
                return 10;
            }
        };
        final SimpleNode simpleNode2 = new SimpleNode( "id", "My Test 2" )
        {
            @Override
            public int getOrder()
            {
                return 5;
            }
        };
        final SimpleNode simpleNode3 = new SimpleNode( "id", "My Test 3" )
        {
            @Override
            public int getOrder()
            {
                return 15;
            }
        };

        collectionNode.addChild( simpleNode1 );
        collectionNode.addChild( simpleNode2 );
        collectionNode.addChild( simpleNode3 );

        Assert.assertThat( collectionNode.getChildren(), Matchers.contains( simpleNode2, simpleNode1, simpleNode3 ) );
    }

    @Test
    public void getEmptyChildren()
    {
        final CollectionNode collectionNode = new CollectionNode( "tests", 0 );
        Assert.assertEquals( 0, collectionNode.getChildren().size() );
    }

    @Test
    public void getSingleChildren()
    {
        final CollectionNode collectionNode = new CollectionNode( "tests", 0 );
        final SimpleNode simpleNode1 = new SimpleNode( "id", "My Test 1" );
        collectionNode.addChild( simpleNode1 );
        Assert.assertThat( collectionNode.getChildren(), Matchers.contains( simpleNode1 ) );
    }
}