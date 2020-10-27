package com.mass3d.query;

import static org.hamcrest.Matchers.lessThan;

import java.beans.PropertyDescriptor;
import org.apache.commons.beanutils.PropertyUtils;
import com.mass3d.schema.Property;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link Order}.
 *
 */
public class OrderTest
{
    private TestObject object1;

    private TestObject object2;

    private Property valueProperty;

    private Order orderAsc;

    private Order orderDesc;

    @Before
    public void setUp() throws Exception
    {
        object1 = new TestObject();
        object2 = new TestObject();
        PropertyDescriptor propertyDescriptor = PropertyUtils
            .getPropertyDescriptor( object1, "value" );
        valueProperty = new Property( String.class, propertyDescriptor.getReadMethod(), propertyDescriptor.getWriteMethod() );
        valueProperty.setName( "value" );
        orderAsc = new Order( valueProperty, Direction.ASCENDING );
        orderDesc = new Order( valueProperty, Direction.DESCENDING );
    }

    @Test
    public void bothNull()
    {
        Assert.assertEquals( 0, orderAsc.compare( object1, object2 ) );
        Assert.assertEquals( 0, orderAsc.compare( object2, object1 ) );
    }

    @Test
    public void leftNullAsc()
    {
        object2.setValue( "Test" );
        Assert.assertEquals( 1, orderAsc.compare( object1, object2 ) );
    }

    @Test
    public void rightNullAsc()
    {
        object1.setValue( "Test" );
        Assert.assertEquals( -1, orderAsc.compare( object1, object2 ) );
    }

    @Test
    public void leftNullDesc()
    {
        object2.setValue( "Test" );
        Assert.assertEquals( -1, orderDesc.compare( object1, object2 ) );
    }

    @Test
    public void rightNullDesc()
    {
        object1.setValue( "Test" );
        Assert.assertEquals( 1, orderDesc.compare( object1, object2 ) );
    }

    @Test
    public void bothNonNullAsc()
    {
        object1.setValue( "Test1" );
        object2.setValue( "Test2" );
        Assert.assertThat( orderAsc.compare( object1, object2 ), lessThan( 0 ) );
    }

    @Test
    public void toOrderStringAsc()
    {
        Assert.assertEquals( "value:asc", Order.from( "asc", valueProperty ).toOrderString() );
    }

    @Test
    public void toOrderStringDesc()
    {
        Assert.assertEquals( "value:desc", Order.from( "desc", valueProperty ).toOrderString() );
    }

    @Test
    public void toOrderStringIAsc()
    {
        Assert.assertEquals( "value:iasc", Order.from( "iasc", valueProperty ).toOrderString() );
    }

    @Test
    public void toOrderStringIDesc()
    {
        Assert.assertEquals( "value:idesc", Order.from( "idesc", valueProperty ).toOrderString() );
    }

    public static class TestObject
    {
        private String value;

        public String getValue()
        {
            return value;
        }

        public void setValue( String value )
        {
            this.value = value;
        }
    }
}