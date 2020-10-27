package com.mass3d.parser.expression.function;

import java.util.Arrays;
import java.util.List;

/**
 * Vector function: median
 *
 */
public class VectorMedian
    extends VectorFunction
{
    private static VectorPercentileCont percentileContinuous = new VectorPercentileCont();

    @Override
    public Object aggregate( List<Double> values, List<Double> args )
    {
        return percentileContinuous.aggregate( values, Arrays.asList( .5 ) );
    }
}
