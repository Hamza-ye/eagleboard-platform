package com.mass3d.parser.expression.function;

import org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType;

/**
 * Vector function: percentileCont (continuous percentile)
 * <p/>
 * The percentileCont function is equivalent to
 * the PostgreSQL function percentile_cont
 * and the Excel function PERCENTILE.INC
 *
 */
public class VectorPercentileCont
    extends VectorPercentileBase
{
    @Override
    protected EstimationType getEstimationType()
    {
        return EstimationType.R_7;
    }
}
