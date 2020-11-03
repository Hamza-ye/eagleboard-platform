package com.mass3d.program.function;

import static org.hisp.dhis.antlr.AntlrParserUtils.castDouble;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.ProgramExpressionItem;

/**
 * Program indicator function: d2 oizp, Object Is Zero or Positive
 *
 */
public class D2Oizp
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
        return "coalesce(case when " + visitor.visitAllowingNulls( ctx.expr( 0 ) ) + " >= 0 then 1 else 0 end, 0)";
    }
}
