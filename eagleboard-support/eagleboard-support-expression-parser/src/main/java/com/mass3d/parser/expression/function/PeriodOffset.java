package com.mass3d.parser.expression.function;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Function least
 *
 */
public class PeriodOffset
    extends FunctionGreatestOrLeast
{
    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        int offset = ctx.period != null ? Integer.valueOf( ctx.period.getText() ) : 0;

        return visitor.visitWithOffset( ctx.expr( 0 ), offset );
    }
}
