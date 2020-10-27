package com.mass3d.parser.expression.function;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import java.util.List;
import java.util.stream.Collectors;
import org.hisp.dhis.antlr.AntlrExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Abstract function for greatest or least
 *
 */
public abstract class FunctionGreatestOrLeast
    implements ExpressionItem
{
    /**
     * Returns the greatest or least value.
     *
     * @param contexts      the expr contexts.
     * @param greatestLeast 1.0 for greatest, -1.0 for least.
     * @return the greatest or least value.
     */
    protected Double greatestOrLeast( List<ExprContext> contexts, AntlrExpressionVisitor visitor, double greatestLeast )
    {
        List<Double> values = contexts.stream()
            .map( c -> visitor.castDoubleVisit( c ) )
            .collect( Collectors.toList() );

        Double returnVal = null;

        for ( Double val : values )
        {
            if ( returnVal == null || val != null && (val - returnVal) * greatestLeast > 0 )
            {
                returnVal = val;
            }
        }
        return returnVal;
    }
}
