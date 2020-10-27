package com.mass3d.parser.expression.function;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.function.AntlrFunctionLog;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Function log [natural logarithm base e, or log with specified base]
 *
 */
public class FunctionLog
    extends AntlrFunctionLog
    implements ExpressionItem
{
    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        if ( ctx.expr().size() == 1 ) // natural log base e
        {
            return "ln(" + visitor.castStringVisit( ctx.expr( 0 ) ) + ")";
        }
        else // log with specified base
        {
            return "log(" + visitor.castStringVisit( ctx.expr( 1 ) )
                + "," + visitor.castStringVisit( ctx.expr( 0 ) ) + ")";
        }
    }
}
