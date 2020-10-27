package com.mass3d.commons.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExpressionFunctionsTest
{    
    @Test
    public void testDaysBetween()
    {
        assertEquals( new Long( 1 ), ExpressionFunctions.daysBetween( "2014-03-04", "2014-03-05" ) );
        assertEquals( new Long( 32 ), ExpressionFunctions.daysBetween( "2015-04-04", "2015-05-06" ) );
    }
}
