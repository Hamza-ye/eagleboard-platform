package com.mass3d.query;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import com.mass3d.query.operators.BetweenOperator;
import com.mass3d.query.operators.EqualOperator;
import com.mass3d.query.operators.GreaterEqualOperator;
import com.mass3d.query.operators.GreaterThanOperator;
import com.mass3d.query.operators.InOperator;
import com.mass3d.query.operators.LessEqualOperator;
import com.mass3d.query.operators.LessThanOperator;
import com.mass3d.query.operators.LikeOperator;
import com.mass3d.query.operators.MatchMode;
import com.mass3d.query.operators.NotEqualOperator;
import com.mass3d.query.operators.NotNullOperator;
import com.mass3d.query.operators.NullOperator;
import org.junit.Test;

public class OperatorTest
{
    enum TestEnum
    {
        A, B, C
    }

    @Test
    public void testBetweenValidTypes()
    {
        BetweenOperator operator = new BetweenOperator( "10", "20" );

        assertTrue( operator.isValid( String.class ) );
        assertTrue( operator.isValid( Number.class ) );
        assertTrue( operator.isValid( Date.class ) );
        assertFalse( operator.isValid( Collection.class ) );
    }

    @Test
    public void testBetweenInt()
    {
        BetweenOperator operator = new BetweenOperator( "10", "20" );

        assertTrue( operator.test( 10 ) );
        assertTrue( operator.test( 15 ) );
        assertTrue( operator.test( 20 ) );
        assertFalse( operator.test( 9 ) );
        assertFalse( operator.test( 21 ) );
    }

    @Test
    public void testBetweenCollection()
    {
        BetweenOperator operator = new BetweenOperator( "2", "4" );

        assertFalse( operator.test( Collections.singletonList( 1 ) ) );
        assertTrue( operator.test( Arrays.asList( 1, 2 ) ) );
        assertTrue( operator.test( Arrays.asList( 1, 2, 3 ) ) );
        assertTrue( operator.test( Arrays.asList( 1, 2, 3, 4 ) ) );
        assertFalse( operator.test( Arrays.asList( 1, 2, 3, 4, 5 ) ) );
    }

    @Test
    public void testEqualValidTypes()
    {
        EqualOperator operator = new EqualOperator( "operator" );

        assertTrue( operator.isValid( String.class ) );
        assertTrue( operator.isValid( Number.class ) );
        assertTrue( operator.isValid( Date.class ) );
        assertTrue( operator.isValid( Boolean.class ) );
        assertTrue( operator.isValid( Enum.class ) );
        assertFalse( operator.isValid( Collection.class ) );
    }

    @Test
    public void testEqual()
    {
        EqualOperator operator = new EqualOperator( "operator" );
        assertTrue( operator.test( "operator" ) );
        assertFalse( operator.test( "not operator" ) );
    }

    @Test
    public void testEqualEnum()
    {
        assertTrue( new EqualOperator( "A" ).test( TestEnum.A ) );
        assertTrue( new EqualOperator( "B" ).test( TestEnum.B ) );
        assertTrue( new EqualOperator( "C" ).test( TestEnum.C ) );

        assertFalse( new EqualOperator( "A" ).test( "abc" ) );
    }

    @Test
    public void testNotEqualValidTypes()
    {
        NotEqualOperator operator = new NotEqualOperator( "operator" );

        assertTrue( operator.isValid( String.class ) );
        assertTrue( operator.isValid( Number.class ) );
        assertTrue( operator.isValid( Date.class ) );
        assertTrue( operator.isValid( Boolean.class ) );
        assertFalse( operator.isValid( Collection.class ) );
    }

    @Test
    public void testNotEqual()
    {
        NotEqualOperator operator = new NotEqualOperator( "operator" );

        assertFalse( operator.test( "operator" ) );
        assertTrue( operator.test( Boolean.TRUE ) );
    }

    @Test
    public void testGreaterEqualValidTypes()
    {
        GreaterEqualOperator operator = new GreaterEqualOperator( "operator" );

        assertTrue( operator.isValid( String.class ) );
        assertTrue( operator.isValid( Number.class ) );
        assertTrue( operator.isValid( Date.class ) );
        assertTrue( operator.isValid( Boolean.class ) );
        assertFalse( operator.isValid( Collection.class ) );
    }

