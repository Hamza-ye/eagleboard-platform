package com.mass3d.parser.expression.function;

import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Aggregates a vector of double[] values (primative array)
 *
 */
public abstract class VectorFunctionDoubleArray
    extends VectorFunction
{
    @Override
    public final Object aggregate( List<Double> values, List<Double> args )
    {
        return aggregate( ArrayUtils.toPrimitive( values.toArray( new Double[0] ) ) );
    }

    /**
     * Aggregates the values, using arguments (if any)
     *
     * @param values the values to aggregate.
     * @return the aggregated value.
     */
    public abstract Object aggregate( double[] values );
}
