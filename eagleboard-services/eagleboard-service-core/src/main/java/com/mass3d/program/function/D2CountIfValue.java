package com.mass3d.program.function;

import static org.hisp.dhis.antlr.AntlrParserUtils.castClass;
import static com.mass3d.parser.expression.CommonExpressionVisitor.DEFAULT_DOUBLE_VALUE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Program indicator function: d2 count if value
 *
 */
public class D2CountIfValue
    extends ProgramCountFunction
{
    @Override
    public final Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        Object programStageElement = getProgramStageElementDescription( ctx, visitor );

        Object value = visitor.visit( ctx.expr( 0 ) );

        castClass( programStageElement.getClass(), value ); // Check that we are comparing same data types.

        return DEFAULT_DOUBLE_VALUE;
    }

    @Override
    public String getConditionSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return " = " + visitor.visit( ctx.expr( 0 ) );
    }
}
