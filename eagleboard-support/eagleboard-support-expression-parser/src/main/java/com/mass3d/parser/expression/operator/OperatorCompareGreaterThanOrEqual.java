package com.mass3d.parser.expression.operator;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.operator.AntlrOperatorCompareGreaterThanOrEqual;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Compare operator: greater than or equal
 *
 */
public class OperatorCompareGreaterThanOrEqual
    extends AntlrOperatorCompareGreaterThanOrEqual
    implements ExpressionItem
{
    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return visitor.castStringVisit( ctx.expr( 0 ) )
            + " >= " + visitor.castStringVisit( ctx.expr( 1 ) );
    }
}
