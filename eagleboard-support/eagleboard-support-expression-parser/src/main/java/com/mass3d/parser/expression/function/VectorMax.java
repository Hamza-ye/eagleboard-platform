package com.mass3d.parser.expression.function;

import static org.apache.commons.math3.stat.StatUtils.max;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Vector function: max
 *
 */
public class VectorMax
    extends VectorFunctionDoubleArray
{
    @Override
    public Object aggregate( double[] values )
    {
        if ( values.length == 0 )
        {
            return null;
        }

        return max( values );
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return "max(" + visitor.visit( ctx.expr( 0 ) ) + ")";
    }
}
