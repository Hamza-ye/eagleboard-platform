package com.mass3d.parser.expression.operator;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.operator.AntlrOperatorMathPlus;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Expression math operator: Plus
 *
 */
public class OperatorMathPlus
    extends AntlrOperatorMathPlus
    implements ExpressionItem
{
    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        if ( ctx.expr().size() == 1 ) // Unary plus operator
        {
            return "+ " + visitor.castStringVisit( ctx.expr( 0 ) );
        }
        else // Addition operator
        {
            return visitor.castStringVisit( ctx.expr( 0 ) ) +
                " + " + visitor.castStringVisit( ctx.expr( 1 ) );
        }
    }
}
