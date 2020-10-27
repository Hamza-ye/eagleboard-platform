package com.mass3d.parser.expression.function;

import static org.hisp.dhis.antlr.AntlrParserUtils.castString;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Function isNull
 *
 */
public class FunctionIsNotNull
    implements ExpressionItem
{
    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return visitor.visitAllowingNulls( ctx.expr( 0 ) ) != null;
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return castString( visitor.visitAllowingNulls( ctx.expr( 0 ) ) ) + " is not null";
    }
}
