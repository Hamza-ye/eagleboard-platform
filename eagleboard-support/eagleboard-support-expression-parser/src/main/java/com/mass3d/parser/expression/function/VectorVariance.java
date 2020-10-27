package com.mass3d.parser.expression.function;

import static org.apache.commons.math3.stat.StatUtils.variance;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Vector function: variance (specifically, sample variance)
 *
 */
public class VectorVariance
    extends VectorFunctionDoubleArray
{
    @Override
    public Object aggregate( double[] values )
    {
        if ( values.length == 0 )
        {
            return null;
        }

        return variance( values );
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return "variance(" + visitor.visit( ctx.expr( 0 ) ) + ")";
    }
}
