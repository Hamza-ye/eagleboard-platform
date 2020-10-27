package com.mass3d.expression.dataitem;

import static com.mass3d.common.DimensionItemType.PROGRAM_INDICATOR;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.common.DimensionalItemId;
import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Expression item ProgramIndicator
 *
 */
public class DimItemProgramIndicator
    extends DimensionalItem
{
    @Override
    public DimensionalItemId getDimensionalItemId( ExprContext ctx,
        CommonExpressionVisitor visitor )
    {
        return new DimensionalItemId( PROGRAM_INDICATOR,
            ctx.uid0.getText() );
    }

    @Override
    public String getId( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return ctx.uid0.getText() +
            (visitor.getPeriodOffset() == 0 ? "" : "." + visitor.getPeriodOffset());
    }
}
