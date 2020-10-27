package com.mass3d.parser.expression.function;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Vector function: stddevPop (population standard deviation)
 *
 */
public class VectorStddevPop
    extends VectorFunctionDoubleArray
{
    private final StandardDeviation sdPopulation = new StandardDeviation( false );

    @Override
    public Object aggregate( double[] values )
    {
        if ( values.length == 0 )
        {
            return null;
        }

        return sdPopulation.evaluate( values );
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return "stddev_pop(" + visitor.visit( ctx.expr( 0 ) ) + ")";
    }
}
