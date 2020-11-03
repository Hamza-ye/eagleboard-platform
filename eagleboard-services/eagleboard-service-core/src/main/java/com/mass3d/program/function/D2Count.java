package com.mass3d.program.function;

import static com.mass3d.parser.expression.CommonExpressionVisitor.DEFAULT_DOUBLE_VALUE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Program indicator function: d2 count
 *
 */
public class D2Count
    extends ProgramCountFunction
{
    @Override
    public final Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        getProgramStageElementDescription( ctx, visitor );

        return DEFAULT_DOUBLE_VALUE;
    }

    @Override
    public String getConditionSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return " is not null";
    }
}
