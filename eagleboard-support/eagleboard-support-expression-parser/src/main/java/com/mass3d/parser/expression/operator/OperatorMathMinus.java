package com.mass3d.parser.expression.operator;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.operator.AntlrOperatorMathMinus;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Math operator: Minus
 *
 */
public class OperatorMathMinus
    extends AntlrOperatorMathMinus
    implements ExpressionItem
{
    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        if ( ctx.expr().size() == 1 ) // Unary minus operator
        {
            return "- " + visitor.castStringVisit( ctx.expr( 0 ) );
        }
        else // Subtraction operator
        {
            return visitor.castStringVisit( ctx.expr( 0 ) ) +
                " - " + visitor.castStringVisit( ctx.expr( 1 ) );
        }
    }
}
