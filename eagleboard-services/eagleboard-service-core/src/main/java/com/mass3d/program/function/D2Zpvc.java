package com.mass3d.program.function;

import static org.hisp.dhis.antlr.AntlrParserUtils.castDouble;
import static com.mass3d.parser.expression.CommonExpressionVisitor.DEFAULT_DOUBLE_VALUE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.commons.util.TextUtils;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.ProgramExpressionItem;

/**
 * Program indicator function: d2 zpvc, Zero or Positive Value Count
 *
 */
public class D2Zpvc
    extends ProgramExpressionItem
{
    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        ctx.expr().stream().forEach( i -> castDouble( visitor.visit( i ) ) );

        return DEFAULT_DOUBLE_VALUE;
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String sql = "nullif(cast((";

        for ( ExprContext c :  ctx.expr() )
        {
            sql += "case when " + visitor.visitAllowingNulls( c ) + " >= 0 then 1 else 0 end + ";
        }

        return TextUtils.removeLast( sql, "+" ).trim() + ") as double precision),0)";
    }
}
