package com.mass3d.expression.dataitem;

import static com.mass3d.common.DimensionItemType.*;
import static com.mass3d.common.DimensionItemType.PROGRAM_ATTRIBUTE;
import static com.mass3d.parser.expression.ParserUtils.assumeExpressionProgramAttribute;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.common.DimensionalItemId;
import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Expression item ProgramAttribute
 *
 */
public class DimItemProgramAttribute
    extends DimensionalItem
{
    @Override
    public DimensionalItemId getDimensionalItemId( ExprContext ctx,
        CommonExpressionVisitor visitor )
    {
        assumeExpressionProgramAttribute( ctx );

        return new DimensionalItemId( PROGRAM_ATTRIBUTE,
            ctx.uid0.getText(),
            ctx.uid1.getText() );
    }

    @Override
    public String getId( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        assumeExpressionProgramAttribute( ctx );

        return ctx.uid0.getText() + "." +
            ctx.uid1.getText() +
            (visitor.getPeriodOffset() == 0 ? "" : "." + visitor.getPeriodOffset());
    }
}
