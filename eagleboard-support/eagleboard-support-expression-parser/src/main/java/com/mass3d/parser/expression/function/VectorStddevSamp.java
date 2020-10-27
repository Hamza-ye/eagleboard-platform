package com.mass3d.parser.expression.function;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Vector function: stddevSamp (sample standard deviation)
 *
 */
public class VectorStddevSamp
    extends VectorFunctionDoubleArray
{
    private final StandardDeviation sdSample = new StandardDeviation( true );

    @Override
    public Object aggregate( double[] values )
    {
        if ( values.length == 0 )
        {
            return null;
        }

        return sdSample.evaluate( values );
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return "stddev_samp(" + visitor.visit( ctx.expr( 0 ) ) + ")";
    }
}
