package com.mass3d.parser.expression.function;

import static org.hisp.dhis.antlr.AntlrParserUtils.castDouble;
import static com.mass3d.expression.MissingValueStrategy.SKIP_IF_ALL_VALUES_MISSING;
import static com.mass3d.expression.MissingValueStrategy.SKIP_IF_ANY_VALUE_MISSING;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.mass3d.common.DimensionalItemId;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;
import com.mass3d.period.Period;

/**
 * Aggregates a vector of samples (base class).
 *
 */
public abstract class VectorFunction
    implements ExpressionItem
{
    @Override
    public Object getItemId( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        // ItemIds in all but last expr (if any) are from current period.

        for ( int i = 0; i < ctx.expr().size() - 1; i++ )
        {
            visitor.visitExpr( ctx.expr().get( i ) );
        }

        // ItemIds in the last (or only) expr are from sampled periods.

        Set<DimensionalItemId> savedItemIds = visitor.getItemIds();
        visitor.setItemIds( visitor.getSampleItemIds() );

        Object result = visitor.visitExpr( ctx.expr().get( ctx.expr().size() - 1 ) );

        visitor.setItemIds( savedItemIds );

        return castDouble( result );
    }

    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        List<Double> args = new ArrayList<>();

        // All but last expr (if any) are from current period.

        for ( int i = 0; i < ctx.expr().size() - 1; i++ )
        {
            args.add( castDouble( visitor.visitExpr( ctx.expr().get( i ) ) ) );
        }

        // Last (or only) expr is from sampled periods.

        ExprContext lastExpr = ctx.expr().get( ctx.expr().size() - 1 );

        List<Double> values = getSampleValues( lastExpr, visitor );

        return vectorHandleNulls( aggregate( values, args ), visitor );
    }

    /**
     * By default, if there is a null value, count it as a value found for
     * the purpose of missingValueStrategy. This can be overridden by a
     * vector function (like count) that returns a non-null value (0) if
     * no actual values are found.
     *
     * @param value   the value to count (might be null)
     * @param visitor the tree visitor
     * @return the value to return (null might be replaced)
     */
    public Object vectorHandleNulls( Object value, CommonExpressionVisitor visitor )
    {
        return visitor.handleNulls( value );
    }

    /**
     * Aggregates the values, using arguments (if any)
     *
     * @param args the values to aggregate.
     * @param args the arguments (if any) for aggregating the values.
     * @return the aggregated value.
     */
    public abstract Object aggregate( List<Double> values, List<Double> args );

    /**
     * Gets a list of sample values to aggregate.
     *
     * @param ctx the sample expression context
     * @param visitor the tree visitor
     * @return the list of sample values
     *
     * The missingValueStrategy is handled as follows: for each sample expression
     * inside the aggregation function, if there are any sample values missing
     * and the strategy is SKIP_IF_ANY_VALUE_MISSING, then that sample is skipped.
     * Also if all the values are missing and the strategy is
     * SKIP_IF_ALL_VALUES_MISSING, then that sample is skipped.
     *
     * Finally, if there were any items in the sample expression, the count of
     * items in the main expression is incremented. And if there was at least
     * one sample value, the count of item values in the main expression is
     * incremented. This means that if the vector is empty, it counts as a
     * missing value in the main expression.
     */
    private List<Double> getSampleValues( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        int savedItemsFound = visitor.getItemsFound();
        int savedItemValuesFound = visitor.getItemValuesFound();
        Map<String, Double> savedItemValueMap = visitor.getItemValueMap();

        List<Double> values = new ArrayList<>();

        for ( Period p : visitor.getSamplePeriods() )
        {
            visitor.setItemsFound( 0 );
            visitor.setItemValuesFound( 0 );

            if ( visitor.getPeriodItemValueMap() != null &&
                visitor.getPeriodItemValueMap().get( p ) != null )
            {
                visitor.setItemValueMap( visitor.getPeriodItemValueMap().get( p ) );
            }
            else // No samples found in this period:
            {
                visitor.setItemValueMap( new HashMap<>() );
            }

            Double value = castDouble( visitor.visit( ctx ) );

            if ( ( visitor.getMissingValueStrategy() == SKIP_IF_ANY_VALUE_MISSING && visitor.getItemValuesFound() < visitor.getItemsFound() )
                || ( visitor.getMissingValueStrategy() == SKIP_IF_ALL_VALUES_MISSING && visitor.getItemsFound() != 0 && visitor.getItemValuesFound() == 0 ) )
            {
                value = null;
            }

            if ( value != null )
            {
                values.add( value );
            }
        }

        if ( visitor.getItemsFound() > 0 )
        {
            savedItemsFound++;

            if ( !values.isEmpty() )
            {
                savedItemValuesFound++;
            }
        }

        visitor.setItemsFound( savedItemsFound );
        visitor.setItemValuesFound( savedItemValuesFound );
        visitor.setItemValueMap( savedItemValueMap );

        return values;
    }
}