    @Test
    public void testGreaterEqual()
    {
        GreaterEqualOperator operator = new GreaterEqualOperator( "10" );

        assertFalse( operator.test( 6 ) );
        assertFalse( operator.test( 7 ) );
        assertFalse( operator.test( 8 ) );
        assertFalse( operator.test( 9 ) );
        assertTrue( operator.test( 10 ) );
        assertTrue( operator.test( 11 ) );
        assertTrue( operator.test( 12 ) );
        assertTrue( operator.test( 13 ) );
    }

    @Test
    public void testGreaterThanValidTypes()
    {
        GreaterThanOperator operator = new GreaterThanOperator( "operator" );

        assertTrue( operator.isValid( String.class ) );
        assertTrue( operator.isValid( Number.class ) );
        assertTrue( operator.isValid( Date.class ) );
        assertTrue( operator.isValid( Boolean.class ) );
        assertFalse( operator.isValid( Collection.class ) );
    }

    @Test
    public void testGreaterThan()
    {
        GreaterThanOperator operator = new GreaterThanOperator( "10" );

        assertFalse( operator.test( 6 ) );
        assertFalse( operator.test( 7 ) );
        assertFalse( operator.test( 8 ) );
        assertFalse( operator.test( 9 ) );
        assertFalse( operator.test( 10 ) );
        assertTrue( operator.test( 11 ) );
        assertTrue( operator.test( 12 ) );
        assertTrue( operator.test( 13 ) );
    }

    @Test
    public void testLikeValidTypes()
    {
        LikeOperator operator = new LikeOperator( "operator", true, MatchMode.ANYWHERE );

        assertTrue( operator.isValid( String.class ) );
        assertFalse( operator.isValid( Number.class ) );
        assertFalse( operator.isValid( Date.class ) );
        assertFalse( operator.isValid( Boolean.class ) );
        assertFalse( operator.isValid( Collection.class ) );
    }

    @Test
    public void testLikeAnywhere()
    {
        LikeOperator operator = new LikeOperator( "oper", true, MatchMode.ANYWHERE );

        assertTrue( operator.test( "operator" ) );
        assertFalse( operator.test( "OPERATOR" ) );
        assertFalse( operator.test( "abc" ) );
    }

    @Test
    public void testLikeStart()
    {
        LikeOperator operator = new LikeOperator( "oper", true, MatchMode.START );

        assertTrue( operator.test( "operator" ) );
        assertFalse( operator.test( "OPERATOR" ) );
        assertFalse( operator.test( "abc" ) );
    }

    @Test
    public void testLikeEnd()
    {
        LikeOperator operator = new LikeOperator( "ator", true, MatchMode.END );

        assertTrue( operator.test( "operator" ) );
        assertFalse( operator.test( "OPERATOR" ) );
        assertFalse( operator.test( "abc" ) );
    }

    @Test
    public void testILikeAnywhere()
    {
        LikeOperator operator = new LikeOperator( "erat", false, MatchMode.ANYWHERE );

        assertTrue( operator.test( "operator" ) );
        assertTrue( operator.test( "OPERATOR" ) );
        assertFalse( operator.test( "abc" ) );
    }

    @Test
    public void testILikeStart()
    {
        LikeOperator operator = new LikeOperator( "oper", false, MatchMode.START );

        assertTrue( operator.test( "operator" ) );
        assertTrue( operator.test( "OPERATOR" ) );
        assertFalse( operator.test( "abc" ) );
    }

    @Test
    public void testILikeEnd()
    {
        LikeOperator operator = new LikeOperator( "ator", false, MatchMode.END );

        assertTrue( operator.test( "operator" ) );
        assertTrue( operator.test( "OPERATOR" ) );
        assertFalse( operator.test( "abc" ) );
    }

