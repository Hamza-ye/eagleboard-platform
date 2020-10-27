package com.mass3d.expression.dataitem;

import static com.mass3d.expression.ExpressionService.DAYS_DESCRIPTION;
import static com.mass3d.parser.expression.ParserUtils.DOUBLE_VALUE_IF_NULL;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Expression item Days
 *
 */
public class ItemDays
    implements ExpressionItem
{
    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        visitor.getItemDescriptions().put( ctx.getText(), DAYS_DESCRIPTION );

        return DOUBLE_VALUE_IF_NULL;
    }

    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return visitor.getDays();
    }
}
