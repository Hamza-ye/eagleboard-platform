package com.mass3d.parser.expression.function;

import static org.hisp.dhis.antlr.AntlrParserUtils.castClass;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Function if
 * <pre>
 *
 * In-memory Logic:
 *
 *     arg0   returns
 *     ----   -------
 *     true   arg1
 *     false  arg2
 *     null   null
 *
 * SQL logic (CASE WHEN arg0 THEN arg1 ELSE arg2 END):
 *
 *     arg0   returns
 *     ----   -------
 *     true   arg1
 *     false  arg2
 *     null   arg2
 * </pre>
 *
 */
public class FunctionIf
    implements ExpressionItem
{
    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        Boolean arg0 = visitor.castBooleanVisit( ctx.expr( 0 ) );

        return arg0 == null
            ? null
            : arg0
            ? visitor.visit( ctx.expr( 1 ) )
            : visitor.visit( ctx.expr( 2 ) );
    }

    @Override
    public Object evaluateAllPaths( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        Boolean arg0 = visitor.castBooleanVisit( ctx.expr( 0 ) );
        Object arg1 = visitor.visit( ctx.expr( 1 ) );
        Object arg2 = visitor.visit( ctx.expr( 2 ) );

        if ( arg1 != null )
        {
            castClass( arg1.getClass(), arg2 );
        }

        return arg0 != null && arg0 ? arg1 : arg2;
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return " case when " + visitor.castStringVisit( ctx.expr( 0 ) ) +
            " then " + visitor.castStringVisit( ctx.expr( 1 ) ) +
            " else " + visitor.castStringVisit( ctx.expr( 2 ) ) + " end";
    }
}
