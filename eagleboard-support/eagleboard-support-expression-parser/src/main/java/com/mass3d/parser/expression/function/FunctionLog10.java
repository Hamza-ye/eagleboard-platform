package com.mass3d.parser.expression.function;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.function.AntlrFunctionLog10;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Function log [common logarithm base 10]
 *
 */
public class FunctionLog10
    extends AntlrFunctionLog10
    implements ExpressionItem
{
    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return "log(" + visitor.castStringVisit( ctx.expr( 0 ) ) + ")";
    }
}
