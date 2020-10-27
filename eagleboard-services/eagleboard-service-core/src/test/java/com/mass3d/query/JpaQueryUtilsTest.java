package com.mass3d.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import com.mass3d.schema.Property;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link JpaQueryUtils}.
 *
 */
public class JpaQueryUtilsTest
{
    @Test
    public void createOrderExpressionNull()
    {
        Assert.assertNull( JpaQueryUtils.createOrderExpression( null, null ) );
    }

    @Test
    public void createSelectOrderExpressionNull()
    {
        Assert.assertNull( JpaQueryUtils.createSelectOrderExpression( null, null ) );
    }

    @Test
    public void createOrderExpressionEmpty()
    {
        Assert.assertNull( JpaQueryUtils.createOrderExpression( new ArrayList<>(), null ) );
    }

    @Test
    public void createOrderExpressionNoPersistent()
    {
        final Property property = new Property();
        property.setName( "valueTest" );
        property.setSimple( true );
        property.setPersisted( false );
        Assert.assertNull( JpaQueryUtils.createOrderExpression( Collections
            .singletonList( new Order( property, Direction.ASCENDING ) ), null ) );
    }

    @Test
    public void createSelectOrderExpressionNoPersistent()
    {
        final Property property = new Property();
        property.setName( "valueTest" );
        property.setSimple( true );
        property.setPersisted( false );
        Assert.assertNull( JpaQueryUtils.createSelectOrderExpression( Collections
            .singletonList( new Order( property, Direction.ASCENDING ) ), null ) );
    }

    @Test
    public void createOrderExpression()
    {
        final Property property1 = new Property();
        property1.setName( "value1" );
        property1.setSimple( true );
        property1.setPersisted( true );

        final Property property2 = new Property();
        property2.setName( "value2" );
        property2.setSimple( true );
        property2.setPersisted( false );

        final Property property3 = new Property();
        property3.setName( "value3" );
        property3.setSimple( true );
        property3.setKlass( Integer.class );
        property3.setPersisted( true );

        final Property property4 = new Property();
        property4.setName( "value4" );
        property4.setSimple( true );
        property4.setPersisted( true );
        property4.setKlass( String.class );

        final Property property5 = new Property();
        property5.setName( "value5" );
        property5.setSimple( true );
        property5.setPersisted( true );

        Assert.assertEquals( "value1 asc,value3 asc,lower(value4) asc,value5 desc", JpaQueryUtils.createOrderExpression( Arrays
            .asList(
            new Order( property1, Direction.ASCENDING ),
            new Order( property2, Direction.ASCENDING ),
            new Order( property3, Direction.ASCENDING ).ignoreCase(),
            new Order( property4, Direction.ASCENDING ).ignoreCase(),
            new Order( property5, Direction.DESCENDING )
        ), null ) );
    }

    @Test
    public void createSelectOrderExpression()
    {
        final Property property1 = new Property();
        property1.setName( "value1" );
        property1.setSimple( true );
        property1.setPersisted( true );
        property1.setKlass( String.class );

        final Property property2 = new Property();
        property2.setName( "value2" );
        property2.setSimple( true );
        property2.setPersisted( false );

        final Property property3 = new Property();
        property3.setName( "value3" );
        property3.setSimple( true );
        property3.setKlass( Integer.class );
        property3.setPersisted( true );

        final Property property4 = new Property();
        property4.setName( "value4" );
        property4.setSimple( true );
        property4.setPersisted( true );
        property4.setKlass( String.class );

        final Property property5 = new Property();
        property5.setName( "value5" );
        property5.setSimple( true );
        property5.setPersisted( true );

        Assert.assertEquals( "lower(value1),lower(value4)", JpaQueryUtils.createSelectOrderExpression( Arrays
            .asList(
            new Order( property1, Direction.ASCENDING ).ignoreCase(),
            new Order( property2, Direction.ASCENDING ),
            new Order( property3, Direction.ASCENDING ).ignoreCase(),
            new Order( property4, Direction.ASCENDING ).ignoreCase(),
            new Order( property5, Direction.DESCENDING )
        ), null ) );
    }

    @Test
    public void createOrderExpressionSingle()
    {
        final Property property1 = new Property();
        property1.setName( "value1" );
        property1.setSimple( true );
        property1.setPersisted( true );

        Assert.assertEquals( "value1 asc", JpaQueryUtils.createOrderExpression( Collections
            .singletonList( new Order( property1, Direction.ASCENDING ) ), null ) );
    }

    @Test
    public void createSelectOrderExpressionSingle()
    {
        final Property property1 = new Property();
        property1.setName( "value1" );
        property1.setSimple( true );
        property1.setKlass( String.class );
        property1.setPersisted( true );

        Assert.assertEquals( "lower(value1)", JpaQueryUtils.createSelectOrderExpression( Collections
            .singletonList( new Order( property1, Direction.ASCENDING ).ignoreCase() ), null ) );
    }

    @Test
    public void createOrderExpressionAlias()
    {
        final Property property1 = new Property();
        property1.setName( "value1" );
        property1.setSimple( true );
        property1.setPersisted( true );

        Assert.assertEquals( "x.value1 asc", JpaQueryUtils.createOrderExpression( Collections
            .singletonList( new Order( property1, Direction.ASCENDING ) ), "x" ) );
    }

    @Test
    public void createSelectOrderExpressionAlias()
    {
        final Property property1 = new Property();
        property1.setName( "value1" );
        property1.setSimple( true );
        property1.setKlass( String.class );
        property1.setPersisted( true );

        Assert.assertEquals( "lower(x.value1)", JpaQueryUtils.createSelectOrderExpression( Collections
            .singletonList( new Order( property1, Direction.ASCENDING ).ignoreCase() ), "x" ) );
    }
}