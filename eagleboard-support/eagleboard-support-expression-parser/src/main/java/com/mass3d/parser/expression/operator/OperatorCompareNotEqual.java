package com.mass3d.parser.expression.operator;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.operator.AntlrOperatorCompareNotEqual;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Compare operator: not equal
 *
 */
public class OperatorCompareNotEqual
    extends AntlrOperatorCompareNotEqual
    implements ExpressionItem
{
    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return visitor.castStringVisit( ctx.expr( 0 ) )
            + " != " + visitor.castStringVisit( ctx.expr( 1 ) );
    }
}
