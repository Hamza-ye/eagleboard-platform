package com.mass3d.expression.dataitem;

import static com.mass3d.common.DimensionItemType.PROGRAM_DATA_ELEMENT;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.common.DimensionalItemId;
import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Expression item ProgramDataElement
 *
 */
public class DimItemProgramDataElement
    extends DimensionalItem
{
    @Override
    public DimensionalItemId getDimensionalItemId( ExprContext ctx,
        CommonExpressionVisitor visitor )
    {
        return new DimensionalItemId( PROGRAM_DATA_ELEMENT,
            ctx.uid0.getText(),
            ctx.uid1.getText() );
    }

    @Override
    public String getId( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return ctx.uid0.getText() + "." +
            ctx.uid1.getText() +
            (visitor.getPeriodOffset() == 0 ? "" : "." + visitor.getPeriodOffset());
    }
}
