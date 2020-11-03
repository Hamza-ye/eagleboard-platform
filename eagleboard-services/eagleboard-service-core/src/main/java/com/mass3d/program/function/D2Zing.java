package com.mass3d.program.function;

import static org.hisp.dhis.antlr.AntlrParserUtils.castDouble;
import static org.hisp.dhis.antlr.AntlrParserUtils.castString;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.ProgramExpressionItem;

/**
 * Program indicator function: d2 zing, Zero If Negative
 *
 */
public class D2Zing
    extends ProgramExpressionItem
{
    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return castDouble( visitor.visit( ctx.expr( 0 ) ) );
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return "greatest(0," + castString( visitor.visit( ctx.expr( 0 ) ) ) + ")";
    }
}
