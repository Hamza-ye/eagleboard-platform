package com.mass3d.parser.expression.function;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType;

/**
 * Vector function: percentile (base class)
 * <p/>
 * All percentile functions take two arguments:
 * <p/>
 * percentile... ( values, fraction )
 * <p/>
 * The percentile is computed according to the EstimationType of the subclass.
 *
 */
public abstract class VectorPercentileBase
    extends VectorFunction
{
    private final Percentile percentile = new Percentile().withEstimationType( getEstimationType() );

    @Override
    public Object aggregate( List<Double> values, List<Double> args )
    {
        Double fraction = args.get( 0 );

        if ( values.size() == 0 || fraction == null || fraction < 0d || fraction > 1d )
        {
            return null;
        }

        Collections.sort( values );

        if ( fraction == 0d )
        {
            return values.get( 0 );
        }

        double[] vals = ArrayUtils.toPrimitive( values.toArray( new Double[0] ) );

        return percentile.evaluate( vals, fraction * 100. );
    }

    /**
     * Each subclass defines its percentile estimation type.
     *
     * @return the percentile estimation type.
     */
    protected abstract EstimationType getEstimationType();
}