    @Test
    public void testLessEqualValidTypes()
    {
        LessEqualOperator operator = new LessEqualOperator( "operator" );

        assertTrue( operator.isValid( String.class ) );
        assertTrue( operator.isValid( Number.class ) );
        assertTrue( operator.isValid( Date.class ) );
        assertTrue( operator.isValid( Boolean.class ) );
        assertFalse( operator.isValid( Collection.class ) );
    }

    @Test
    public void testLessEqual()
    {
        LessEqualOperator operator = new LessEqualOperator( "10" );

        assertTrue( operator.test( 6 ) );
        assertTrue( operator.test( 7 ) );
        assertTrue( operator.test( 8 ) );
        assertTrue( operator.test( 9 ) );
        assertTrue( operator.test( 10 ) );
        assertFalse( operator.test( 11 ) );
        assertFalse( operator.test( 12 ) );
        assertFalse( operator.test( 13 ) );
    }

    @Test
    public void testLessThanValidTypes()
    {
        LessThanOperator operator = new LessThanOperator( "operator" );

        assertTrue( operator.isValid( String.class ) );
        assertTrue( operator.isValid( Number.class ) );
        assertTrue( operator.isValid( Date.class ) );
        assertTrue( operator.isValid( Boolean.class ) );
        assertFalse( operator.isValid( Collection.class ) );
    }

    @Test
    public void testLessThan()
    {
        LessThanOperator operator = new LessThanOperator( "10" );

        assertTrue( operator.test( 6 ) );
        assertTrue( operator.test( 7 ) );
        assertTrue( operator.test( 8 ) );
        assertTrue( operator.test( 9 ) );
        assertFalse( operator.test( 10 ) );
        assertFalse( operator.test( 11 ) );
        assertFalse( operator.test( 12 ) );
        assertFalse( operator.test( 13 ) );
    }

    @Test
    public void testNullValidTypes()
    {
        NullOperator operator = new NullOperator();

        assertTrue( operator.isValid( String.class ) );
        assertTrue( operator.isValid( Number.class ) );
        assertTrue( operator.isValid( Date.class ) );
        assertTrue( operator.isValid( Boolean.class ) );
        assertFalse( operator.isValid( Collection.class ) );
    }

    @Test
    public void testNull()
    {
        NullOperator operator = new NullOperator();

        assertTrue( operator.test( null ) );
        assertFalse( operator.test( "test" ) );
    }

    @Test
    public void testNotNullValidTypes()
    {
        NotNullOperator operator = new NotNullOperator();

        assertTrue( operator.isValid( String.class ) );
        assertTrue( operator.isValid( Number.class ) );
        assertTrue( operator.isValid( Date.class ) );
        assertTrue( operator.isValid( Boolean.class ) );
        assertFalse( operator.isValid( Collection.class ) );
    }

    @Test
    public void testNotNull()
    {
        NotNullOperator operator = new NotNullOperator();

        assertFalse( operator.test( null ) );
        assertTrue( operator.test( "test" ) );
    }

    @Test
    public void testInValidTypes()
    {
        InOperator operator = new InOperator( Arrays.asList( 1, 2, 3 ) );
        assertTrue( operator.isValid( Collection.class ) );
    }

    @Test
    public void testInInt()
    {
        InOperator operator = new InOperator( Arrays.asList( 1, 2, 3 ) );

        assertFalse( operator.test( 0 ) );
        assertTrue( operator.test( 1 ) );
        assertTrue( operator.test( 2 ) );
        assertTrue( operator.test( 3 ) );
        assertFalse( operator.test( 4 ) );
    }

    @Test
    public void testInString()
    {
        InOperator operator = new InOperator( Arrays.asList( "b", "c", "d" ) );

        assertFalse( operator.test( "a" ) );
        assertTrue( operator.test( "b" ) );
        assertTrue( operator.test( "c" ) );
        assertTrue( operator.test( "d" ) );
        assertFalse( operator.test( "e" ) );
    }

    @Test
    public void testInEnum()
    {
        InOperator operator = new InOperator( Arrays.asList( "A", "B" ) );

        assertTrue( operator.test( TestEnum.A ) );
        assertTrue( operator.test( TestEnum.B ) );
        assertFalse( operator.test( TestEnum.C ) );
        assertFalse( operator.test( "abc" ) );
    }
}
