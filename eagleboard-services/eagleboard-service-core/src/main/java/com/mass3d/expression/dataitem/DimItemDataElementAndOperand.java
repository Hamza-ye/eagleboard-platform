package com.mass3d.expression.dataitem;

import static org.apache.commons.lang3.ObjectUtils.anyNotNull;
import static com.mass3d.common.DimensionItemType.DATA_ELEMENT;
import static com.mass3d.common.DimensionItemType.DATA_ELEMENT_OPERAND;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import com.mass3d.common.DimensionalItemId;
import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Expression items DataElement and DataElementOperand
 *
 */
public class DimItemDataElementAndOperand
    extends DimensionalItem
{
    @Override
    public DimensionalItemId getDimensionalItemId( ExprContext ctx,
        CommonExpressionVisitor visitor )
    {
        if ( isDataElementOperandSyntax( ctx ) )
        {
            return new DimensionalItemId( DATA_ELEMENT_OPERAND,
                ctx.uid0.getText(),
                ctx.uid1 == null ? null : ctx.uid1.getText(),
                ctx.uid2 == null ? null : ctx.uid2.getText(),
                visitor.getPeriodOffset() );
        }
        else
        {
            return new DimensionalItemId( DATA_ELEMENT,
                ctx.uid0.getText(), visitor.getPeriodOffset() );
        }
    }

    @Override
    public String getId( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        if ( isDataElementOperandSyntax( ctx ) )
        {
            return ctx.uid0.getText() + "." +
                (ctx.uid1 == null ? "*" : ctx.uid1.getText()) +
                (ctx.uid2 == null ? "" : "." + ctx.uid2.getText()) +
                (visitor.getPeriodOffset() == 0 ? "" : "." + visitor.getPeriodOffset());
        }
        else // Data element:
        {
            return ctx.uid0.getText() + (visitor.getPeriodOffset() == 0 ? "" : "." + visitor.getPeriodOffset());
        }
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Does an item of the form #{...} have the syntax of a data element operand (as
     * opposed to a data element)?
     *
     * @param ctx the item context
     * @return true if data element operand syntax
     */
    private boolean isDataElementOperandSyntax( ExprContext ctx )
    {
        if ( ctx.uid0 == null )
        {
            throw new ParserExceptionWithoutContext(
                "Data Element or DataElementOperand must have a uid " + ctx.getText() );
        }

        return anyNotNull( ctx.uid1, ctx.uid2 );
    }
}
