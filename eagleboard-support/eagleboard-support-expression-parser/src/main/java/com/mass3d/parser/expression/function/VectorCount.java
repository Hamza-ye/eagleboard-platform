package com.mass3d.parser.expression.function;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Vector function: count
 *
 */
public class VectorCount
    extends VectorFunctionDoubleArray
{
    @Override
    public Object aggregate( double[] values )
    {
        return Double.valueOf( values.length );
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return "count(" + visitor.visit( ctx.expr( 0 ) ) + ")";
    }
}
