package com.mass3d.parser.expression.operator;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.operator.AntlrOperatorGroupingParentheses;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Operator to group using parentheses
 *
 */
public class OperatorGroupingParentheses
    extends AntlrOperatorGroupingParentheses
    implements ExpressionItem
{
    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return "(" + visitor.castStringVisit( ctx.expr( 0 ) ) + ")";
    }
}
